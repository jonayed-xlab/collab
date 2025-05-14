package com.jbtech.collab.repository;

import com.jbtech.collab.model.UserProjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserProjectRepository extends JpaRepository<UserProjectMapping, Long> {
    List<UserProjectMapping> findByUserId(Long userId);

    List<UserProjectMapping> findByProjectId(Long projectId);

    Optional<UserProjectMapping> findByProjectIdAndUserId(Long projectId, Long userId);
}