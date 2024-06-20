package ru.hse.store.restApi.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.hse.store.restApi.dto.status.StatusResponse;
import ru.hse.store.restApi.dto.user.ShortUserResponse;
import ru.hse.store.restApi.dto.user.UserRequest;
import ru.hse.store.restApi.dto.user.UserResponse;

@Tag(name = "users")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "request processed successfully"),
        @ApiResponse(responseCode = "400", description = "validation error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))})
})
public interface UserApi {

    @Operation(summary = "Создать пользователя")
    @PostMapping(value = "/api/v1/user/register/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    StatusResponse createUser(@RequestBody UserRequest dto, @PathVariable(required = false) String type);

    @Operation(summary = "Получить пользователя (текущего)")
    @GetMapping(value = "/api/v1/user/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse getUser();

    @Operation(summary = "Получить пользователя по айди")
    @GetMapping(value = "/api/v1/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ShortUserResponse getUser(@PathVariable Long id);

    @Operation(summary = "Обновить пользователя")
    @PutMapping(value = "/api/v1/user/update", produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse updateUser(@RequestBody UserRequest dto);

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping(value = "/api/v1/user/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    StatusResponse deleteUser();
}
