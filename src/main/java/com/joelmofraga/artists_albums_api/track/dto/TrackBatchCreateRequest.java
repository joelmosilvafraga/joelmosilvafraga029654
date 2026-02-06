package com.joelmofraga.artists_albums_api.track.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TrackBatchCreateRequest(
        @NotEmpty List<@Valid TrackCreateRequest> tracks
) {}
