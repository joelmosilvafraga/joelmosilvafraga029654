package com.joelmofraga.artists_albums_api.websocket.dto;

import java.time.Instant;

public record AlbumCreatedEvent(
        Long albumId,
        String title,
        Integer releaseYear,
        Instant createdAt
) {}