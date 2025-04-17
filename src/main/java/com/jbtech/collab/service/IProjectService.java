package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.AssignProjectRequest;
import com.jbtech.collab.dto.request.ProjectRequest;
import com.jbtech.collab.model.Project;

import java.util.List;

public interface IProjectService {
    Project create(ProjectRequest request);
    Project get(Long id);
    List<Project> getAll();
    Project update(Long id, Project request);
    void delete(Long id);
    List<Project> getProjectByUserId(Long userId);
    void assignProject(AssignProjectRequest request);
}
