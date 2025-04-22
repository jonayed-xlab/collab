package com.jbtech.collab.resource;

import com.jbtech.collab.dto.request.WorkPackageRequest;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.dto.response.RoadMapResponse;
import com.jbtech.collab.dto.response.WorkPackageDashboardResponse;
import com.jbtech.collab.dto.response.WorkPackageResponseWrapper;
import com.jbtech.collab.model.WorkPackage;
import com.jbtech.collab.service.IWorkPackageService;
import com.jbtech.collab.utils.WorkPackageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/work-package")
@RequiredArgsConstructor
public class WorkPackageResource {

    private final IWorkPackageService workPackageService;

    @PostMapping
    public ApiResponse<WorkPackage> create(@RequestBody WorkPackageRequest request) {
        return ApiResponse.success(
                workPackageService.create(request)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkPackageResponseWrapper> get(@PathVariable Long id) {
        return ApiResponse.success(
                workPackageService.getById(id)
        );
    }

    @GetMapping
    public ApiResponse<List<WorkPackage>> getAll(@RequestParam(required = false) String title,
                                                  @RequestParam(required = false) WorkPackageEnum workPackageType) {
        return ApiResponse.success(
                workPackageService.getAll(title, workPackageType)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkPackage> update(@PathVariable Long id, @RequestBody WorkPackageRequest request) {
        return ApiResponse.success(
                workPackageService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        workPackageService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/{projectId}/project")
    public ApiResponse<List<WorkPackageResponseWrapper>> getWorkPackages(@PathVariable Long projectId) {
        return ApiResponse.success(
                workPackageService.getProjectWorkPackages(projectId)
        );
    }

    @GetMapping("/{userId}/user")
    public ApiResponse<List<WorkPackage>> getWorkPackagesByUser(@PathVariable Long userId) {
        return ApiResponse.success(
                workPackageService.getProjectWorkPackagesByUser(userId)
        );
    }

    @GetMapping("/roadmap/{projectId}")
    public ApiResponse<List<RoadMapResponse>> getRoadmap(@PathVariable Long projectId) {
        return ApiResponse.success(
                workPackageService.getRoadmap(projectId)
        );
    }

    @GetMapping("/dashboard/{projectId}")
    public ApiResponse<WorkPackageDashboardResponse> getWorkPackageStats(Long projectId) {
        return ApiResponse.success(
                workPackageService.getWorkPackageStats(projectId)
        );
    }
}
