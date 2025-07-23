package com.example.dockermanager.infrastructure.dockmgrcore;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoModuleClient {

    RestTemplate restTemplate;

    public <T, R> R sendRequest(String url, T requestDto, Class<R> responseType) {
        ResponseEntity<R> responseEntity = restTemplate.postForEntity(url, requestDto, responseType);
        return responseEntity.getBody();
    }
}
