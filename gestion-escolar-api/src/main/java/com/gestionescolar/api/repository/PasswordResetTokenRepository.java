package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.PasswordResetToken;
import com.gestionescolar.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsuario(Usuario usuario);
    Optional<PasswordResetToken> findByShortId(String shortId);
    void deleteByUsuario(Usuario usuario);
    void deleteByExpiryDateBefore(java.time.LocalDateTime date);
}