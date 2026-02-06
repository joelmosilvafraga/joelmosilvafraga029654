package com.joelmofraga.artists_albums_api.media.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlbumMediaId implements Serializable {

    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Column(name = "media_id", nullable = false)
    private Long mediaId;

    public AlbumMediaId() {}

    public AlbumMediaId(Long albumId, Long mediaId) {
        this.albumId = albumId;
        this.mediaId = mediaId;
    }

    public Long getAlbumId() { return albumId; }
    public Long getMediaId() { return mediaId; }

    public void setAlbumId(Long albumId) { this.albumId = albumId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbumMediaId that)) return false;
        return Objects.equals(albumId, that.albumId) && Objects.equals(mediaId, that.mediaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, mediaId);
    }
}
