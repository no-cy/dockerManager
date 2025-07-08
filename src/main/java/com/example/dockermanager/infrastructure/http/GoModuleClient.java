package com.example.dockermanager.infrastructure.http;

import com.example.dockermanager.application.dto.CreateDockerContainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoModuleClient {

    private final RestTemplate restTemplate;

    public String sendContainerRequest(CreateDockerContainerDto dto) {
        String url = "http://<ip:port>/create";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, dto, String.class);
        return responseEntity.getBody();
    }
}
