package com.joelmofraga.artists_albums_api.media.dto;

public record UploadResponse(
        Long albumId,
        Long mediaId,
        String bucket,
        String key,
        String type,
        boolean primary
) {}
