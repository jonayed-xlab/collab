package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.AssignProjectRequest;
import com.jbtech.collab.dto.request.ProjectRequest;
import com.jbtech.collab.dto.response.ProjectResponse;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.Project;
import com.jbtech.collab.model.User;
import com.jbtech.collab.model.UserProjectMapping;
import com.jbtech.collab.repository.ProjectRepository;
import com.jbtech.collab.repository.UserProjectRepository;
import com.jbtech.collab.repository.UserRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IProjectService;
import com.jbtech.collab.utils.JwtUtil;
import com.jbtech.collab.utils.ProjectStatusEnum;
import com.jbtech.collab.utils.UserRoleEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectService extends BaseService implements IProjectService {

    private final ProjectRepository projectRepo;
    private final UserProjectRepository userProjectRepo;
    private final UserRepository userRepo;

    public ProjectService(JwtUtil jwtUtil, ProjectRepository projectRepo, UserProjectRepository userProjectRepo, UserRepository userRepo) {
        super(jwtUtil);
        this.projectRepo = projectRepo;
        this.userProjectRepo = userProjectRepo;
        this.userRepo = userRepo;
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
    public List<ProjectResponse> getAll(String name) {

        List<ProjectResponse> projectResponses = new ArrayList<>();

//        if (!Objects.equals(UserRoleEnum.PROJECT_MANAGER, getCurrentUser().getRole())
//                && !Objects.equals(UserRoleEnum.ADMIN, getCurrentUser().getRole())) {
//            throw new ApiException("E401", "Access denied");
//        }
//
//        if (name == null || name.trim().isEmpty()) {
//            return projectRepo.findAll();
//        }

//        List<Project> projects = projectRepo.findByNameContainingIgnoreCase(name.trim());

        List<Project> projects = projectRepo.findAll();
        getProjectReponse(projects, projectResponses);
        return projectResponses;
    }

    private void getProjectReponse(List<Project> projects, List<ProjectResponse> projectResponses) {
        projects.forEach(
                project -> {
                    ProjectResponse projectResponse = new ProjectResponse();
                    List<UserProjectMapping> mappings = userProjectRepo.findByProjectId(project.getId());
                    projectResponse.setId(project.getId());
                    projectResponse.setName(project.getName());
                    projectResponse.setDescription(project.getDescription());
                    projectResponse.setTotalMembers(mappings.size());
                    projectResponse.setStatus(project.getStatus().name());
                    projectResponse.setUpdatedAt((project.getUpdatedAt()) == null ? project.getCreatedAt() : project.getUpdatedAt());
                    projectResponses.add(projectResponse);
                }
        );
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
    public List<ProjectResponse> getProjectByUserId(Long userId) {
        List<ProjectResponse> projectResponses = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        List<UserProjectMapping> mappings = userProjectRepo.findByUserId(userId);
        mappings.forEach(
                mapping -> {
                    projects.add(get(mapping.getProjectId()));
                }
        );
        getProjectReponse(projects, projectResponses);
        return projectResponses;
    }

    @Override
    @Transactional
    public void assignProject(AssignProjectRequest request) {
        userProjectRepo.findByProjectIdAndUserId(request.getProjectId(), request.getUserId())
                .ifPresent(mapping -> {
                    throw new ApiException("E409", "Project already assigned to user");
                });
        UserProjectMapping mapping = new UserProjectMapping();
        mapping.setUserId(request.getUserId());
        mapping.setProjectId(request.getProjectId());
        userProjectRepo.save(mapping);
    }

    @Override
    public List<User> getUserByProjectId(Long projectId) {
        List<User> users = new ArrayList<>();
            List<UserProjectMapping> mapping = userProjectRepo.findByProjectId(projectId);
            mapping.forEach( e -> {
                if (e.getUserId() != null) {
                    Optional<User> user = userRepo.findById(e.getUserId());
                    if (user.isPresent()) {
                        users.add(user.get());
                    } else {
                        throw new ApiException("E404", "User not found");
                    }
                }
            });
        return users;
    }

    @Override
    public void deleteAssignUser(Long projectId, Long userId) {
        Optional<UserProjectMapping> mapping = userProjectRepo.findByProjectIdAndUserId(projectId, userId);
        if (mapping.isPresent()) {
            userProjectRepo.delete(mapping.get());
        } else {
            throw new ApiException("E404", "Mapping not found");
        }
    }
}
