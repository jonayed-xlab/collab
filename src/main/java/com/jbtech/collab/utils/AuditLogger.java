package com.jbtech.collab.utils;

import com.jbtech.collab.model.ActivityLog;
import com.jbtech.collab.repository.ActivityLogRepository;
import com.jbtech.collab.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AuditLogger extends BaseService {

    private final ActivityLogRepository activityLogRepo;

    public AuditLogger(JwtUtil jwtUtil, ActivityLogRepository activityLogRepo) {
        super(jwtUtil);
        this.activityLogRepo = activityLogRepo;
    }

    public void logChanges(Object oldObj, Object newObj, String createdBy, String updatedBy, String entityType, Long entityId, String entityName) {

        List<ActivityLog> logs = new ArrayList<>();

        Field[] fields = (oldObj != null ? oldObj.getClass().getDeclaredFields() : newObj.getClass().getDeclaredFields());

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object oldValue = Objects.nonNull(oldObj) ? field.get(oldObj) : null;
                Object newValue = field.get(newObj);

                if (Objects.isNull(oldValue) && Objects.isNull(newValue)) continue;

                if (Objects.isNull(oldValue) || !oldValue.equals(newValue)) {

                    ActivityLog log = new ActivityLog();

                    log.setEntityId(entityId);
                    log.setEntityType(entityType);
                    log.setEntityName(entityName);
                    log.setCreatedBy(createdBy);
                    log.setCreatedAt(LocalDateTime.now());
                    log.setFieldName(field.getName());
                    log.setOldValue(oldValue != null ? oldValue.toString() : "");
                    log.setNewValue(newValue != null ? newValue.toString() : "");
                    log.setUpdatedBy(updatedBy);
                    log.setUpdatedAt(LocalDateTime.now());

                    logs.add(log);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        activityLogRepo.saveAll(logs);
    }
}
