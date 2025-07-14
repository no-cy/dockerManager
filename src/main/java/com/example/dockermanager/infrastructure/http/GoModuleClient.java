package com.example.dockermanager.infrastructure.http;

import com.example.dockermanager.application.docker.dto.GoModuleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoModuleClient {

    private final RestTemplate restTemplate;

    public String sendContainerRequest(GoModuleRequestDto requestDto) {
        String url = "http://<ip:port>/create";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestDto, String.class);
        return responseEntity.getBody();
    }
}
