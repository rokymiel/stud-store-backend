package ru.hse.store.restApi.endpoints;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.store.restApi.dto.admin.AdminNewStatusRequest;
import ru.hse.store.restApi.dto.admin.AdminProjectResponse;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.status.StatusResponse;

import java.util.List;

@Tag(name = "admin console")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "request processed successfully"),
        @ApiResponse(responseCode = "400", description = "validation error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))})
})
public interface AdminConsoleApi {
    @Operation(summary = "Изменение статуса")
    @PatchMapping(value = "/admin/project/{projectId}/status/update")
    ResponseEntity<?> updateProjectStatus(@RequestBody AdminNewStatusRequest dto, @PathVariable Long projectId);

    @Operation(summary = "Список всех проектов в системе")
    @GetMapping(value = "/admin/projects_list", produces = MediaType.APPLICATION_JSON_VALUE)
    List<AdminProjectResponse> projectsList();
}