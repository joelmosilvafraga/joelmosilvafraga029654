package com.joelmofraga.artists_albums_api.album.dto;

import java.time.Instant;

public class AlbumResponse {

    private Long id;
    private String title;
    private Integer releaseYear;
    private String genre;

    private String albumTypeCode;
    private String albumTypeDescription;

    private Instant createdAt;
    private Instant updatedAt;

    public AlbumResponse(
            Long id,
            String title,
            Integer releaseYear,
            String genre,
            String albumTypeCode,
            String albumTypeDescription,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.albumTypeCode = albumTypeCode;
        this.albumTypeDescription = albumTypeDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Integer getReleaseYear() { return releaseYear; }
    public String getGenre() { return genre; }
    public String getAlbumTypeCode() { return albumTypeCode; }
    public String getAlbumTypeDescription() { return albumTypeDescription; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
