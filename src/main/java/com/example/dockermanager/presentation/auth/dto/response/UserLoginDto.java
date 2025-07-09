package com.example.dockermanager.presentation.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDto {
    String accessToken;
    String refreshToken;
    String name;
    String email;
    String nickname;
    String image;
    String socialLoginInfo;
    boolean joinStatus;
}
