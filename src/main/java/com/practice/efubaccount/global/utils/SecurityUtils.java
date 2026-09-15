package com.practice.efubaccount.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    /**
     * 현재 인증된 사용자 email 반환 메서드
     * 만약 인증 정보가 없는 경우, null 반환
     */

    //TODO: 현재 인증된 사용자 email을 반환하는 메서드 작성.
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null) {
            return null;
        }
        return authentication.getName(); // 식별자 값을 가져온다는 의미의 getName임
    }

}

