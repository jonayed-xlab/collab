package com.jbtech.collab.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusCount {
    private String status;
    private Long count;
}
