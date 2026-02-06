package com.joelmofraga.artists_albums_api.album.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AlbumCreateRequest {

    @NotBlank
    @Size(max = 250)
    private String title;

    @NotNull
    private Integer releaseYear;

    @Size(max = 100)
    private String genre;

    @NotBlank
    @Size(max = 50)
    private String albumTypeCode;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getAlbumTypeCode() { return albumTypeCode; }
    public void setAlbumTypeCode(String albumTypeCode) { this.albumTypeCode = albumTypeCode; }
}
