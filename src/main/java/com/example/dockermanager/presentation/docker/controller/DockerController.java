package com.example.dockermanager.presentation.docker.controller;

import com.example.dockermanager.application.docker.dto.ContainerResponseDto;
import com.example.dockermanager.application.docker.dto.ContainerStatusUpdateDto;
import com.example.dockermanager.application.docker.dto.UpdateContainerDto;
import com.example.dockermanager.application.service.DockerContainerService;
import com.example.dockermanager.common.dto.ResponseDto;
import com.example.dockermanager.application.docker.dto.CreateDockerContainerDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/docker")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class DockerController {

    DockerContainerService containerCreateService;

    @PostMapping("/create")
    public ResponseDto<String> createDockerContainer(@RequestAttribute("userId") Long userId, @RequestBody CreateDockerContainerDto createDockerContainerDto) {
        return ResponseDto.of(HttpStatus.CREATED,"컨테이너 생성에 성공했습니다.", containerCreateService.createContainer(userId, createDockerContainerDto));
    }

    @GetMapping("/containers")
    public ResponseDto<List<ContainerResponseDto>> getContainers(@RequestAttribute("userId") Long userId) {
        return ResponseDto.of(HttpStatus.OK, "컨테이너 조회에 성공하였습니다.", containerCreateService.getContainersByUserId(userId));
    }

    @PatchMapping("/containers")
    public ResponseDto<String> updateContainerInfo(@RequestAttribute("userId") Long userId, @RequestBody UpdateContainerDto updateContainerDto) {
        return ResponseDto.of(HttpStatus.OK, "컨테이너 정보를 수정하는데 성공하였습니다.", containerCreateService.updateContainer(userId, updateContainerDto));
    }

    @PostMapping("/containers/status")
    public ResponseDto<String> updateContainerStatus(@RequestAttribute("userId") Long userId, @RequestBody ContainerStatusUpdateDto containerStatusUpdateDto) {
        return ResponseDto.of(HttpStatus.OK, "컨테이너 정보를 수정하는데 성공하였습니다.", containerCreateService.changeContainerStatus(userId, containerStatusUpdateDto));
    }
}
