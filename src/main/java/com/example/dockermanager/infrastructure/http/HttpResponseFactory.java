package com.example.dockermanager.infrastructure.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HttpResponseFactory {

    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    // Map 형태로 응답 처리
    public Map<String, Object> exchangeForMap(String url, HttpMethod method, HttpEntity<?> request) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, method, request, new ParameterizedTypeReference<>() {});

        validateResponse(response, url);
        return response.getBody();
    }

    // 객체 타입 응답 처리 (ex. GitLabUserResponse)
    public <T> T exchangeForObject(String url, HttpMethod method, HttpEntity<?> request, Class<T> clazz) {
        ResponseEntity<T> response = restTemplate.exchange(url, method, request, clazz);
        validateResponse(response, url);
        return response.getBody();
    }

    // 특정 키 추출
    public Object extractField(Map<String, Object> body, String key, String context) {
        if (!body.containsKey(key)) {
            throw new IllegalStateException(context + ": 응답에 '" + key + "' 필드가 없습니다.");
        }
        return body.get(key);
    }

    // 원하는 타입으로 변환
    public <T> T convert(Object source, TypeReference<T> typeRef) {
        return objectMapper.convertValue(source, typeRef);
    }

    // POST 요청 처리
    public Map<String, Object> postForMap(String url, HttpEntity<?> request) {
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        validateResponse(response, url);
        return response.getBody();
    }

    // 공통 응답 체크
    private void validateResponse(ResponseEntity<?> response, String context) {
        if(response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new IllegalStateException(context + " 호출 실패: " + response.getStatusCode());
        }
    }
}
