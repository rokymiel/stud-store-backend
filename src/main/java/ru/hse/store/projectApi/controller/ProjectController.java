package ru.hse.store.projectApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.store.projectApi.service.BinaryService;
import ru.hse.store.projectApi.service.ImageService;
import ru.hse.store.projectApi.service.ProjectService;
import ru.hse.store.restApi.dto.project.NewProjectResponse;
import ru.hse.store.restApi.dto.project.ProjectRequest;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.endpoints.ProjectApi;
import ru.hse.store.restApi.exceptions.StudStoreException;
import ru.hse.store.userApi.security.UserAuthProviderService;

import java.io.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ProjectController implements ProjectApi {

    private final UserAuthProviderService authProviderService;
    private final ProjectService projectService;
    private final BinaryService binaryService;
    private final ImageService imageService;

    @Override
    public ProjectResponse getProject(Long projectId) {
        return projectService.getProject(projectId);
    }

    @Override
    public NewProjectResponse newProject(ProjectRequest dto) {
        Long id = authProviderService.getCurrentUserId();
        return projectService.createProject(dto, id);
    }

    @Override
    public ResponseEntity<?> updateProject(Long projectId, ProjectRequest dto) {
        projectService.updateProject(projectId, dto);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<ProjectResponse> getMyProjects() {
        Long id = authProviderService.getCurrentUserId();
        return projectService.getProjectsOfUser(id);
    }

    @Override
    public ResponseEntity<?> uploadBinaryFile(@Param("projectId") Long projectId, MultipartFile file) throws IOException {
        var fileId = binaryService.upload(projectId, file);
        return ResponseEntity.ok(fileId);
    }


    @Override
    public ResponseEntity<?> getBinaryFile(Long fileId) throws IOException {

        return binaryService.load(fileId);
    }

    @Override
    public ResponseEntity<?> uploadImageFile(Long projectId, String type, MultipartFile file) throws IOException {
        switch (type) {
            case ("icon"):
                return imageService.uploadIcon(projectId, file);
            case ("preview"):
                return imageService.uploadPreview(projectId, file);
            default:
                throw new StudStoreException("Unsupported type: " + type);
        }
    }

    @Override
    public ResponseEntity<?> loadImage(Long projectId, String imageId) throws IOException {
        return imageService.getImage(projectId, imageId);
    }
}