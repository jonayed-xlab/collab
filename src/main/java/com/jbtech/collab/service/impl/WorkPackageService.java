package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.NotificationRequest;
import com.jbtech.collab.dto.request.WorkPackageRequest;
import com.jbtech.collab.dto.response.*;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.User;
import com.jbtech.collab.model.WorkPackage;
import com.jbtech.collab.model.WorkPackageDynamicMapping;
import com.jbtech.collab.repository.ProjectRepository;
import com.jbtech.collab.repository.UserRepository;
import com.jbtech.collab.repository.WorkPackageDynamicMappingRepository;
import com.jbtech.collab.repository.WorkPackageRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.INotificationService;
import com.jbtech.collab.service.IWorkPackageService;
import com.jbtech.collab.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkPackageService extends BaseService implements IWorkPackageService {

    private final UserRepository userRepo;
    private final WorkPackageRepository wpRepository;
    private final WorkPackageDynamicMappingRepository wpdmRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogger auditLogger;
    private final INotificationService notificationService;

    public WorkPackageService(JwtUtil jwtUtil, UserRepository userRepo, WorkPackageRepository wpRepository, WorkPackageDynamicMappingRepository wpdmRepository, ProjectRepository projectRepository, AuditLogger auditLogger, INotificationService notificationService) {
        super(jwtUtil);
        this.userRepo = userRepo;
        this.wpRepository = wpRepository;
        this.wpdmRepository = wpdmRepository;
        this.projectRepository = projectRepository;
        this.auditLogger = auditLogger;
        this.notificationService = notificationService;
    }


    @Override
    @Transactional
    public WorkPackage create(WorkPackageRequest request) {

        projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ApiException("E404", "Project not found"));

        WorkPackage wp = WorkPackage.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .workPackageType(request.getWorkPackageType())
                .assignedTo(request.getAssignedTo())
                .accountableTo(request.getAccountableTo())
                .estimateWork(request.getEstimateWork())
                .remainingWork(request.getRemainingWork())
                .spentWork(request.getSpentWork())
                .storyPoints(request.getStoryPoints())
                .earnedStoryPoints(request.getEarnedStoryPoints())
                .ProjectType(request.getProjectType())
                .startDate(request.getStartDate().atStartOfDay())
                .endDate(request.getEndDate().atStartOfDay())
                .percentageComplete(request.getPercentageComplete())
                .category(request.getCategory())
                .taskType(request.getTaskType())
                .version(request.getVersion())
                .priority(request.getPriority())
                .repositoryName(request.getRepositoryName())
                .branchName(request.getBranchName())
                .status(StatusEnum.NEW)
                .projectId(request.getProjectId())
                .createdBy(getCurrentUser().getName())
                .createdAt(LocalDateTime.now())
                .build();

        wp = wpRepository.save(wp);

        if (Boolean.TRUE.equals(request.getIsParentAvailable())) {

            WorkPackageDynamicMapping wdm = new WorkPackageDynamicMapping();
            wdm.setParentWorkPackageId(request.getParentId());
            wdm.setParentWorkPackageType(request.getParentWorkPackageType());
            wdm.setChildWorkPackageId(wp.getId());
            wdm.setChildWorkPackageType(request.getWorkPackageType());
        }

        auditLogger.logChanges(null,
                wp,
                wp.getCreatedBy(),
                null,
                wp.getWorkPackageType().getValue(),
                wp.getId(),
                wp.getTitle()
        );

        // send notification
        sendNotification(
                wp,
                Objects.isNull(wp.getAssignedTo()) ? Boolean.FALSE : Boolean.TRUE,
                Objects.isNull(wp.getAccountableTo()) ? Boolean.FALSE : Boolean.TRUE
        );

        return wp;
    }

    private void sendNotification(WorkPackage wp, Boolean isAssignedTo, Boolean isAccountableTo) {
        List<NotificationRequest> list = new ArrayList<>();
        if (Boolean.TRUE.equals(isAssignedTo)) {
            list.add(
                    NotificationRequest.builder()
                            .userId(wp.getAssignedTo())
                            .projectId(wp.getProjectId())
                            .workPackageId(wp.getId())
                            .title(wp.getTitle())
                            .message("Assigned to you")
                            .createdBy(wp.getCreatedBy())
                            .build()
            );
        }
        if (Boolean.TRUE.equals(isAccountableTo)) {
            list.add(
                    NotificationRequest.builder()
                            .userId(wp.getAccountableTo())
                            .projectId(wp.getProjectId())
                            .workPackageId(wp.getId())
                            .title(wp.getTitle())
                            .message("Accountable to you")
                            .createdBy(wp.getCreatedBy())
                            .build()
            );
        }

        if (list.isEmpty()) {
            return;
        }

        notificationService.notifyUsers(list);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkPackageResponseWrapper getById(Long id) {

        WorkPackage currentWorkPackage = wpRepository.findById(id)
                .orElseThrow(() -> new ApiException("E404", "WorkPackage not found"));

        List<WorkPackageDynamicMapping> parentMappings = wpdmRepository.findByChildWorkPackageId(id);
        List<WorkPackage> relatedWorkPackages = new ArrayList<>();

        for (WorkPackageDynamicMapping mapping : parentMappings) {
            Long parentId = mapping.getParentWorkPackageId();
            List<WorkPackageDynamicMapping> siblingMappings = wpdmRepository.findByParentWorkPackageId(parentId);
            List<WorkPackage> siblings = siblingMappings.stream()
                    .map(siblingMapping -> wpRepository.findById(siblingMapping.getChildWorkPackageId()).orElse(null))
                    .filter(Objects::nonNull)
                    .filter(wp -> !wp.getId().equals(id))
                    .toList();
            relatedWorkPackages.addAll(siblings);
        }

        relatedWorkPackages = relatedWorkPackages.stream().distinct().collect(Collectors.toList());

        List<WorkPackageDynamicMapping> childMappings = wpdmRepository.findByParentWorkPackageId(id);
        List<WorkPackage> childWorkPackages = childMappings.stream()
                .map(mapping -> wpRepository.findById(mapping.getChildWorkPackageId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return WorkPackageResponseWrapper.builder()
                .workPackage(currentWorkPackage)
                .relatedWorkPackages(relatedWorkPackages)
                .childWorkPackages(childWorkPackages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkPackage> getAll(String title, WorkPackageEnum workPackageType) {

        if ((title == null || title.isEmpty()) && workPackageType == null) {
            return wpRepository.findAll();
        }

        if (title != null && workPackageType != null) {
            return wpRepository.findByTitleContainingIgnoreCaseAndWorkPackageType(title, workPackageType);
        } else if (title != null) {
            return wpRepository.findByTitleContainingIgnoreCase(title);
        } else {
            return wpRepository.findByWorkPackageType(workPackageType);
        }
    }

    @Override
    @Transactional
    public WorkPackage update(Long id, WorkPackageRequest request) {

        WorkPackage wp = wpRepository.findById(id)
                .orElseThrow(() -> new ApiException("E404", "WorkPackage not found"));

        WorkPackage originalCopy = new WorkPackage();
        BeanUtils.copyProperties(wp, originalCopy);

        wp.setTitle(request.getTitle());
        wp.setDescription(request.getDescription());
        wp.setWorkPackageType(request.getWorkPackageType());
        wp.setAssignedTo(request.getAssignedTo());
        wp.setAccountableTo(request.getAccountableTo());
        wp.setEstimateWork(request.getEstimateWork());
        wp.setRemainingWork(request.getRemainingWork());
        wp.setSpentWork(request.getSpentWork());
        wp.setStoryPoints(request.getStoryPoints());
        wp.setEarnedStoryPoints(request.getEarnedStoryPoints());
        wp.setProjectType(request.getProjectType());
        wp.setStartDate(request.getStartDate().atStartOfDay());
        wp.setEndDate(request.getEndDate().atStartOfDay());
        wp.setPercentageComplete(request.getPercentageComplete());
        wp.setCategory(request.getCategory());
        wp.setTaskType(request.getTaskType());
        wp.setVersion(request.getVersion());
        wp.setPriority(request.getPriority());
        wp.setRepositoryName(request.getRepositoryName());
        wp.setBranchName(request.getBranchName());
        wp.setStatus(request.getStatus());
        wp.setProjectId(request.getProjectId());

        WorkPackage updatedWorkPackage = wpRepository.save(wp);

        auditLogger.logChanges(originalCopy,
                updatedWorkPackage,
                null,
                updatedWorkPackage.getUpdatedBy(),
                updatedWorkPackage.getWorkPackageType().getValue(),
                updatedWorkPackage.getId(),
                updatedWorkPackage.getTitle()
        );

        // send notification
        sendNotification(
                updatedWorkPackage,
                (Objects.isNull(wp.getAssignedTo()) || wp.getAssignedTo().equals(updatedWorkPackage.getAssignedTo())) ? Boolean.FALSE : Boolean.TRUE,
                (Objects.isNull(wp.getAccountableTo()) || wp.getAccountableTo().equals(updatedWorkPackage.getAccountableTo())) ? Boolean.FALSE : Boolean.TRUE
        );

        return updatedWorkPackage;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        WorkPackage wp = wpRepository.findById(id)
                .orElseThrow(() -> new ApiException("E404", "WorkPackage not found"));

        List<WorkPackageDynamicMapping> parentMappings =
                wpdmRepository.findByParentWorkPackageId(wp.getId());
        if (!parentMappings.isEmpty()) {
            wpdmRepository.deleteAll(parentMappings);
        }

        List<WorkPackageDynamicMapping> childMappings =
                wpdmRepository.findByChildWorkPackageId(wp.getId());
        if (!childMappings.isEmpty()) {
            wpdmRepository.deleteAll(childMappings);
        }

        wpRepository.delete(wp);
    }

    @Override
    public List<WorkPackageResponseWrapper> getProjectWorkPackages(Long projectId) {

        List<WorkPackage> storyWorkPackages = wpRepository.findByProjectIdAndWorkPackageType(projectId, WorkPackageEnum.STORY);

        if (storyWorkPackages.isEmpty()) {
            return Collections.emptyList();
        }

        return storyWorkPackages.stream().map(currentWorkPackage -> {
            List<WorkPackageDynamicMapping> childMappings = wpdmRepository.findByParentWorkPackageId(currentWorkPackage.getId());

            List<WorkPackage> childWorkPackages = childMappings.stream()
                    .map(mapping -> wpRepository.findById(mapping.getChildWorkPackageId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return WorkPackageResponseWrapper.builder()
                    .workPackage(currentWorkPackage)
                    .relatedWorkPackages(null)
                    .childWorkPackages(childWorkPackages)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoadMapResponse> getRoadmap(Long projectId) {

        List<WorkPackage> workPackages = wpRepository.findByProjectId(projectId);

        // Group work packages by sprint/version
        Map<String, List<WorkPackage>> sprintMap = workPackages.stream()
                .collect(Collectors.groupingBy(WorkPackage::getVersion));

        List<RoadMapResponse> roadMapResponses = new ArrayList<>();

        for (Map.Entry<String, List<WorkPackage>> entry : sprintMap.entrySet()) {

            String sprintName = entry.getKey();
            List<WorkPackage> sprintWPs = entry.getValue();

            int totalCount = sprintWPs.size();
            int openCount = (int) sprintWPs.stream()
                    .filter(wp -> wp.getStatus() != StatusEnum.CLOSED)
                    .count();
            int closedCount = totalCount - openCount;

            int openCountPercentage = (int) ((openCount / (double) totalCount) * 100);
            int closedCountPercentage = 100 - openCountPercentage;

            int progressPercentage = (int) sprintWPs.stream()
                    .filter(wp -> wp.getPercentageComplete() != null)
                    .mapToInt(WorkPackage::getPercentageComplete)
                    .average()
                    .orElse(0);

            LocalDateTime startDate = sprintWPs.stream()
                    .filter(wp -> wp.getStartDate() != null)
                    .map(WorkPackage::getStartDate)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime endDate = sprintWPs.stream()
                    .filter(wp -> wp.getEndDate() != null)
                    .map(WorkPackage::getEndDate)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            roadMapResponses.add(
                    RoadMapResponse.builder()
                            .sprintName(sprintName)
                            .startDate(startDate != null ? startDate.toLocalDate().toString() : null)
                            .endDate(endDate != null ? endDate.toLocalDate().toString() : null)
                            .progressPercentage(progressPercentage)
                            .openCount(openCount)
                            .openCountPercentage(openCountPercentage)
                            .closedCount(closedCount)
                            .closedCountPercentage(closedCountPercentage)
                            .build()
            );
        }

        return roadMapResponses;
    }

    @Override
    public List<WorkPackage> getProjectWorkPackagesByUser(Long userId) {
        return wpRepository.findByAssignedTo(userId);
    }

    @Override
    public WorkPackageDashboardResponse getWorkPackageStats(Long projectId) {

        List<WorkPackage> workPackages = wpRepository.findByProjectId(projectId);

        // 1. Status Counts
        List<StatusCount> statusCounts = workPackages.stream()
                .collect(Collectors.groupingBy(
                        wp -> wp.getStatus().name(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    return StatusCount.builder()
                            .status(entry.getKey())
                            .count(entry.getValue())
                            .build();
                })
                .collect(Collectors.toList());

        // 2. Work by User (Assigned To)
        Map<Long, String> userMap = userRepo.findAll().stream()
                .collect(Collectors.toMap(User::getId, User::getName));

        List<UserWorkCount> userWorkCounts = workPackages.stream()
                .filter(wp -> wp.getAssignedTo() != null)
                .collect(Collectors.groupingBy(
                        WorkPackage::getAssignedTo,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    return UserWorkCount.builder()
                            .username(userMap.getOrDefault(entry.getKey(), "Unknown"))
                            .count(entry.getValue())
                            .build();
                })
                .collect(Collectors.toList());

        // 3. WorkPackageType Open/Closed Counts
        List<WorkPackageTypeStats> workPackageTypeStats = workPackages.stream()
                .collect(Collectors.groupingBy(
                        WorkPackage::getWorkPackageType,
                        Collectors.groupingBy(WorkPackage::getStatus, Collectors.counting())
                ))
                .entrySet().stream()
                .map(entry -> {
                    WorkPackageEnum type = entry.getKey();
                    Map<StatusEnum, Long> statusMap = entry.getValue();

                    Long openCount = statusMap.entrySet().stream()
                            .filter(e -> e.getKey() != StatusEnum.CLOSED)
                            .mapToLong(Map.Entry::getValue)
                            .sum();

                    return WorkPackageTypeStats.builder()
                            .workPackageType(type.name())
                            .openCount(openCount)
                            .closeCount(statusMap.getOrDefault(StatusEnum.CLOSED, 0L))
                            .build();
                })
                .collect(Collectors.toList());

        // 4. Category Counts
        List<CategoryCount> categoryCounts = workPackages.stream()
                .filter(wp -> wp.getCategory() != null)
                .collect(Collectors.groupingBy(
                        WorkPackage::getCategory,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    return CategoryCount.builder()
                            .category(entry.getKey())
                            .count(entry.getValue())
                            .build();
                })
                .collect(Collectors.toList());

        // 5. Priority Stats (Open/Closed)
        List<PriorityStats> priorityStats = workPackages.stream()
                .filter(wp -> wp.getPriority() != null)
                .collect(Collectors.groupingBy(
                        WorkPackage::getPriority,
                        Collectors.groupingBy(WorkPackage::getStatus, Collectors.counting())
                ))
                .entrySet().stream()
                .map(entry -> {
                    PriorityEnum priority = entry.getKey();
                    Map<StatusEnum, Long> statusMap = entry.getValue();
                    Long openCount = statusMap.entrySet().stream()
                            .filter(e -> e.getKey() != StatusEnum.CLOSED)
                            .mapToLong(Map.Entry::getValue)
                            .sum();

                    return PriorityStats.builder()
                            .priority(priority.name())
                            .openCount(openCount)
                            .closeCount(statusMap.getOrDefault(StatusEnum.CLOSED, 0L))
                            .build();
                })
                .collect(Collectors.toList());

        return WorkPackageDashboardResponse.builder()
                .statusCounts(statusCounts)
                .userWorkCounts(userWorkCounts)
                .workPackageTypeStats(workPackageTypeStats)
                .categoryCounts(categoryCounts)
                .priorityStats(priorityStats)
                .build();
    }
}
