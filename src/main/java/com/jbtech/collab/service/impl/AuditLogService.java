package com.jbtech.collab.service.impl;

import com.jbtech.collab.model.ActivityLog;
import com.jbtech.collab.repository.ActivityLogRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IAuditLogService;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService extends BaseService implements IAuditLogService {
    private final ActivityLogRepository activityLogRepository;

    public AuditLogService(JwtUtil jwtUtil, ActivityLogRepository activityLogRepository) {
        super(jwtUtil);
        this.activityLogRepository = activityLogRepository;
    }

    public List<ActivityLog> getLogsByEntityIdAndType(Long entityId, String entityType) {
        List<ActivityLog> logs = activityLogRepository.findByEntityIdAndEntityTypeOrderByUpdatedAtDesc(entityId, entityType);
        if (logs.isEmpty()) return null;
        return logs;
    }
}
