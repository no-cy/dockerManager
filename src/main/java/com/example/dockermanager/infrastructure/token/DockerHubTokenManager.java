package com.example.dockermanager.infrastructure.token;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class DockerHubTokenManager {
    String jwtToken;
    long expiresAtMillis;

    public boolean isETokenValid() {
        return jwtToken != null && System.currentTimeMillis() < expiresAtMillis;
    }

    public void storeToken(String jwtToken, long expiresInSeconds) {
        this.jwtToken = jwtToken;
        this.expiresAtMillis = System.currentTimeMillis() + (expiresInSeconds * 1000);
    }

    public String getToken() {
        return jwtToken;
    }
}
