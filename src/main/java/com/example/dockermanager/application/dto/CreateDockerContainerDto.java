package com.example.dockermanager.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateDockerContainerDto {
    String image;

    String tag;

    String containerName;

    @JsonProperty("cmds")
    List<String> cmdList;

    @JsonProperty("ports")
    List<PortMappingDto> portMappingDtoList;

    Integer ttl;
}
