package com.jbtech.collab.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PriorityStats {
    private String priority;
    private Long openCount;
    private Long closeCount;
}
