package com.jbtech.collab.resource;


import com.jbtech.collab.dto.request.WorkPackageRequest;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.dto.response.WorkPackageResponseWrapper;
import com.jbtech.collab.model.WorkPackage;
import com.jbtech.collab.service.IWorkPackageService;
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
    public ApiResponse<List<WorkPackage>> getAll() {
        return ApiResponse.success(
                workPackageService.getAll()
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

    @GetMapping("/{projectId}/packages")
    public ApiResponse<List<WorkPackage>> getWorkPackages(@PathVariable Long projectId) {
        return ApiResponse.success(workPackageService.getProjectWorkPackages(projectId));
    }
}
