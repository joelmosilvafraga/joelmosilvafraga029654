package com.joelmofraga.artists_albums_api.auth.repository;

import com.joelmofraga.artists_albums_api.auth.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query("""
        select u from AppUser u
        left join fetch u.userRoles ur
        left join fetch ur.role r
        where u.username = :username
    """)
    Optional<AppUser> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
