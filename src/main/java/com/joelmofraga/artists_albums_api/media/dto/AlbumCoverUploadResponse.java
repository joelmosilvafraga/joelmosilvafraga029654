package com.joelmofraga.artists_albums_api.media.dto;

import java.time.OffsetDateTime;

public record AlbumCoverUploadResponse(
        Long albumId,
        Long mediaId,
        String bucket,
        String objectKey,
        String contentType,
        Long sizeBytes,
        String url,
        OffsetDateTime expiresAt
) {}