package com.example.dockermanager.application.auth.service;

import com.example.dockermanager.application.auth.dto.SocialUserInfo;

public interface SocialLoginService {
    SocialUserInfo login(String code);
}
