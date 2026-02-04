package com.joelmofraga.artists_albums_api.artist.domain;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import com.joelmofraga.artists_albums_api.album.domain.ArtistAlbumId;
import jakarta.persistence.*;

@Entity
@Table(name = "album_artist")
public class ArtistAlbum {

    @EmbeddedId
    private ArtistAlbumId id;

    @ManyToOne
    @MapsId("albumId")
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private Artist artist;
}
