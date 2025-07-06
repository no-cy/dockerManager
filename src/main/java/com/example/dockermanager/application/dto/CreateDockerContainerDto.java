package com.example.dockermanager.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateDockerContainerDto {
    String image;
    String tag;
    String containerName;
    List<String> cmd;
    List<PortMappingDto> portMappingDto;
    Integer ttl;
}
