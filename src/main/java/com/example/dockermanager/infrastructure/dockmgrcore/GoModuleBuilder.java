package com.example.dockermanager.infrastructure.dockmgrcore;

import com.example.dockermanager.configuration.DockmgrCoreConfig;
import com.example.dockermanager.infrastructure.dockmgrcore.enums.ContainerMethodType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoModuleBuilder {

    DockmgrCoreConfig dockmgrCoreConfig;

    public String getUrl(ContainerMethodType type) {
        return String.format("%s://%s:%s%s",
                dockmgrCoreConfig.getProtocol(),
                dockmgrCoreConfig.getHost(),
                dockmgrCoreConfig.getPort(),
                type.getPath());
    }
}
