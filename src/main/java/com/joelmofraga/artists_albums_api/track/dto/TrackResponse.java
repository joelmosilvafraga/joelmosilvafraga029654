package com.joelmofraga.artists_albums_api.track.dto;

import java.time.Instant;

public record TrackResponse(
        Long id,
        Long albumId,
        Integer trackNumber,
        String title,
        Integer durationSeconds,
        Instant createdAt
) {}
