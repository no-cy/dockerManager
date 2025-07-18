package com.example.dockermanager.configuration.interceptor;

import com.example.dockermanager.infrastructure.db.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class SessionValidationInterceptor implements HandlerInterceptor {
   RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = request.getHeader("X-Session-Id");
        log.info("sessionId={}", sessionId);
        if (sessionId == null || !redisService.exists(sessionId)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing session ID");
            return false;
        }

        Long userId = redisService.getUserIdBySession(sessionId);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing user ID");
            return false;
        }

        request.setAttribute("userId", userId);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
