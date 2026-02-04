package com.joelmofraga.artists_albums_api.auth.repository;

import com.joelmofraga.artists_albums_api.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    long deleteByUserId(Long userId);
}
