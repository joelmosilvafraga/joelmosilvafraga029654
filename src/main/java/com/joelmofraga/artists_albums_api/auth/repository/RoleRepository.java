package com.joelmofraga.artists_albums_api.auth.repository;

import com.joelmofraga.artists_albums_api.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
