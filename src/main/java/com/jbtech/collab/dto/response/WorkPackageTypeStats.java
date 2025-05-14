package com.jbtech.collab.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WorkPackageTypeStats {
    private String workPackageType;
    private Long openCount;
    private Long closeCount;
}
