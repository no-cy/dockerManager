package com.example.dockermanager.domain.container.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "container_ports")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContainerPort {

    @Id
    @Column
    long id;

    @Column(name = "container_id")
    String containerId;

    @Column(name = "container_port")
    String containerPort;

    @Column(name = "host_port")
    String hostPort;
}
