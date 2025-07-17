package com.example.dockermanager.application.auth.service;

import com.example.dockermanager.application.auth.dto.SocialUserInfo;
import com.example.dockermanager.application.auth.dto.session.SessionUser;
import com.example.dockermanager.application.user.service.UserService;
import com.example.dockermanager.application.auth.dto.SocialUserLoginResult;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {
    SocialLoginServiceFactory socialLoginServiceFactory;
    UserService userService;
    HttpSession session;

    public SocialUserLoginResult login(String provider, String code) {
        SocialLoginService socialLoginService = socialLoginServiceFactory.getSocialLoginService(provider);
        SocialUserInfo userInfo = socialLoginService.login(code);
        Long userId = userService.findOrCreateUser(userInfo);

        session.setAttribute("user", new SessionUser(userId, userInfo.getName(), userInfo.getEmail()));

        return SocialUserLoginResult.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .accessToken(userInfo.getAccessToken())
                .build();
    }
}
