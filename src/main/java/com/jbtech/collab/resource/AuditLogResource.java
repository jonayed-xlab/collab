package com.jbtech.collab.resource;

import com.jbtech.collab.dto.response.ActivityLogResponse;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.model.ActivityLog;
import com.jbtech.collab.service.IAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/logs")
@RequiredArgsConstructor
public class AuditLogResource {

    private final IAuditLogService auditLogService;

    @GetMapping
    public ApiResponse<List<ActivityLog>> getLogs(
            @RequestParam Long entityId,
            @RequestParam String entityType) {

        List<ActivityLog> logs = auditLogService.getLogsByEntityIdAndType(entityId, entityType);
        return ApiResponse.success(logs);
    }

    @GetMapping("/all")
    public ApiResponse<Page<ActivityLogResponse>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ActivityLogResponse> logs = auditLogService.getAllLogs(PageRequest.of(page, size));
        return ApiResponse.success(logs);
    }
}
