package ru.hse.store.userApi.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.hse.store.userApi.entity.Permission;
import ru.hse.store.userApi.entity.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@UtilityClass
public class UserPermissionUtils {

    public static List<GrantedAuthority> getAllowedAuthorities(Set<Role> roles, Set<Permission> permissions) {
        return getAuthorities(roles, permissions)
                .stream()
                .map(privilege -> (GrantedAuthority) new SimpleGrantedAuthority(privilege))
                .toList();
    }

    private static List<String> getAuthorities(Set<Role> roles, Set<Permission> permissions) {
        Stream<String> roleNames = roles.stream()
                .map(Role::getName);
//        Stream<String> privilegesNames = roles.stream()
//                .map(Role::getPermissions)
//                .flatMap(Set::stream)
//                .map(Permission::getName);
        Stream<String> permissionNames = permissions.stream()
                .map(Permission::getName);
        return Stream.concat(roleNames, permissionNames).toList();
    }
}
