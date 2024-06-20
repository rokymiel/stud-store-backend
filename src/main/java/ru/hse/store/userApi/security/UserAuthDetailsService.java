package ru.hse.store.userApi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hse.store.userApi.dto.UserAuthDetails;
import ru.hse.store.userApi.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.hse.store.userApi.utils.UserPermissionUtils.getAllowedAuthorities;

@Service
public class UserAuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserAuthDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var foundUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email %s not found".formatted(email))
        );
        // Назначаем авторизационные доступы из сохрненных в моделях данных о ролях и пермишенах
        // getAllowedAuthorities
        var authorities = getAllowedAuthorities(foundUser.getRoles(), foundUser.getPermissions());
        return UserAuthDetails.builder()
                .id(foundUser.getId())
                .email(foundUser.getEmail())
                .password(foundUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
