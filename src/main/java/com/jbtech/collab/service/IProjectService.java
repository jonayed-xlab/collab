package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.AssignProjectRequest;
import com.jbtech.collab.dto.request.ProjectRequest;
import com.jbtech.collab.dto.response.ProjectResponse;
import com.jbtech.collab.model.Project;
import com.jbtech.collab.model.User;

import java.util.List;

public interface IProjectService {
    Project create(ProjectRequest request);
    Project get(Long id);
    List<ProjectResponse> getAll(String name);
    Project update(Long id, Project request);
    void delete(Long id);
    List<ProjectResponse> getProjectByUserId(Long userId);
    void assignProject(AssignProjectRequest request);
    List<User> getUserByProjectId(Long projectId);
    void deleteAssignUser(Long projectId, Long userId);
}
