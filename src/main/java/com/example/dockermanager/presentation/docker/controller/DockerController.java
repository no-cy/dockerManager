package com.example.dockermanager.presentation.docker.controller;

import com.example.dockermanager.application.service.DockerContainerService;
import com.example.dockermanager.common.dto.ResponseDto;
import com.example.dockermanager.application.dto.CreateDockerContainerDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/docker")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class DockerController {

    DockerContainerService containerCreateService;

    @PostMapping("/create")
    public ResponseDto<String> createDockerContainer(@RequestBody CreateDockerContainerDto createDockerContainerDto) {
        return ResponseDto.of(HttpStatus.CREATED,"컨테이너 생성에 성공했습니다.", containerCreateService.createContainer(createDockerContainerDto));
    }


}
