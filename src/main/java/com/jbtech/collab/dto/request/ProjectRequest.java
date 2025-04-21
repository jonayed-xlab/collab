package com.jbtech.collab.dto.request;

import com.jbtech.collab.utils.ProjectStatusEnum;
import lombok.Data;

@Data
public class ProjectRequest {
    private String name;
    private String description;
    private ProjectStatusEnum status;
}
