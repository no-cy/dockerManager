package com.example.dockermanager.application.dockerhub.service;

import com.example.dockermanager.configuration.DockerHubConfig;
import com.example.dockermanager.infrastructure.http.HttpRequestFactory;
import com.example.dockermanager.infrastructure.http.HttpResponseFactory;
import com.example.dockermanager.infrastructure.token.DockerHubTokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    HttpRequestFactory httpRequestFactory;
    HttpResponseFactory httpResponseFactory;

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

        HttpHeaders headers = httpRequestFactory.jwtHeaders(jwtToken);

        HttpEntity<Void> request = httpRequestFactory.withoutBody(headers);

        Map<String, Object> body = httpResponseFactory.exchangeForMap(url, HttpMethod.GET, request);
        Object rawResults = httpResponseFactory.extractField(body, "results", url);
        List<Map<String, Object>> results = objectMapper.convertValue(rawResults, new TypeReference<>() {});

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

        HttpHeaders headers = httpRequestFactory.jwtHeaders(jwtToken);

        HttpEntity<Void> request = httpRequestFactory.withoutBody(headers);

        Map<String, Object> body = httpResponseFactory.exchangeForMap(url, HttpMethod.GET, request);
        Object rawResults = httpResponseFactory.extractField(body, "results", url);
        List<Map<String, Object>> results = objectMapper.convertValue(rawResults, new TypeReference<>() {});

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

        HttpHeaders headers = httpRequestFactory.jsonHeaders();
        HttpEntity<Map<String, String>> request = httpRequestFactory.withBody(body, headers);

        Map<String, Object> response = httpResponseFactory.postForMap(loginUrl, request);
        return (String) httpResponseFactory.extractField(response, "token", loginUrl);
    }
}
