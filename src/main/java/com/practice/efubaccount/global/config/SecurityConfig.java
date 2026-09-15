package com.practice.efubaccount.global.config;

import com.practice.efubaccount.account.service.CustomOAuth2UserService;
import com.practice.efubaccount.global.handler.OAuth2AuthenticationSuccessHandler;
import com.practice.efubaccount.global.jwt.JwtAuthenticationFilter;
import com.practice.efubaccount.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /**
     * SecurityFilterChain 설정을 위한 Bean 등록
     * HTTP 요청에 대한 보안 구성을 정의하고, JWT 인증 필터 추가
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 기본 인증 방식 비활성화 (UI 대신 토큰을 통한 인증을 사용하기 때문)
                .httpBasic(AbstractHttpConfigurer::disable)
                // CSRF 보호 비활성화 (토큰 기반 인증이므로 필요하지 않음)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 설정 비활성화
                .cors(AbstractHttpConfigurer::disable)
                // 요청에 따른 인증 인가 설정
                .authorizeHttpRequests(requests -> {
                    /* 액세스토큰 재발급, GET요청은 모두 허용 */
                    requests.requestMatchers("/auth/token").permitAll();
                    requests.requestMatchers(HttpMethod.GET).permitAll();
                    /* 다른 모든 요청은 인증을 요구 */
                    requests.anyRequest().authenticated();
                })
                // JWT를 사용하므로 sateless
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 인증 필터를 UsernamePAsswordAuthenticationFilter 앞에 추가하여 JWT를 통한 인증 수행
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                // OAuth2 로그인 설정 - 인증된 사용자 정보(프로필)를 가져오는 방식 정의, 인증 성공시 동작을 정의하는 successHandler 설정
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler))
                .build();
    }
}
