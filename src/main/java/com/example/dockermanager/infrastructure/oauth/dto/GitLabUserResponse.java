package com.example.dockermanager.infrastructure.oauth.dto;

import lombok.Data;

@Data
public class GitLabUserResponse {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String avatar_url;
}
