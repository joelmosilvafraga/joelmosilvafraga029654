package com.joelmofraga.artists_albums_api.media.service;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import com.joelmofraga.artists_albums_api.media.domain.AlbumMedia;
import com.joelmofraga.artists_albums_api.media.domain.AlbumMediaId;
import com.joelmofraga.artists_albums_api.media.domain.MediaObject;
import com.joelmofraga.artists_albums_api.media.dto.AlbumCoverUploadResponse;
import com.joelmofraga.artists_albums_api.media.repository.AlbumMediaRepository;
import com.joelmofraga.artists_albums_api.media.repository.MediaObjectRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlbumCoverService {

    private static final String MEDIA_TYPE_COVER = "COVER";
    private static final Duration PRESIGNED_EXPIRATION = Duration.ofMinutes(30);

    private final S3Client s3;
    private final S3Presigner presigner;

    private final AlbumMediaRepository albumMediaRepository;
    private final MediaObjectRepository mediaObjectRepository;
    private final EntityManager entityManager;

    @Value("${storage.s3.bucket}")
    private String bucket;

    @Transactional
    public AlbumCoverUploadResponse uploadCover(Long albumId, MultipartFile file) throws IOException {
        if (albumId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "albumId is required");
        }
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file is required");
        }

        if (!entityManager.contains(entityManager.getReference(Album.class, albumId))) {
            Album albumCheck = entityManager.find(Album.class, albumId);
            if (albumCheck == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found: " + albumId);
            }
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        String ext = guessExt(contentType);
        String key = "albums/" + albumId + "/cover/" + System.currentTimeMillis() + ext;

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        try {
            MediaObject media = new MediaObject();
            media.setBucket(bucket);
            media.setObjectKey(key);
            media.setContentType(contentType);
            media.setSizeBytes(file.getSize());
            media.setCreatedAt(OffsetDateTime.now());
            media = mediaObjectRepository.save(media);

            albumMediaRepository.clearPrimaryCover(albumId, MEDIA_TYPE_COVER);

            AlbumMedia link = new AlbumMedia();

            Album albumRef = entityManager.getReference(Album.class, albumId);
            MediaObject mediaRef = entityManager.getReference(MediaObject.class, media.getId());

            link.setAlbum(albumRef);
            link.setMedia(mediaRef);

            link.setId(new AlbumMediaId(albumId, media.getId()));

            link.setMediaType(MEDIA_TYPE_COVER);
            link.setPrimary(true);
            link.setCreatedAt(OffsetDateTime.now());
            albumMediaRepository.save(link);

            String url = generatePresignedGetUrl(bucket, key, PRESIGNED_EXPIRATION);
            OffsetDateTime expiresAt = OffsetDateTime.now().plus(PRESIGNED_EXPIRATION);

            return new AlbumCoverUploadResponse(
                    albumId,
                    media.getId(),
                    bucket,
                    key,
                    contentType,
                    file.getSize(),
                    url,
                    expiresAt
            );
        } catch (RuntimeException ex) {
            try {
                s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
            } catch (Exception ignored) { }
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public PresignedUrlResponse getCoverPresignedUrl(Long albumId) {
        if (albumId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "albumId is required");
        }

        AlbumMedia cover = albumMediaRepository
                .findFirstPrimaryByAlbumIdAndMediaType(albumId, MEDIA_TYPE_COVER)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cover not found for album: " + albumId
                ));

        Long mediaId = Objects.requireNonNull(cover.getId(), "AlbumMedia.id is null").getMediaId();

        MediaObject media = mediaObjectRepository.findById(mediaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media not found: " + mediaId
                ));

        String url = generatePresignedGetUrl(media.getBucket(), media.getObjectKey(), PRESIGNED_EXPIRATION);
        OffsetDateTime expiresAt = OffsetDateTime.now().plus(PRESIGNED_EXPIRATION);

        return new PresignedUrlResponse(url, expiresAt);
    }

    private String generatePresignedGetUrl(String bucket, String key, Duration expiration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    private String guessExt(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/jpeg" -> ".jpg";
            default -> "";
        };
    }

    public record PresignedUrlResponse(String url, OffsetDateTime expiresAt) {}
}
