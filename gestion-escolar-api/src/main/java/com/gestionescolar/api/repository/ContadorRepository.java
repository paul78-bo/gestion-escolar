package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Contador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContadorRepository extends JpaRepository<Contador, Long> {

    Optional<Contador> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    // 🔒 Bloqueo pesimista para evitar colisiones en concurrencia
    @Query("SELECT c FROM Contador c WHERE c.nombre = :nombre")
    Optional<Contador> findByNombreForUpdate(@Param("nombre") String nombre);
}