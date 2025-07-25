package com.example.dockermanager.presentation.dockerhub;

import com.example.dockermanager.application.dockerhub.service.DockerHubService;
import com.example.dockermanager.common.dto.ResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dockerhub")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DockerHubController {
    DockerHubService dockerHubService;

    @GetMapping("/repos")
    public ResponseDto<Map<String, List<String>>> getPrivateRepos() {
        return ResponseDto.of(HttpStatus.OK, "Docker Hub에서 이미지를 조회하는데 성공하였습니다.", dockerHubService.getPrivateRepositories());
    }
}
