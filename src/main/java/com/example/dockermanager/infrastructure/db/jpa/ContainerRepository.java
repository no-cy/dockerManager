package com.example.dockermanager.infrastructure.db.jpa;

import com.example.dockermanager.domain.container.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ContainerRepository extends JpaRepository<Container, String> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Container c WHERE c.containerName = :name AND c.status <> 'deleted' and c.status IS NOT NULL")
    Boolean existsByContainerName(@Param("name") String name);

//    @Query("SELECT c FROM Container c WHERE c.userId = :userId")
//    List<Container> findByUserId(Long userId);

    List<Container> findByUser_UserId(Long userId); //

    @Query("SELECT c FROM Container c where c.user.userId = :userId AND c.containerId = :containerId AND c.status = 'running'")
    Optional<Container> findByUserIdAndContainerIdAndStatusRunning(Long userId, String containerId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Container c WHERE c.user.userId = :userId AND c.containerId = :containerId AND c.status = :status")
    Boolean existsByUserIdAndContainerIdAndStatus(Long userId, String containerId, String status);
}
