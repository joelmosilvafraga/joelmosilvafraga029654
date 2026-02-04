package com.joelmofraga.artists_albums_api.album.domain;
import com.joelmofraga.artists_albums_api.artist.domain.ArtistAlbum;
import jakarta.persistence.*;
import java.util.Set;
import java.time.Instant;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 250)
    private String title;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(length = 100)
    private String genre;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "album_type_id")
    private AlbumType albumType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private Set<ArtistAlbum> artistAlbums;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public AlbumType getAlbumType() { return albumType; }
    public void setAlbumType(AlbumType albumType) { this.albumType = albumType; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
