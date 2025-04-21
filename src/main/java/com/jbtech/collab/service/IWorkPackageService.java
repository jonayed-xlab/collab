package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.WorkPackageRequest;
import com.jbtech.collab.dto.response.RoadMapResponse;
import com.jbtech.collab.dto.response.WorkPackageDashboardResponse;
import com.jbtech.collab.dto.response.WorkPackageResponseWrapper;
import com.jbtech.collab.model.WorkPackage;

import java.util.List;

public interface IWorkPackageService {
    WorkPackage create(WorkPackageRequest request);
    WorkPackageResponseWrapper getById(Long id);
    List<WorkPackage> getAll();
    WorkPackage update(Long id, WorkPackageRequest request);
    void delete(Long id);
    List<WorkPackage> getProjectWorkPackages(Long projectId);
    List<RoadMapResponse> getRoadmap(Long projectId);
    List<WorkPackage> getProjectWorkPackagesByUser(Long userId);
    WorkPackageDashboardResponse getWorkPackageStats(Long projectId);
}
