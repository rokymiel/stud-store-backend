package ru.hse.store.restApi.endpoints.webstore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.status.StatusResponse;
import ru.hse.store.restApi.dto.webstore.LandingResponse;
import ru.hse.store.restApi.dto.webstore.StoreScreenResponse;

import java.util.List;

@Tag(name = "web store")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "request processed successfully"),
        @ApiResponse(responseCode = "400", description = "validation error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))})
})
public interface PublicWebStoreApi {
    @Operation(summary = "Поиск проектов в публичной зоне")
    @GetMapping("/public/search")
    List<ProjectResponse> publicSearch(@RequestParam String query);

    @Operation(summary = "Конфигурация экрана публичного магазина")
    @GetMapping("/public/store")
    StoreScreenResponse publicStoreScreen();

    @Operation(summary = "Проекты лэндинговой страницы")
    @GetMapping("/public/landing")
    LandingResponse landingProjects();
}
