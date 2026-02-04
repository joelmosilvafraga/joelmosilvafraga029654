package com.joelmofraga.artists_albums_api.album.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ArtistAlbumId implements Serializable {

    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "artist_id")
    private Long artistId;
}