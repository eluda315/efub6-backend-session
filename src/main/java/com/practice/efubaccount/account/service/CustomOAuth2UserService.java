package com.practice.efubaccount.account.service;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.repository.AccountRepository;
import com.practice.efubaccount.global.utils.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountRepository accountRepository;

    //OAuth2UserRequest를 받아 사용자를 로드하는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 사용자 정보 로드
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 구글 OAuth2UserInfo 객체 생성
        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfo(oAuth2User.getAttributes());

        // TODO: DB에서 해당 사용자 조회 -> 없으면 새로 생성
        Account account = accountRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(()-> createAccount(oAuth2UserInfo));

        // TODO: 사용자 속성 생성
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("id", account.getAccountId());
        attributes.put("email", account.getEmail());


        // DefaultOAuth2User 객체 생성하여 반환
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "email"); // 기본 식별자 지정
    }

    //사용자 생성 메서드 : OAuth2로그인은 비밀번호가 필요하지 않으므로 ""로 처리
    //TODO: 처음으로 로그인 시도하는 유저 정보를 받아 이메일, 비밀번호, 닉네임의 정보가 있는 사용자 생성
    private Account createAccount(OAuth2UserInfo oAuth2UserInfo) {
        Account account = Account.builder()
                .email(oAuth2UserInfo.getEmail())
                .password("")
                .nickname(oAuth2UserInfo.getNickname())
                .build();

        return accountRepository.save(account);
    }
}

