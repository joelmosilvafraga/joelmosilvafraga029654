package com.joelmofraga.artists_albums_api.track.domain;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "track",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_track_album_track_number", columnNames = {"album_id", "track_number"})
        },
        indexes = {
                @Index(name = "ix_track_album_id", columnList = "album_id"),
                @Index(name = "ix_track_title", columnList = "title")
        }
)
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(name = "track_number", nullable = false)
    private Integer trackNumber;

    @Column(nullable = false, length = 250)
    private String title;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }


    public Long getId() { return id; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public Integer getTrackNumber() { return trackNumber; }
    public void setTrackNumber(Integer trackNumber) { this.trackNumber = trackNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public Instant getCreatedAt() { return createdAt; }
}
