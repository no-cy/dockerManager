package com.example.dockermanager.application.service;

import com.example.dockermanager.application.docker.dto.ContainerResponseDto;
import com.example.dockermanager.application.docker.dto.CreateDockerContainerDto;
import com.example.dockermanager.application.docker.dto.PortMappingDto;
import com.example.dockermanager.common.exception.DataAlreadyExistsException;
import com.example.dockermanager.domain.container.entity.Container;
import com.example.dockermanager.infrastructure.db.jpa.ContainerPortRepository;
import com.example.dockermanager.infrastructure.db.jpa.ContainerRepository;
import com.example.dockermanager.infrastructure.http.GoModuleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DockerContainerService {

    private final GoModuleClient goModuleClient;
    private final ContainerRepository containerRepository;
    private final ContainerPortRepository containerPortRepository;

    public String createContainer(CreateDockerContainerDto dto) {

        if (containerRepository.existsByContainerName(dto.getContainerName())) {
            throw new DataAlreadyExistsException("이미 " + dto.getContainerName() + " 컨테이너가 생성되어 있습니다.");
        }

        List<String> requestedHostPorts = dto.getPortMappingDtoList().stream()
                .map(PortMappingDto::getHostPort)
                .toList();

        List<String> duplicatePorts = containerPortRepository.findAllByContainerPortIn(requestedHostPorts);
        if (!duplicatePorts.isEmpty()) {
            String ports = String.join(", ", duplicatePorts);
            throw new DataAlreadyExistsException("이미 다음 포트가 할당되어 있습니다: " + ports);
        }

        return goModuleClient.sendContainerRequest(dto);
    }

    public List<ContainerResponseDto> getContainersByUserId(Long userId) {
        List<Container> containers = containerRepository.findByUserId(userId);
        return containers.stream()
                .map(ContainerResponseDto::from)
                .collect(Collectors.toList());
    }
}
