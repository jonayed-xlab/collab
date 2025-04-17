package com.jbtech.collab.repository;

import com.jbtech.collab.model.UserProjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserProjectRepository extends JpaRepository<UserProjectMapping, Long> {
    List<UserProjectMapping> findByUserId(Long userId);
}