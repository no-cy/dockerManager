package com.example.dockermanager.application.service;

import com.example.dockermanager.application.dto.CreateDockerContainerDto;
import com.example.dockermanager.infrastructure.http.GoModuleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerContainerService {

    private final GoModuleClient goModuleClient;

    public String createContainer(CreateDockerContainerDto dto) {
        return goModuleClient.sendContainerRequest(dto);
    }
}
