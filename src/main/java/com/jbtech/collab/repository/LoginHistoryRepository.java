package com.jbtech.collab.repository;

import com.jbtech.collab.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    Optional<LoginHistory> findTopByUserIdOrderByLoginTimeDesc(Long userId);
}
