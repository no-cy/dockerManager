package com.example.dockermanager.application.dockerhub.service;

import com.example.dockermanager.configuration.DockerHubConfig;
import com.example.dockermanager.infrastructure.token.DockerHubTokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DockerHubService {
    DockerHubConfig dockerHubConfig;
    ObjectMapper objectMapper;
    RestTemplate restTemplate;
    DockerHubTokenManager tokenManager;

    public Map<String, List<String>> getPrivateRepositories() {
        String username = dockerHubConfig.getUsername();
        String jwtToken;

        if (tokenManager.isETokenValid()) {
            jwtToken = tokenManager.getToken();
        } else {
            String pat = dockerHubConfig.getToken();
            jwtToken = loginToDockerHub(username, pat);
            tokenManager.storeToken(jwtToken, 18000); // 토큰 5시간 유효
        }

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

        HashMap<String, List<String>> repoMap = new HashMap<>();

        // name과 tag 목록을 가져옴
        for (Map<String, Object> repo : results) {
            String repoName = repo.get("name").toString();
            log.info("repoName: {}", repoName);
            // 해당 repo의 tag 목록을 가져옴
            List<String> repoTagList = getRepoTags(repoName, jwtToken);
            log.info("repoTagList: {}", repoTagList);
            repoMap.put(repoName, repoTagList);
        }

        return repoMap;
    }

    public List<String> getRepoTags(String repoName, String jwtToken) {
        String url = "https://hub.docker.com/v2/repositories/" + dockerHubConfig.getUsername() + "/" + repoName + "/tags?page_size=100";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "JWT " + jwtToken);

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

        ArrayList<String> tagList = new ArrayList<>();

        // 해당 repo의 tag 목록을 가져옴
        for (Map<String, Object> tag : results) {
            tagList.add((String) tag.get("name"));
        }

        return tagList;
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
