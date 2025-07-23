package com.example.dockermanager.infrastructure.dockmgrcore.enums;

import com.example.dockermanager.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ContainerCommand {
    START("exited", ContainerMethodType.START, "해당 컨테이너가 실행 중이거나 존재하지 않습니다."),
    STOP("running", ContainerMethodType.STOP, "해당 컨테이너가 실행 중이 아닙니다.");

    String requiredStatus;
    ContainerMethodType methodType;
    String errorMessage;

    public static ContainerCommand from(String input) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("올바르지 않은 명령어입니다. 다시 시도해 주세요"));
    }

}
