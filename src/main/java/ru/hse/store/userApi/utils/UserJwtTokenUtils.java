package ru.hse.store.userApi.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.hse.store.userApi.dto.UserAuthDetails;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserJwtTokenUtils {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private Long expirationMinutes;

    public String createToken(UserAuthDetails user) {
        return Jwts.builder()
                .setExpiration(Date.from(Instant.now().plusMillis(TimeUnit.MINUTES.toMillis(expirationMinutes))))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .addClaims(Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "authorities", String.join(
                                ", ", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                        )
                ))
                .compact();
    }

}
