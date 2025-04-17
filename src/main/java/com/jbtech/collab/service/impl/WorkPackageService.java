package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.WorkPackageRequest;
import com.jbtech.collab.dto.response.WorkPackageResponseWrapper;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.WorkPackage;
import com.jbtech.collab.model.WorkPackageDynamicMapping;
import com.jbtech.collab.repository.ProjectRepository;
import com.jbtech.collab.repository.WorkPackageDynamicMappingRepository;
import com.jbtech.collab.repository.WorkPackageRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IWorkPackageService;
import com.jbtech.collab.utils.AuditLogger;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkPackageService extends BaseService implements IWorkPackageService {

    private final WorkPackageRepository wpRepository;
    private final WorkPackageDynamicMappingRepository wpdmRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogger auditLogger;

    public WorkPackageService(JwtUtil jwtUtil, WorkPackageRepository wpRepository, WorkPackageDynamicMappingRepository wpdmRepository, ProjectRepository projectRepository, AuditLogger auditLogger) {
        super(jwtUtil);
        this.wpRepository = wpRepository;
        this.wpdmRepository = wpdmRepository;
        this.projectRepository = projectRepository;
        this.auditLogger = auditLogger;
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
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .percentageComplete(request.getPercentageComplete())
                .category(request.getCategory())
                .taskType(request.getTaskType())
                .version(request.getVersion())
                .priority(request.getPriority())
                .repositoryName(request.getRepositoryName())
                .branchName(request.getBranchName())
                .status(request.getStatus())
                .projectId(request.getProjectId())
                .createdBy(getCurrentUser())
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

        /*
        auditLogger.logChanges(null,
                wp,
                wp.getCreatedBy(),
                null,
                wp.getWorkPackageType().getValue(),
                wp.getId()
        );
        */

        return wp;
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
    public List<WorkPackage> getAll() {
        return wpRepository.findAll();
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
        wp.setStartDate(request.getStartDate());
        wp.setEndDate(request.getEndDate());
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
                updatedWorkPackage.getId()
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
    public List<WorkPackage> getProjectWorkPackages(Long projectId) {
        return wpRepository.findByProjectId(projectId);
    }
}
