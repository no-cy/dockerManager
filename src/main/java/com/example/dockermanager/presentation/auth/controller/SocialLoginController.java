package com.example.dockermanager.presentation.auth.controller;

import com.example.dockermanager.application.auth.service.AuthService;
import com.example.dockermanager.application.auth.dto.SocialUserLoginResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SocialLoginController {

    final AuthService authService;

    @Value("${auth.redirect-uri}")
    String loginRedirectUri;

    @GetMapping("/{provider}/callback")
    public RedirectView socialLogin(@PathVariable String provider, @RequestParam String code) {
        SocialUserLoginResult loginResult = authService.login(provider, code);

        String redirectUrl = UriComponentsBuilder
                .fromUriString(loginRedirectUri)
                .queryParam("token", loginResult.getAccessToken())
                .queryParam("username", URLEncoder.encode(loginResult.getName(), StandardCharsets.UTF_8))
                .queryParam("email", loginResult.getEmail())
                .build(false).
                toUriString();

        return new RedirectView(redirectUrl);
    }
}
