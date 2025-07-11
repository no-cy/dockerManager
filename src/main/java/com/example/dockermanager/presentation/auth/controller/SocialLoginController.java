package com.example.dockermanager.presentation.auth.controller;

import com.example.dockermanager.application.auth.service.AuthService;
import com.example.dockermanager.common.ResponseCode;
import com.example.dockermanager.common.dto.ResponseDto;
import com.example.dockermanager.presentation.auth.dto.response.UserLoginDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SocialLoginController {

    private final AuthService authService;

    @GetMapping("/{provider}/callback")
    public ResponseDto<UserLoginDto>socialLogin(@PathVariable String provider, @RequestParam String code) {
        UserLoginDto dto = authService.login(provider, code);
        return ResponseDto.of(ResponseCode.LOGIN_SUCCESS.getHttpStatus(), ResponseCode.LOGIN_SUCCESS.getMessage(), dto);
    }
}
