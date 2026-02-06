package com.joelmofraga.artists_albums_api.media.repository;

import com.joelmofraga.artists_albums_api.media.domain.MediaObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaObjectRepository extends JpaRepository<MediaObject, Long> {}
