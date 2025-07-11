package com.example.dockermanager.infrastructure.oauth;

import com.example.dockermanager.application.auth.dto.SocialUserInfo;
import com.example.dockermanager.application.auth.service.SocialLoginService;
import com.example.dockermanager.infrastructure.oauth.dto.GitLabUserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitLabLoginService implements SocialLoginService {

    final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.gitlab.client-id}")
    String clientId;

    @Value("${oauth.gitlab.client-secret}")
    String clientSecret;

    @Value("${oauth.gitlab.redirect-uri}")
    String redirectUri;

    @Value("${oauth.gitlab.token-uri}")
    String tokenUri;

    @Value("${oauth.gitlab.user-info-uri}")
    String userInfoUri;

    @Override
    public SocialUserInfo login(String code) {
        // 1. Access Token 요청
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                tokenRequest,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. 사용자 정보 요청
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        HttpEntity<Object> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<GitLabUserResponse> userResponse = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                userRequest,
                GitLabUserResponse.class
        );

        GitLabUserResponse user = userResponse.getBody();

        return SocialUserInfo.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
