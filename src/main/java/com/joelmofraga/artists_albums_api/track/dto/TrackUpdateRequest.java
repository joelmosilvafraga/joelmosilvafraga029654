package com.joelmofraga.artists_albums_api.track.dto;

import jakarta.validation.constraints.Size;

public record TrackUpdateRequest(
        @Size(max = 250) String title,
        Integer durationSeconds,
        Integer trackNumber
) {}
