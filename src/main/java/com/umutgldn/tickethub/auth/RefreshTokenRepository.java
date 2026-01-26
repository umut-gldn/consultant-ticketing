package com.umutgldn.tickethub.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revokedAt= CURRENT_TIMESTAMP  WHERE rt.user.id= :userId AND rt.revokedAt IS NULL")
    void revokeAllByUserId(@Param("userId") UUID userId);

}
