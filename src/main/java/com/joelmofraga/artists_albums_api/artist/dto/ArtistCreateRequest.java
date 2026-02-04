package com.joelmofraga.artists_albums_api.artist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArtistCreateRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 120)
    private String country;

    @Size(max = 120)
    private String genre;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
