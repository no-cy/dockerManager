package com.example.dockermanager.infrastructure.db.jpa;

import com.example.dockermanager.domain.container.entity.ContainerPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface ContainerPortRepository extends JpaRepository<ContainerPort, Integer> {
    Boolean existsByContainerPort(@Param("hostPort") String hostPort);
}
