package com.example.snapcampus.common.login;

import lombok.Data;

@Data
public class TokenInfo {
    private String userId;
    private String grantType;
    private String accessToken;
    private String refreshToken;

    // 생성자
    public TokenInfo(String userId, String grantType, String accessToken, String refreshToken) {
        this.userId = userId;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
