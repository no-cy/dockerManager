package com.example.dockermanager.infrastructure.http;

import com.example.dockermanager.application.docker.dto.GoModuleRequestDto;
import com.example.dockermanager.configuration.DockMgrHttpConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoModuleClient {

    private final DockMgrHttpConfig dockMgrHttpConfig;
    private final RestTemplate restTemplate;

    public String sendContainerRequest(GoModuleRequestDto requestDto) {
        String url = dockMgrHttpConfig.getDockmgrCoreUrl();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestDto, String.class);
        return responseEntity.getBody();
    }
}
