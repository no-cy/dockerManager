package com.example.dockermanager.application.docker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContainerStatusUpdateDto {
    @JsonProperty("container_id")
    String containerId;
    String cmd;
}
