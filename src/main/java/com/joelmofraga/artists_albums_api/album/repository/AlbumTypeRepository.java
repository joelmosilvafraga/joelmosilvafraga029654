package com.joelmofraga.artists_albums_api.album.repository;


import com.joelmofraga.artists_albums_api.album.domain.AlbumType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumTypeRepository extends JpaRepository<AlbumType, Long> {
    Optional<AlbumType> findByCodeIgnoreCase(String code);
}
