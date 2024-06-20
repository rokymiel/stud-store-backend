package ru.hse.store.projectApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.store.projectApi.entity.Binary;
import ru.hse.store.projectApi.entity.Project;
import ru.hse.store.projectApi.repository.BinaryRepository;
import ru.hse.store.projectApi.repository.ProjectRepository;
import ru.hse.store.restApi.dto.project.NewProjectResponse;
import ru.hse.store.restApi.dto.project.ProjectRequest;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.exceptions.NotFoundException;
import ru.hse.store.userApi.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BinaryService {
    private final BinaryRepository binaryRepository;
    private final ProjectRepository projectRepository;
    private final String storagePath = "projects";

    @Transactional
    public Long upload(Long projectId, MultipartFile file) throws IOException {
        var project = projectRepository.findById(projectId).orElseThrow(
                () -> new NotFoundException(Project.class)
        );

        var fileName = file.getOriginalFilename();
        String path = String.format("%s/%s/%s", storagePath, projectId, fileName);

        File theDir = new File(String.format("%s/%s", storagePath, projectId));
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        InputStream initialStream = file.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = new File(path);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }

        var entity = new Binary();
        entity.setProjectId(projectId);
        entity.setFile(fileName);

        var response = binaryRepository.save(entity);
        project.setWorkBinary(response.getId().toString());
        projectRepository.save(project);

        return  response.getId();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> load(Long fileId) throws IOException {
        var file = binaryRepository.findById(fileId).orElseThrow(
                () -> new NotFoundException(Binary.class)
        );
        String path = String.format("%s/%s/%s", storagePath, file.getProjectId(), file.getFile());

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + path + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(Files.readAllBytes(Path.of(path)));
    }
}
