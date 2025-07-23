package com.example.dockermanager.infrastructure.dockmgrcore.enums;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum ContainerMethodType {
    CREATE("/v1/container/create"),
    STOP("/v1/container/stop"),
    START("/v1/container/start");

    private final String path;

    public String getPath() {
        return path;
    }
}
