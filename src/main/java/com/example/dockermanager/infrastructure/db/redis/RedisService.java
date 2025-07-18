package com.example.dockermanager.infrastructure.db.redis;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class RedisService {
    StringRedisTemplate stringRedisTemplate;

    public void saveSession(String sessionId, Long userId) {
        stringRedisTemplate.opsForValue().set("session:" + sessionId, String.valueOf(userId), Duration.ofHours(2));
    }

    public Long getUserIdBySession(String sessionId) {
        String value = stringRedisTemplate.opsForValue().get("session:" + sessionId);
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e) {
            log.info("failed to parse session id: {}", sessionId);
            return null;
        }
    }

    public void deleteSession(String sessionId) {
        stringRedisTemplate.delete("session:" + sessionId);
    }

    public boolean exists(String sessionId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey("session:" + sessionId));
    }
}
