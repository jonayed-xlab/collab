package com.jbtech.collab.repository;

import com.jbtech.collab.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByEntityIdAndEntityTypeOrderByUpdatedAtDesc(Long entityId, String entityType);
    List<ActivityLog> findByCreatedBy(String createdBy);
}