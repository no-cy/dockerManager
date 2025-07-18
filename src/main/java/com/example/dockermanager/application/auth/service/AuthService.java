package com.example.dockermanager.application.auth.service;

import com.example.dockermanager.application.auth.dto.SocialUserInfo;
import com.example.dockermanager.application.user.service.UserService;
import com.example.dockermanager.application.auth.dto.SocialUserLoginResult;
import com.example.dockermanager.infrastructure.db.redis.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {
    SocialLoginServiceFactory socialLoginServiceFactory;
    UserService userService;
    RedisService redisService;

    public SocialUserLoginResult login(String provider, String code) {
        SocialLoginService socialLoginService = socialLoginServiceFactory.getSocialLoginService(provider);
        SocialUserInfo userInfo = socialLoginService.login(code);
        Long userId = userService.findOrCreateUser(userInfo);

        String sessionId = UUID.randomUUID().toString();
        redisService.saveSession(sessionId, userId);

        return SocialUserLoginResult.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .accessToken(userInfo.getAccessToken())
                .sessionId((sessionId))
                .build();
    }
}
