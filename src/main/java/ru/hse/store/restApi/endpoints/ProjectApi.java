package ru.hse.store.restApi.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.store.restApi.dto.project.NewProjectResponse;
import ru.hse.store.restApi.dto.project.ProjectRequest;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.status.StatusResponse;

import java.io.IOException;
import java.util.List;

@Tag(name = "projects")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "request processed successfully"),
        @ApiResponse(responseCode = "400", description = "validation error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))})
})
public interface ProjectApi {

    @Operation(summary = "Получение проекта по айди")
    @GetMapping("/project/{projectId}")
    ProjectResponse getProject(@PathVariable Long projectId);

    @Operation(summary = "Создать новый проект")
    @PostMapping(value = "/project/new", produces = MediaType.APPLICATION_JSON_VALUE)
    NewProjectResponse newProject(@RequestBody ProjectRequest dto);

    @Operation(summary = "Обновить основные данные о проекте")
    @PutMapping(value = "/project/{projectId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest dto);

    @Operation(summary = "Получение всех проектов пользователя")
    @GetMapping(value = "/projects/my", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProjectResponse> getMyProjects();

    @Operation(summary = "Загрузка бинаря")
    @PostMapping(value = "/project/binary/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> uploadBinaryFile(
//            @RequestPart(value = "file", required = false) MultipartFile file
            @Param("projectId") Long projectId,
            MultipartFile file
    ) throws IOException;

    @Operation(summary = "Получение бинаря")
    @GetMapping(
            value = "/project/binary/{fileId}"
//            produces = MediaType.//APPLICATION_OCTET_STREAM_VALUE
    )
    ResponseEntity<?> getBinaryFile(@PathVariable Long fileId) throws IOException;

    @Operation(summary = "Загрузка изображения")
    @PostMapping(value = "/project/image/{type}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> uploadImageFile(
            @Param("projectId") Long projectId,
            @PathVariable String type,
            MultipartFile file
    ) throws IOException;

    @Operation(summary = "Загрузка изображения")
    @GetMapping(value = "/project/image",
            produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<?> loadImage(
            @Param("projectId") Long projectId,
            @Param("imageId") String imageId
    ) throws IOException;
}