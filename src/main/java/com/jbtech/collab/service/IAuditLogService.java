package com.jbtech.collab.service;

import com.jbtech.collab.dto.response.ActivityLogResponse;
import com.jbtech.collab.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAuditLogService {
    List<ActivityLog> getLogsByEntityIdAndType(Long entityId, String entityType);
    Page<ActivityLogResponse> getAllLogs(Pageable pageable);
}
