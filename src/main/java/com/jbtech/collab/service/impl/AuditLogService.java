package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.response.ActivityLogResponse;
import com.jbtech.collab.model.ActivityLog;
import com.jbtech.collab.repository.ActivityLogRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IAuditLogService;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuditLogService extends BaseService implements IAuditLogService {
    private final ActivityLogRepository activityLogRepo;

    public AuditLogService(JwtUtil jwtUtil, ActivityLogRepository activityLogRepo) {
        super(jwtUtil);
        this.activityLogRepo = activityLogRepo;
    }

    public List<ActivityLog> getLogsByEntityIdAndType(Long entityId, String entityType) {
        List<ActivityLog> logs = activityLogRepo.findByEntityIdAndEntityTypeOrderByUpdatedAtDesc(entityId, entityType);
        if (logs.isEmpty()) return null;
        return logs;
    }

    @Override
    public List<ActivityLogResponse> getAllLogs() {

        List<ActivityLog> logs = activityLogRepo.findByCreatedBy(getCurrentUser().getName());

        List<ActivityLogResponse> responses = new ArrayList<>();
        logs.forEach(e -> {

            ActivityLogResponse response = new ActivityLogResponse();

            response.setEntityId(e.getEntityId());
            response.setName(Objects.isNull(e.getUpdatedBy()) ? e.getCreatedBy() : e.getUpdatedBy());
            response.setEntityType(e.getEntityType());
            response.setFieldName(e.getFieldName());
            response.setOldValue(e.getOldValue());
            response.setNewValue(e.getNewValue());
            response.setUpdatedAt(Objects.isNull(e.getUpdatedAt()) ? e.getCreatedAt() : e.getUpdatedAt());
            response.setEntityName(null);

            responses.add(response);
        });

        return responses;
    }
}
