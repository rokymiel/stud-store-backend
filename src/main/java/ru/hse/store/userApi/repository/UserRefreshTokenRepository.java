package ru.hse.store.userApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.hse.store.userApi.entity.UserRefreshToken;

import java.time.Instant;
import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    void deleteAllByExpirationDateBefore(Instant date);

}
