package ru.hse.store.projectApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.store.projectApi.entity.Project;
import ru.hse.store.projectApi.mapper.ProjectMapper;
import ru.hse.store.projectApi.repository.ProjectRepository;
import ru.hse.store.restApi.dto.project.NewProjectResponse;
import ru.hse.store.restApi.dto.project.ProjectRequest;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional
    public NewProjectResponse createProject(ProjectRequest dto, Long creatorId) {
        var project = projectMapper.map(dto);
        project.setCreatorId(creatorId);

        var newProject = projectRepository.save(project);
        return new NewProjectResponse(newProject.getId().toString());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id) {
        var project = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Project.class)
        );
        return projectMapper.map(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsOfUser(Long userId) {

        var projects = projectRepository.findByCreatorId(userId);

        return projects.stream().map(projectMapper::map).toList() ;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {

        var projects = projectRepository.findAll();

        return projects.stream().map(projectMapper::map).toList() ;
    }

    @Transactional
    public void updateProject(Long id, ProjectRequest dto) {
        var existingProject = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Project.class)
        );
        var updatedProject = projectMapper.map(dto);
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setShortName(updatedProject.getShortName());
        projectRepository.save(existingProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> search(String query, Boolean isPublic) {
        var projects = projectRepository.findByQuery(query);
        return projects.stream().map(projectMapper::map).toList();
    }

//    @Transactional(readOnly = true)
//    public ProjectResponse getUserProjects(Long userId) {
//        var project = projectRepository.findById(id).orElseThrow(
//                () -> new NotFoundException(Project.class)
//        );
//        return projectMapper.map(project);
//    }
}
