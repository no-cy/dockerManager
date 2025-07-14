package com.example.dockermanager.common.util.docker;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class ContainerTimeUtils {
    private ContainerTimeUtils() {}

    public static long calculateInSeconds(LocalDateTime scheduledTerminationAt) {
        if (scheduledTerminationAt == null) return -1;
        Duration duration = Duration.between(LocalDateTime.now(), scheduledTerminationAt);
        return Math.max(duration.getSeconds(), 0);
    }
}
