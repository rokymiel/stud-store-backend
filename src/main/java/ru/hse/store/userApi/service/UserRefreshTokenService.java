package ru.hse.store.userApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.store.restApi.exceptions.NotFoundException;
import ru.hse.store.restApi.exceptions.RefreshTokenException;
import ru.hse.store.userApi.entity.User;
import ru.hse.store.userApi.entity.UserRefreshToken;
import ru.hse.store.userApi.repository.UserRefreshTokenRepository;
import ru.hse.store.userApi.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserRefreshTokenService {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Value("${security.jwt.expiration-time}")
    private Long expirationMinutes;
    @Value("${security.jwt.refresh-period}")
    private Long refreshMinutes;

    @Transactional
    public UserRefreshToken createRefreshToken(Long userId) {
        return userRefreshTokenRepository.save(
                new UserRefreshToken()
                        .setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class)))
                        .setRefreshToken(UUID.randomUUID().toString())
                        .setExpirationDate(Instant.now().plusMillis(TimeUnit.MINUTES.toMillis(expirationMinutes)))
        );
    }

    @Transactional
    public UserRefreshToken updateRefreshToken(String token) {
        UserRefreshToken refreshToken = userRefreshTokenRepository.findByRefreshToken(token).orElseThrow(
                () -> new RefreshTokenException("Refresh token not found or expired")
        );
        if (refreshToken.isExpired()) {
            userRefreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token is expired");
        }
        Instant newExpirationDate = Instant.now().plusMillis(
                TimeUnit.MINUTES.toMillis(expirationMinutes + refreshMinutes)
        );
        String newRefreshToken = UUID.randomUUID().toString();
        return userRefreshTokenRepository.save(
                refreshToken
                        .setRefreshToken(newRefreshToken)
                        .setExpirationDate(newExpirationDate)
        );
    }

    @Transactional
    public void clearExpiredTokens() {
        userRefreshTokenRepository.deleteAllByExpirationDateBefore(Instant.now());
    }

}
