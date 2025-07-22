package com.example.dockermanager.infrastructure.container;


import com.example.dockermanager.application.docker.dto.UpdateContainerDto;
import com.example.dockermanager.domain.container.entity.Container;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContainerMapper {
    void updateContainerFromDto(UpdateContainerDto updateContainerDto, @MappingTarget Container container);
}
