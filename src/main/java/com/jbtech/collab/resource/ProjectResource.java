package com.jbtech.collab.resource;

import com.jbtech.collab.dto.request.AssignProjectRequest;
import com.jbtech.collab.dto.request.ProjectRequest;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.model.Project;
import com.jbtech.collab.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/project")
@RequiredArgsConstructor
public class ProjectResource {

    private final IProjectService projectService;

    @PostMapping
    public ApiResponse<Project> create(@RequestBody ProjectRequest project) {
        return ApiResponse.success(
                projectService.create(project)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<Project> get(@PathVariable Long id) {
        return ApiResponse.success(
                projectService.get(id)
        );
    }

    @GetMapping
    public ApiResponse<List<Project>> getAll(@RequestParam (required = false) String name) {
        return ApiResponse.success(
                projectService.getAll(name)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<Project> update(@PathVariable Long id, @RequestBody Project project) {
        return ApiResponse.success(
                projectService.update(id, project)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/assign")
    public ApiResponse<Void> assign(@RequestBody AssignProjectRequest request) {
        projectService.assignProject(request);
        return ApiResponse.success(null);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Project>> getProject(@PathVariable("userId") Long userId) {
        return ApiResponse.success(
                projectService.getProjectByUserId(userId)
        );
    }
}
