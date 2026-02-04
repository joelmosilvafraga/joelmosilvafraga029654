package com.joelmofraga.artists_albums_api.auth.dto;

public record RefreshResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        String refreshToken
) {}
