package com.example.dockermanager.infrastructure.oauth;

import com.example.dockermanager.application.auth.dto.SocialUserInfo;
import com.example.dockermanager.application.auth.service.SocialLoginService;
import com.example.dockermanager.infrastructure.http.HttpRequestFactory;
import com.example.dockermanager.infrastructure.http.HttpResponseFactory;
import com.example.dockermanager.infrastructure.oauth.dto.GitLabUserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitLabLoginService implements SocialLoginService {

    final HttpRequestFactory httpRequestFactory;
    final HttpResponseFactory httpResponseFactory;
    
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

        HttpHeaders headers = httpRequestFactory.formUrlEncodedHeaders();
        HttpEntity<MultiValueMap<String, String>> tokenRequest = httpRequestFactory.withBody(params, headers);

        Map<String, Object> body = httpResponseFactory.postForMap(tokenUri, tokenRequest);

        String accessToken = (String) httpResponseFactory.extractField(body, "access_token", tokenUri);

        // 2. 사용자 정보 요청
        HttpHeaders userHeaders = httpRequestFactory.bearerHeaders(accessToken);
        HttpEntity<Void> userRequest = httpRequestFactory.withoutBody(userHeaders);
        // HttpEntity<Object> userRequest = new HttpEntity<>(userHeaders);

        GitLabUserResponse user = httpResponseFactory.exchangeForObject(userInfoUri, HttpMethod.GET, userRequest, GitLabUserResponse.class);

        return SocialUserInfo.builder()
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .build();
    }
}
