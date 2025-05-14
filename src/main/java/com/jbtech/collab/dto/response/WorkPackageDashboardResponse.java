package com.jbtech.collab.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkPackageDashboardResponse {
    private List<StatusCount> statusCounts;
    private List<UserWorkCount> userWorkCounts;
    private List<WorkPackageTypeStats> workPackageTypeStats;
//    private List<CategoryCount> categoryCounts;
    private List<PriorityStats> priorityStats;
}

