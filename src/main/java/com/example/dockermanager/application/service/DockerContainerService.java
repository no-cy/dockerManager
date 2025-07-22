package com.example.dockermanager.application.service;

import com.example.dockermanager.application.docker.dto.*;
import com.example.dockermanager.common.exception.DataAlreadyExistsException;
import com.example.dockermanager.common.exception.NotFoundException;
import com.example.dockermanager.common.util.docker.ContainerTimeUtils;
import com.example.dockermanager.domain.container.entity.Container;
import com.example.dockermanager.infrastructure.container.ContainerMapper;
import com.example.dockermanager.infrastructure.db.jpa.ContainerPortRepository;
import com.example.dockermanager.infrastructure.db.jpa.ContainerRepository;
import com.example.dockermanager.infrastructure.http.GoModuleClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerContainerService {

    private final GoModuleClient goModuleClient;
    private final ContainerRepository containerRepository;
    private final ContainerPortRepository containerPortRepository;
    private final ContainerMapper containerMapper;

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

        Long ttl = ContainerTimeUtils.calculateInSeconds(dto.getScheduledTerminationAt());
        GoModuleRequestDto requestDto = GoModuleRequestDto.from(dto, ttl);

        return goModuleClient.sendContainerRequest(requestDto);
    }

    public List<ContainerResponseDto> getContainersByUserId(Long userId) {
        List<Container> containers = containerRepository.findByUserId(userId);
        return containers.stream()
                .map(ContainerResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public String updateContainer(Long userId, UpdateContainerDto dto) {
        String containerId = dto.getContainerId();
        Optional<Container> containerOpt = containerRepository.findByUserIdAndContainerIdAndStatusRunning(userId, containerId);
        Long ttl = ContainerTimeUtils.calculateInSeconds(dto.getScheduledTerminationAt());

        if (containerOpt.isPresent()) {
            Container container = containerOpt.get();
            containerMapper.updateContainerFromDto(dto, container);
            container.applyTtl(ttl);
        } else {
            throw new NotFoundException("해당 컨테이너를 찾지 못하였습니다.");
        }

        return "컨테이너를 수정하는데 성공하였습니다.";
    }
}
