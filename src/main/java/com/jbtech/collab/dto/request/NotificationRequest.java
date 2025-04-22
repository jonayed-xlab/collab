package com.jbtech.collab.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private Long projectId;
    private Long workPackageId;
    private String title;
    private String message;
    private Boolean read = false;
    private LocalDateTime timestamp;
    private String createdBy;
}
