package com.example.dockermanager.application.service;

import com.example.dockermanager.application.docker.dto.ContainerStatusUpdateDto;
import com.example.dockermanager.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DockerContainerServiceTest {

    @Autowired
    DockerContainerService dockerContainerService;

    @DisplayName("실행 중인 컨테이너를 기동하려고 하면 NotFoundException 발생")
    @Test
    public void 실행중인_컨테이너를_기동할_경우() {
        // containerId, status 값
        ContainerStatusUpdateDto dto = new ContainerStatusUpdateDto(
                "38bf79a2b8d37970ca47dfc3f716f9d4c764feee739a00440ab8e5f0e9dbf6ed",
                "start");
        Long userId = 5L;

        var actual = assertThrows(NotFoundException.class, () -> dockerContainerService.changeContainerStatus(userId, dto));

        // NotFoundException 발생
        assertThat(actual.getMessage()).isEqualTo("해당 컨테이너가 실행 중이거나 존재하지 않습니다.");
    }

}