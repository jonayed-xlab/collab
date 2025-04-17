package com.jbtech.collab.service;

import com.jbtech.collab.model.ActivityLog;

import java.util.List;

public interface IAuditLogService {
    List<ActivityLog> getLogsByEntityIdAndType(Long entityId, String entityType);
}
