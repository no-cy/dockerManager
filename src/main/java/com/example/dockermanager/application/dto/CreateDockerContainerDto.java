package com.example.dockermanager.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateDockerContainerDto {
    String image;
    String tag;
    String containerName;
    List<String> cmdList;
    List<PortMappingDto> portMappingDtoList;
    Integer ttl;
}
