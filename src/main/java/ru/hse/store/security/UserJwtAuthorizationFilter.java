package ru.hse.store.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.hse.store.userApi.dto.UserAuthDetails;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.hse.store.security.JwtUtils.*;

@Component
@RequiredArgsConstructor
public class UserJwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = resolveClaims(request, secretKey);
        if (claims == null || isTokenExpired(claims)) {
            filterChain.doFilter(request, response);
            return;
        }
        List<GrantedAuthority> authorities = parseAuthorities(claims.get("authorities", String.class));
        UserAuthDetails userDetails = UserAuthDetails.builder()
                .id(claims.get("id", Long.class))
                .email(claims.get("email", String.class))
                .authorities(authorities)
                .password(null)
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> parseAuthorities(String authorities) {
        return authorities == null ? Collections.emptyList() : Arrays.stream(authorities.split(", "))
                .filter(privilege -> !privilege.isBlank())
                .map(privilege -> (GrantedAuthority) new SimpleGrantedAuthority(privilege))
                .toList();
    }
}
