package com.example.dockermanager.application.service;

import com.example.dockermanager.application.docker.dto.*;
import com.example.dockermanager.common.exception.DataAlreadyExistsException;
import com.example.dockermanager.common.exception.NotFoundException;
import com.example.dockermanager.common.util.docker.ContainerTimeUtils;
import com.example.dockermanager.domain.container.entity.Container;
import com.example.dockermanager.infrastructure.container.ContainerMapper;
import com.example.dockermanager.infrastructure.db.jpa.ContainerPortRepository;
import com.example.dockermanager.infrastructure.db.jpa.ContainerRepository;
import com.example.dockermanager.infrastructure.dockmgrcore.GoModuleBuilder;
import com.example.dockermanager.infrastructure.dockmgrcore.GoModuleClient;
import com.example.dockermanager.infrastructure.dockmgrcore.enums.ContainerCommand;
import com.example.dockermanager.infrastructure.dockmgrcore.enums.ContainerMethodType;
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
    private final GoModuleBuilder goModuleBuilder;

    public String createContainer(Long userId, CreateDockerContainerDto dto) {

        if (containerRepository.existsByContainerName(dto.getContainerName())) {
            throw new DataAlreadyExistsException("이미 " + dto.getContainerName() + " 컨테이너가 생성되어 있습니다.");
        }

        List<String> requestedHostPorts = dto.getPortMappingDtoList().stream()
                .map(PortMappingDto::getHostPort)
                .toList();

        for (String requestedHostPort : requestedHostPorts) {
            if (containerPortRepository.existsByContainerPort(requestedHostPort)) {
                throw new DataAlreadyExistsException("이미 다음 포트가 할당되어 있습니다: " + requestedHostPort);
            }
        }

        Long ttl = ContainerTimeUtils.calculateInSeconds(dto.getScheduledTerminationAt());
        GoContainerCreateRequestDto requestDto = GoContainerCreateRequestDto.from(dto, userId, ttl);
        String url = goModuleBuilder.getUrl(ContainerMethodType.CREATE);

        return goModuleClient.sendRequest(url, requestDto, String.class);
    }

    public List<ContainerResponseDto> getContainersByUserId(Long userId) {
        List<Container> containers = containerRepository.findByUser_UserId(userId);
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

    public String changeContainerStatus(Long userId, ContainerStatusUpdateDto dto) {
        String cmd = dto.getCmd();
        String containerId = dto.getContainerId();

        // status 값이 stop(exited)일 경우, 해당 컨테이너가 running 상태인지 확인.
        // start 일 경우, 해당 컨테이너가 stop 중인지 확인.
        ContainerCommand containerCommand = ContainerCommand.from(cmd);
        Boolean result = containerRepository.existsByContainerIdAndStatus(containerId, containerCommand.getRequiredStatus());
        if (!result) {
            throw new NotFoundException(containerCommand.getErrorMessage());
        }

        String url = goModuleBuilder.getUrl(containerCommand.getMethodType());

        GoContainerUpdateStatusRequestDto requestDto = GoContainerUpdateStatusRequestDto.from(dto);

        // dockmgr-core 에게 데이터 전달.
        return goModuleClient.sendRequest(url, requestDto, String.class);
    }
}
