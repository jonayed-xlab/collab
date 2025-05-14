package com.jbtech.collab.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jbtech.collab.model.BaseModel;
import com.jbtech.collab.utils.PriorityEnum;
import com.jbtech.collab.utils.StatusEnum;
import com.jbtech.collab.utils.WorkPackageEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkPackageResponse{
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private WorkPackageEnum workPackageType;

    private Long assignedTo;
    private String assignedToName;
    private Long accountableTo;
    private String accountableToName;

    private String estimateWork;
    private String remainingWork;
    private String spentWork;

    private String storyPoints;
    private String earnedStoryPoints;

    private String ProjectType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
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
    private String projectName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;
    private String updatedBy;

}

