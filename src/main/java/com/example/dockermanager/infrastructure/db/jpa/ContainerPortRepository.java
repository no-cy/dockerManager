package com.example.dockermanager.infrastructure.db.jpa;

import com.example.dockermanager.domain.container.entity.ContainerPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContainerPortRepository extends JpaRepository<ContainerPort, Integer> {
    @Query("SELECT cp.hostPort FROM ContainerPort cp WHERE cp.containerPort IN :hostPorts")
    List<String> findAllByContainerPortIn(@Param("hostPorts") List<String> hostPorts);
}
