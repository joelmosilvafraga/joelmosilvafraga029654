package com.joelmofraga.artists_albums_api.artist.repository;

import com.joelmofraga.artists_albums_api.artist.domain.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String name, Sort sort);
}

