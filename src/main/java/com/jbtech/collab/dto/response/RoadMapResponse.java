package com.jbtech.collab.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadMapResponse {
    private String sprintName;
    private String startDate;
    private String endDate;
    private Integer progressPercentage;
    private Integer openCount;
    private Integer openCountPercentage;
    private Integer closedCount;
    private Integer closedCountPercentage;
}
