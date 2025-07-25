package com.example.dockermanager.configuration;

import com.example.dockermanager.infrastructure.http.HttpRequestFactory;
import com.example.dockermanager.infrastructure.http.HttpResponseFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {

    @Bean
    public HttpRequestFactory httpRequestFactory() {
        return new HttpRequestFactory();
    }

    @Bean
    public HttpResponseFactory httpResponseFactory(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new HttpResponseFactory(restTemplate, objectMapper);
    }
}
