package com.joelmofraga.artists_albums_api.track.repository;

import com.joelmofraga.artists_albums_api.track.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {


    @Query("select coalesce(max(t.trackNumber), 0) from Track t where t.album.id = :albumId")
    int findMaxTrackNumberByAlbumId(@Param("albumId") Long albumId);


    List<Track> findAllByAlbumIdOrderByTrackNumberAsc(Long albumId);
}
