package com.example.dockermanager.application.docker.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContainerStatusUpdateDto {
    String cmd;

    // Jackson이 단일 필드를 매핑하기 위해서는 @JsonCreator 어노테이션 필요
    // 또한, @Setter, @NoArgsConstructor 를 사용해도 문제없음
    @JsonCreator
    public ContainerStatusUpdateDto(@JsonProperty("cmd") String cmd) {
        this.cmd = cmd;
    }
}
