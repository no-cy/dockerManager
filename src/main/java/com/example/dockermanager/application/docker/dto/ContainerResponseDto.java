package com.example.dockermanager.application.docker.dto;

import com.example.dockermanager.domain.container.entity.Container;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ContainerResponseDto {

    String id;
    String image;
    String tag;
    Long ttl;
    LocalDateTime created_at;
    String container_name;
    LocalDateTime deleted_at;
    String status;
    LocalDateTime last_check_time;

    public static ContainerResponseDto from(Container container) {
        return new ContainerResponseDto(
                container.getContainerId(),
                container.getImage(),
                container.getTag(),
                container.getTtl(),
                container.getCreatedAt(),
                container.getContainerName(),
                container.getDeletedAt(),
                container.getStatus(),
                container.getLastCheckTime()
        );
    }
}
