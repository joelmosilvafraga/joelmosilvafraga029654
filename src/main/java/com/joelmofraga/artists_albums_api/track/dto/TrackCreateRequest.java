package com.joelmofraga.artists_albums_api.track.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrackCreateRequest(
        @NotBlank @Size(max = 250) String title,
        Integer durationSeconds
) {}
