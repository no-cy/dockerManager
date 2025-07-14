package com.example.dockermanager.application.docker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateDockerContainerDto {
    @JsonProperty("user_id")
    Long userId;

    String image;

    String tag;

    @JsonProperty("container_name")
    String containerName;

    @JsonProperty("cmds")
    List<String> cmdList;

    @JsonProperty("ports")
    List<PortMappingDto> portMappingDtoList;

    @JsonProperty("scheduled_termination_at")
    LocalDateTime scheduledTerminationAt; // 사용자가 지정한 컨테이너 종료 시간
}
