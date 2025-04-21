package com.jbtech.collab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jbtech.collab.utils.PriorityEnum;
import com.jbtech.collab.utils.StatusEnum;
import com.jbtech.collab.utils.WorkPackageEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WorkPackage extends BaseModel {
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
}
