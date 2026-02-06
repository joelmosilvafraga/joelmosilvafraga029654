package com.joelmofraga.artists_albums_api.media.repository;

import com.joelmofraga.artists_albums_api.media.domain.AlbumMedia;
import com.joelmofraga.artists_albums_api.media.domain.AlbumMediaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlbumMediaRepository extends JpaRepository<AlbumMedia, AlbumMediaId> {

    @Query("""
        select am from AlbumMedia am
        where am.id.albumId = :albumId
          and am.mediaType = :mediaType
          and am.isPrimary = true
        order by am.createdAt desc
    """)
    Optional<AlbumMedia> findFirstPrimaryByAlbumIdAndMediaType(Long albumId, String mediaType);

    @Modifying
    @Query("""
        update AlbumMedia am
        set am.isPrimary = false
        where am.id.albumId = :albumId
          and am.mediaType = :mediaType
          and am.isPrimary = true
    """)
    void clearPrimaryCover(Long albumId, String mediaType);
}
