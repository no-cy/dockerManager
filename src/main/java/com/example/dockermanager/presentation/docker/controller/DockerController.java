package com.example.dockermanager.presentation.docker.controller;

import com.example.dockermanager.application.auth.dto.session.SessionUser;
import com.example.dockermanager.application.docker.dto.ContainerResponseDto;
import com.example.dockermanager.application.service.DockerContainerService;
import com.example.dockermanager.common.dto.ResponseDto;
import com.example.dockermanager.application.docker.dto.CreateDockerContainerDto;
import jakarta.servlet.http.HttpSession;
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
    public ResponseDto<String> createDockerContainer(@RequestBody CreateDockerContainerDto createDockerContainerDto) {
        return ResponseDto.of(HttpStatus.CREATED,"컨테이너 생성에 성공했습니다.", containerCreateService.createContainer(createDockerContainerDto));
    }

    @GetMapping("/containers")
    public ResponseDto<List<ContainerResponseDto>> getContainers(HttpSession session) {

        SessionUser user = (SessionUser) session.getAttribute("user");
        Long userId = user.getId();

        log.info("userId: {}, 사용자: {}", userId, user.getName());
        return ResponseDto.of(HttpStatus.OK, "컨테이너 조회에 성공하였습니다.", containerCreateService.getContainersByUserId(userId));
    }

}
