package com.jbtech.collab.dto.request;

import com.jbtech.collab.utils.PriorityEnum;
import com.jbtech.collab.utils.StatusEnum;
import com.jbtech.collab.utils.WorkPackageEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkPackageRequest {
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private WorkPackageEnum workPackageType;

    private Long assignedTo;
    private Long accountableTo;

    private String estimateWork;
    private String remainingWork;
    private String spentWork;

    private String storyPoints;
    private String earnedStoryPoints;

    private String ProjectType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer percentageComplete;
    private String category;
    private String taskType;
    private String version;
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    private String repositoryName;
    private String branchName;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private Long projectId;
    private Boolean isParentAvailable = Boolean.FALSE;
    private Long parentId;
    @Enumerated(EnumType.STRING)
    private WorkPackageEnum parentWorkPackageType;
}
