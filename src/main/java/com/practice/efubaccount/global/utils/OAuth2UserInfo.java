package com.practice.efubaccount.global.utils;

import java.util.Map;

public class OAuth2UserInfo {
    private Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    //TODO: 이름 반환
    public String getNickname() {
        return (String) attributes.get("name");
    }

    //TODO: 이메일 반환
    public String getEmail() {
        return (String) attributes.get("email");
    }
}

