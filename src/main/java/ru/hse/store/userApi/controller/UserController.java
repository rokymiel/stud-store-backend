package ru.hse.store.userApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.store.restApi.dto.status.StatusResponse;
import ru.hse.store.restApi.dto.user.ShortUserResponse;
import ru.hse.store.restApi.dto.user.UserRequest;
import ru.hse.store.restApi.dto.user.UserResponse;
import ru.hse.store.restApi.endpoints.UserApi;
import ru.hse.store.userApi.security.UserAuthProviderService;
import ru.hse.store.userApi.security.permission.StaticRole;
import ru.hse.store.userApi.service.UserService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController implements UserApi {

    private final UserAuthProviderService authProviderService;
    private final UserService userService;

    @Override
    public StatusResponse createUser(UserRequest dto, String type) {
        Long id = userService.createUser(dto, StaticRole.from(type));
        return new StatusResponse("ok", null);
    }

    @Override
    public UserResponse getUser() {
        Long id = authProviderService.getCurrentUserId();
        return userService.getFullUser(id);
    }

    @Override
    public ShortUserResponse getUser(Long id) {
        return userService.getShortUser(id);
    }

    @Override
    public UserResponse updateUser(UserRequest dto) {
        Long id = authProviderService.getCurrentUserId();
        return userService.updateUser(id, dto);
    }

    @Override
    public StatusResponse deleteUser() {
        Long id = authProviderService.getCurrentUserId();
        userService.deleteUser(id);
        return new StatusResponse("ok", null);
    }

}
