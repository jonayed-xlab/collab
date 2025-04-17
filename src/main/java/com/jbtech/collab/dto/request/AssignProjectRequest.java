package com.jbtech.collab.dto.request;

import lombok.Data;

@Data
public class AssignProjectRequest {
    private Long projectId;
    private Long userId;
}
