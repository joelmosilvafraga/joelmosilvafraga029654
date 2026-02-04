package com.joelmofraga.artists_albums_api.artist.dto;

import java.time.Instant;

public class ArtistResponse {

    private Long id;
    private String name;
    private String country;
    private String genre;
    private Instant createdAt;
    private Instant updatedAt;

    public ArtistResponse(Long id, String name, String country, String genre, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getGenre() { return genre; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
