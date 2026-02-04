package com.joelmofraga.artists_albums_api.auth.service;

import com.joelmofraga.artists_albums_api.auth.domain.RefreshToken;
import com.joelmofraga.artists_albums_api.auth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private static final SecureRandom RNG = new SecureRandom();

    private final RefreshTokenRepository repo;
    private final long expirationDays;

    public RefreshTokenService(RefreshTokenRepository repo,
                               @Value("${app.security.refresh.expiration-days:7}") long expirationDays) {
        this.repo = repo;
        this.expirationDays = expirationDays;
    }

    public Issued issue(Long userId) {
        String raw = generateRawToken();
        String hash = sha256Base64(raw);

        RefreshToken rt = new RefreshToken();
        rt.setUserId(userId);
        rt.setTokenHash(hash);
        rt.setExpiresAt(Instant.now().plus(expirationDays, ChronoUnit.DAYS));
        rt.setRevoked(false);

        repo.save(rt);

        return new Issued(raw, rt.getExpiresAt());
    }


    public Long validateAndRevoke(String rawRefreshToken) {
        String hash = sha256Base64(rawRefreshToken);

        RefreshToken stored = repo.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (stored.isRevoked()) throw new RuntimeException("Refresh token revoked");
        if (stored.getExpiresAt().isBefore(Instant.now())) throw new RuntimeException("Refresh token expired");

        stored.setRevoked(true);
        repo.save(stored);

        return stored.getUserId();
    }

    public record Issued(String refreshToken, Instant expiresAt) {}

    private String generateRawToken() {
        byte[] bytes = new byte[64];
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Base64(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("hash error", e);
        }
    }
}
