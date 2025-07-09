package com.example.dockermanager.application.auth.service;

import com.example.dockermanager.presentation.auth.dto.response.UserLoginDto;

public interface SocialLoginService {
    UserLoginDto login(String code);
}
