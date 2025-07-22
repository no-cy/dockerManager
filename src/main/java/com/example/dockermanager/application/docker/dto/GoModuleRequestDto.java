package com.example.dockermanager.application.docker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoModuleRequestDto {
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

    Long ttl;

    public static GoModuleRequestDto from(CreateDockerContainerDto dto, Long userId, Long ttl) {
        return new GoModuleRequestDto(
                userId,
                dto.getImage(),
                dto.getTag(),
                dto.getContainerName(),
                dto.getCmdList(),
                dto.getPortMappingDtoList(),
                ttl
        );
    }
}
