package com.practice.efubaccount.account.service;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.dto.response.TokenResponseDto;
import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import com.practice.efubaccount.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AccountService accountService;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken 재발급:
     * 클라이언트가 전달한 리프레시 토큰을 검증해 유효한 경우 새로운 액세스토큰을 발급한다.
     */
    public TokenResponseDto reissueAccessToken(String refreshToken){
        // TODO 1.전달받은 리프레시 토큰에서 이메일을 추출하여 사용자 정보 가져오기
        String email = tokenProvider.extractEmail(refreshToken);
        Account account = accountService.findByEmail(email);

        // TODO 2.Redis에서 해당 사용자 Id를 키로 하는 리프래시 토큰 가져오기
        String storedRefreshToken = redisTemplate.opsForValue().get(account.getAccountId().toString());

        // TODO 3.전달받은 리프레시 토큰과 Redis에 저장된 리프레시 토큰이 일치하는지 확인
        if(storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // TODO 4.일치한다면 새로운 AccessToken 생성
        String accessToken = tokenProvider.createAccessToken(account);

        //TODO 5. accessToken 반환
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
