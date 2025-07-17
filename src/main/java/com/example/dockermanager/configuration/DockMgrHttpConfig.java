package com.example.dockermanager.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dockmgrcore")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DockMgrHttpConfig {
    String dockmgrCoreUrl;
}
