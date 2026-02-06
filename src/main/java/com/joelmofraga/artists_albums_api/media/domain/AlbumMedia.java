package com.joelmofraga.artists_albums_api.media.domain;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "album_media")
public class AlbumMedia {

    @EmbeddedId
    private AlbumMediaId id;

    @MapsId("albumId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id")
    private Album album;

    @MapsId("mediaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_id")
    private MediaObject media;

    @Column(name = "media_type", nullable = false, length = 40)
    private String mediaType;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
