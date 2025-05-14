package com.jbtech.collab.repository;

import com.jbtech.collab.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByEntityIdAndEntityTypeOrderByUpdatedAtDesc(Long entityId, String entityType);
    Page<ActivityLog> findByCreatedBy(String createdBy, Pageable pageable);

}