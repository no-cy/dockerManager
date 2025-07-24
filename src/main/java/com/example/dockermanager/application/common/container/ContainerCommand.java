package com.example.dockermanager.application.common.container;

import com.example.dockermanager.common.exception.NotFoundException;
import com.example.dockermanager.infrastructure.dockmgrcore.enums.ContainerMethodType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ContainerCommand {
    START("exited", "컨테이너가 성공적으로 시작되었습니다.", ContainerMethodType.START, "해당 컨테이너가 실행 중이거나 존재하지 않습니다."),
    STOP("running", "컨테이너가 성공적으로 중지되었습니다.", ContainerMethodType.STOP, "해당 컨테이너가 실행 중이 아닙니다."),
    DELETE("exited", "컨테이너가 성공적으로 삭제되었습니다.",ContainerMethodType.DELETE, "해당 컨테이너가 실행 중입니다. 컨테이너를 중지한 후에 다시 시도해 주세요.");

    String requiredStatus;
    String successMessage;
    ContainerMethodType methodType;
    String errorMessage;

    public static ContainerCommand from(String input) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("올바르지 않은 명령어입니다. 다시 시도해 주세요"));
    }

}
