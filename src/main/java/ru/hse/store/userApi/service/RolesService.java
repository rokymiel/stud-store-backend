package ru.hse.store.userApi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.store.userApi.entity.Role;
import ru.hse.store.userApi.repository.RoleRepository;
import ru.hse.store.userApi.security.permission.StaticRole;

import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RoleRepository roleRepository;

    @Transactional
    public Role getFromStatic(StaticRole staticRole) {
        AtomicReference<Role> result = new AtomicReference<>();
        var name = staticRole.getAuthorityName();
        roleRepository.findByName(name).ifPresentOrElse(
                result::set,
                () -> result.set(roleRepository.save(new Role().setName(name)))
        );

        return result.get();
    }
}
