package ru.hse.store.projectApi.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.store.projectApi.entity.Project;
import ru.hse.store.projectApi.repository.ProjectRepository;
import ru.hse.store.restApi.exceptions.NotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ProjectRepository projectRepository;
    private final String storagePath = "images";

    @Transactional
    public ResponseEntity<?> uploadIcon(Long projectId, MultipartFile file) throws IOException {
        var project = projectRepository.findById(projectId).orElseThrow(
                () -> new NotFoundException(Project.class)
        );

        System.out.println("LOG -> " + file.getOriginalFilename());
        System.out.println("LOG -> " + file.getName());
        System.out.println("LOG -> " + file.getContentType());
        UUID uuid = saveImage(projectId, file);

        project.setIcon(uuid.toString());
        projectRepository.save(project);

        return ResponseEntity.ok(uuid);
    }

    @Transactional
    public ResponseEntity<?> uploadPreview(Long projectId, MultipartFile file) throws IOException {
        var project = projectRepository.findById(projectId).orElseThrow(
                () -> new NotFoundException(Project.class)
        );

        System.out.println("LOG -> " + file.getOriginalFilename());
        System.out.println("LOG -> " + file.getName());
        System.out.println("LOG -> " + file.getContentType());
        UUID uuid = saveImage(projectId, file);

        var photos = project.getPhotos();
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(uuid.toString());
        project.setPhotos(photos);

        projectRepository.save(project);

        return ResponseEntity.ok(uuid);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getImage(Long projectId, String imageId) throws IOException {
        var fileName = imageId + ".jpg";
        String path = String.format("%s/%s/%s", storagePath, projectId, fileName);
        BufferedImage image = ImageIO.read(new File(path));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);

        return ResponseEntity.ok(os.toByteArray());
    }

    private @NotNull UUID saveImage(Long projectId, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        var fileName = uuid + ".jpg";
        String path = String.format("%s/%s/%s", storagePath, projectId, fileName);

        File theDir = new File(String.format("%s/%s", storagePath, projectId));
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        InputStream initialStream = file.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = new File(path);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }
        return uuid;
    }
}
