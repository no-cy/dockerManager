package com.example.dockermanager.application.dockerhub.service;

import com.example.dockermanager.configuration.DockerHubConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DockerHubService {
    DockerHubConfig dockerHubConfig;
    ObjectMapper objectMapper;
    RestTemplate restTemplate;

    public List<String> getPrivateRepositories() {
        String username = dockerHubConfig.getUsername();
        String pat = dockerHubConfig.getToken();
        String jwtToken = loginToDockerHub(username, pat);

        String url = "https://hub.docker.com/v2/repositories/" + dockerHubConfig.getUsername() + "/?page_size=100";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "JWT " + jwtToken);
        //headers.setBasicAuth(dockerHubConfig.getUsername(), dockerHubConfig.getToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("Docker API 호출 실패: " + response.getStatusCode());
        }

        Object rawResults = response.getBody().get("results");

        List<Map<String, Object>> results = objectMapper.convertValue(
                rawResults,
                new TypeReference<>() {}
        );

        ArrayList<String> repoNameList = new ArrayList<>();
        for (Map<String, Object> repo : results) {
            repoNameList.add((String) repo.get("name"));
        }

        return repoNameList;
    }

    private String loginToDockerHub(String username, String pat) {
        String loginUrl = "https://hub.docker.com/v2/users/login";

        Map<String, String> body = Map.of(
                "username", username,
                "password", pat
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, request, Map.class);

        if (response.getStatusCode() != HttpStatus.OK || !response.getBody().containsKey("token")) {
            throw new IllegalStateException("DockerHub 로그인 실패: " + response.getStatusCode());
        }

        return (String) response.getBody().get("token");
    }
}
