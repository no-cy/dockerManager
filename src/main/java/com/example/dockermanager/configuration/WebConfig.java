package com.example.dockermanager.configuration;

import com.example.dockermanager.configuration.interceptor.SessionValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SessionValidationInterceptor sessionValidationInterceptor;

    public WebConfig(SessionValidationInterceptor sessionValidationInterceptor) {
        this.sessionValidationInterceptor = sessionValidationInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://128.10.30.240:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor)
                .addPathPatterns("/v1/docker/**")
                .excludePathPatterns("/v1/auth/**", "/api/dockerhub/**");
    }
}
