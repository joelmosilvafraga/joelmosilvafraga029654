package com.joelmofraga.artists_albums_api.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        String refreshToken
) {}
