package ru.hse.store.userApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.store.restApi.dto.user.ShortUserResponse;
import ru.hse.store.restApi.dto.user.UserRequest;
import ru.hse.store.restApi.dto.user.UserResponse;
import ru.hse.store.restApi.exceptions.InvalidArgumentException;
import ru.hse.store.restApi.exceptions.NotFoundException;
import ru.hse.store.userApi.entity.Role;
import ru.hse.store.userApi.entity.User;
import ru.hse.store.userApi.mapper.UserMapper;
import ru.hse.store.userApi.repository.UserRepository;
import ru.hse.store.userApi.security.permission.StaticRole;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String DEFAULT_ROLE_NAME = "ROLE_STUDENT";

    private final UserRepository repository;
    private final UserMapper mapper;
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(UserRequest dto, StaticRole staticRole) {
        validateRequest(dto);

        repository.findByEmail(dto.email()).ifPresent(
                user -> {
                    throw new InvalidArgumentException("User with email %s already exists".formatted(user.getEmail()));
                }
        );
        Set<Role> startRole = Set.of(rolesService.getFromStatic(staticRole));

        var user = mapper.map(dto, passwordEncoder).setRoles(startRole);
        return repository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserResponse getFullUser(Long id) {
        var user = repository.findById(id).orElseThrow(
                () -> new NotFoundException(User.class)
        );
        return mapper.map(user);
    }

    @Transactional(readOnly = true)
    public ShortUserResponse getShortUser(Long id) {
        var user = repository.findById(id).orElseThrow(
                () -> new NotFoundException(User.class)
        );
        return mapper.mapToShort(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest dto) {
        validateRequest(dto);

        var existingUser = repository.findById(id).orElseThrow(
                () -> new NotFoundException(User.class)
        );
        var user = mapper.map(dto)
                .setId(existingUser.getId())
                .setPassword(dto.password() == null ? existingUser.getPassword() : passwordEncoder.encode(dto.password()));
                //.setRoles(existingUser.getRoles());
        return mapper.map(repository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        var existingUser = repository.findById(id).orElseThrow(
                () -> new NotFoundException(User.class)
        );
        repository.delete(existingUser);
    }

    private void validateRequest(UserRequest dto) {
        if (dto.email().length() > 64 || dto.firstName().length() > 64 || dto.lastName().length() > 64) {
            throw new InvalidArgumentException("email and names should contain less than 64 chars");
        }
    }
}

