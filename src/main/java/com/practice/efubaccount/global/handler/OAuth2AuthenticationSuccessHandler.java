package com.practice.efubaccount.global.handler;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.repository.AccountRepository;
import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import com.practice.efubaccount.global.jwt.TokenProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final AccountRepository accountRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // TODO 1.OAuth2 인증된 사용자 정보 가져오기
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        //TODO 2.사용자 속성에서 이메일 추출
        String email = (String) oAuth2User.getAttributes().get("email");

        // TODO 3.email을 통해 데이터베이스에서 User 엔티티 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // TODO 4.AccessToken, RefreshToken 발급
        String accessToken = tokenProvider.createAccessToken(account);
        String refreshToken = tokenProvider.createRefreshToken(account);

        // TODO 5.리프레시토큰을 redis에 저장
        tokenProvider.saveRefreshToken(account.getAccountId(), refreshToken);

        // JSON형식 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // AccessToken, RefreshToken 토큰 전달
        String jsonResponse = String.format("{\"accessToken\":\"%s\", \"refreshToken\":\"%s\"}", accessToken, refreshToken);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
