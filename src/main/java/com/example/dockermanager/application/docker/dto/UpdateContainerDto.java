package com.example.dockermanager.application.docker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateContainerDto {
    @JsonProperty("id")
    String containerId;

    @JsonProperty("container_name")
    String containerName;

    @JsonProperty("scheduled_termination_at")
    LocalDateTime scheduledTerminationAt; // 사용자가 지정한 컨테이너 종료 시간
}
