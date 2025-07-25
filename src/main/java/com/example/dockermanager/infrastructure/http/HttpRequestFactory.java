package com.example.dockermanager.infrastructure.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestFactory {

    public HttpHeaders formUrlEncodedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders bearerHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }

    public HttpHeaders jwtHeaders(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "JWT " + jwtToken);
        return headers;
    }

    public HttpHeaders basicAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        return headers;
    }

    public <T> HttpEntity<T> withBody(T body, HttpHeaders headers) {
        return new HttpEntity<>(body, headers);
    }

    public HttpEntity<Void> withoutBody(HttpHeaders headers) {
        return new HttpEntity<>(headers);
    }
}
