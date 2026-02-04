package com.joelmofraga.artists_albums_api.album.repository;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {

    Optional<Album> findFirstByTitleIgnoreCase(String title);

    @Query("""
        select distinct a
        from Album a
        join a.artistAlbums aa
        join aa.artist ar
        where lower(ar.name) like lower(concat('%', :artistName, '%'))
        order by a.releaseYear asc nulls last, a.title asc
    """)
    List<Album> findAlbumsByArtistNameIgnoreCase(@Param("artistName") String artistName);
}