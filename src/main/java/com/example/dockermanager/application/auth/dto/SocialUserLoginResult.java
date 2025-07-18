package com.example.dockermanager.application.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialUserLoginResult {
    String accessToken;
    String refreshToken;
    String name;
    String email;
    String nickname;
    String image;
    String socialLoginInfo;
    String sessionId;
    boolean joinStatus;
}
