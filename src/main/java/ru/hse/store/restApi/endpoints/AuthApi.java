package ru.hse.store.restApi.endpoints;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hse.store.restApi.dto.auth.AuthRequest;
import ru.hse.store.restApi.dto.auth.AuthResponse;
//import com.tinkoffedu.dto.auth.RefreshTokenRequest;
//import com.tinkoffedu.dto.auth.RefreshTokenResponse;
import ru.hse.store.restApi.dto.auth.RefreshTokenRequest;
import ru.hse.store.restApi.dto.auth.RefreshTokenResponse;
import ru.hse.store.restApi.dto.status.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "authentication")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "request processed successfully"),
        @ApiResponse(responseCode = "400", description = "validation error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))})
})
public interface AuthApi {

//    @Operation(summary = "Логин по email и паролю")
//    @PostMapping(value = "/api/v1/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
//    AuthResponse login(@RequestBody AuthRequest request);

    @Operation(summary = "Логин по email и паролю")
    @PostMapping(value = "/api/v1/auth/login")
    ResponseEntity<?> login(@RequestBody AuthRequest request);

    @Operation(summary = "Обновление токена авторизации")
    @GetMapping(value = "/api/v1/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken);

    @Operation(summary = "Обновление токена авторизации")
    @PostMapping(value = "/api/v1/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> refreshToken(Authentication authentication, HttpServletRequest request, HttpServletResponse response);
}

