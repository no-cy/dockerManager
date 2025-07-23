package com.example.dockermanager.application.docker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoContainerUpdateStatusRequestDto {
    @JsonProperty("container_id")
    String containerId;

    public static GoContainerUpdateStatusRequestDto from(ContainerStatusUpdateDto dto) {
        return new GoContainerUpdateStatusRequestDto(dto.getContainerId());
    }
}
