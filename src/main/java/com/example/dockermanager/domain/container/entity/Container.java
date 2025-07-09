package com.example.dockermanager.domain.container.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "containers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @Column
    String image;

    @Column
    String tag;

    @Column
    long ttl;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "container_name")
    String containerName;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column
    String status;

    @Column(name = "last_check_time")
    LocalDateTime lastCheckTime;
}
