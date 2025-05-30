package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.WorkPackageRequest;
import com.jbtech.collab.dto.response.RoadMapResponse;
import com.jbtech.collab.dto.response.WorkPackageDashboardResponse;
import com.jbtech.collab.dto.response.WorkPackageResponseWrapper;
import com.jbtech.collab.model.WorkPackage;
import com.jbtech.collab.utils.WorkPackageEnum;

import java.util.List;

public interface IWorkPackageService {
    WorkPackage create(WorkPackageRequest request);
    WorkPackageResponseWrapper getById(Long id);
    List<WorkPackage> getAll(String title, WorkPackageEnum workPackageType);
    WorkPackage update(Long id, WorkPackageRequest request);
    void delete(Long id);
    List<WorkPackageResponseWrapper> getProjectWorkPackages(Long projectId);
    List<WorkPackage> getProjectWorkPackagesByUser(Long userId);
    List<RoadMapResponse> getRoadmap(Long projectId);
    WorkPackageDashboardResponse getWorkPackageStats(Long projectId);
}
