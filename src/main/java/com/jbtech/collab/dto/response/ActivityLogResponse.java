package com.jbtech.collab.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityLogResponse {
    private Long entityId;
    private String entityName;
    private String entityType;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;
    private String fieldName;
    private String oldValue;
    private String newValue;
}
