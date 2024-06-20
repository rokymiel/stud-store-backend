package ru.hse.store.userApi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.hse.store.restApi.exceptions.StudStoreException;
import ru.hse.store.userApi.dto.UserAuthDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthProviderService {

    public Long getCurrentUserId() {
        var principal = getPrincipal();
        if (principal instanceof UserAuthDetails) {
            return ((UserAuthDetails) principal).getId();
        }
        throw new StudStoreException("Unknown principal format");
    }

    public String getCurrentUserEmail() {
        var principal = getPrincipal();
        if (principal instanceof String) {
            return (String) principal;
        }
        if (principal instanceof UserAuthDetails) {
            return ((UserAuthDetails) principal).getEmail();
        }
        throw new StudStoreException("Unknown principal format");
    }

    private Object getPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> new StudStoreException("Unable to get principal"));
    }

}

