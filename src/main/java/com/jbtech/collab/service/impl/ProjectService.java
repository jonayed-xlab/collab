package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.AssignProjectRequest;
import com.jbtech.collab.dto.request.ProjectRequest;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.Project;
import com.jbtech.collab.model.UserProjectMapping;
import com.jbtech.collab.repository.ProjectRepository;
import com.jbtech.collab.repository.UserProjectRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IProjectService;
import com.jbtech.collab.utils.JwtUtil;
import com.jbtech.collab.utils.ProjectStatusEnum;
import com.jbtech.collab.utils.UserRoleEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectService extends BaseService implements IProjectService {

    private final ProjectRepository projectRepo;
    private final UserProjectRepository userProjectRepo;

    public ProjectService(JwtUtil jwtUtil, ProjectRepository projectRepo, UserProjectRepository userProjectRepo) {
        super(jwtUtil);
        this.projectRepo = projectRepo;
        this.userProjectRepo = userProjectRepo;
    }

    @Override
    public Project create(ProjectRequest request) {

        Optional<Project> existing = projectRepo.findByName(request.getName());

        if (existing.isPresent()) {
            throw new ApiException("E409", "Project already exists");
        }

        Project newProject = new Project();
        newProject.setName(request.getName());
        newProject.setDescription(request.getDescription());
        newProject.setStatus(ProjectStatusEnum.NEW);

        return projectRepo.save(newProject);
    }

    @Override
    public Project get(Long id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new ApiException("E404", "Project not found"));
    }

    @Override
    public List<Project> getAll(String name) {

        if (!Objects.equals(UserRoleEnum.PROJECT_MANAGER, getCurrentUser().getRole())
                && !Objects.equals(UserRoleEnum.ADMIN, getCurrentUser().getRole())) {
            throw new ApiException("E401", "Access denied");
        }

        if (name == null || name.trim().isEmpty()) {
            return projectRepo.findAll();
        }

        return projectRepo.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public Project update(Long id, Project request) {
        Project existing = get(id);
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());
        return projectRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        projectRepo.deleteById(id);
    }

    @Override
    public List<Project> getProjectByUserId(Long userId) {
        List<Project> projects = new ArrayList<>();
        List<UserProjectMapping> mappings = userProjectRepo.findByUserId(userId);
        mappings.forEach(
                mapping -> {
                    projects.add(get(mapping.getProjectId()));
                }
        );
        return projects;
    }

    @Override
    @Transactional
    public void assignProject(AssignProjectRequest request) {
        UserProjectMapping mapping = new UserProjectMapping();
        mapping.setUserId(request.getUserId());
        mapping.setProjectId(request.getProjectId());
        userProjectRepo.save(mapping);
    }
}
