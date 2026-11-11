package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.enums.DiaSemana;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    // ==========================================
    // BÚSQUEDAS BÁSICAS
    // ==========================================
    List<Asignacion> findByActivoTrue();

    Page<Asignacion> findByActivoTrue(Pageable pageable);

    List<Asignacion> findByGrupoId(Long grupoId);

    List<Asignacion> findByProfesorId(Long profesorId);

    List<Asignacion> findByMateriaId(Long materiaId);

    Optional<Asignacion> findByGrupoIdAndMateriaId(Long grupoId, Long materiaId);

    boolean existsByGrupoIdAndMateriaId(Long grupoId, Long materiaId);

    // ==========================================
    // CONSULTAS PERSONALIZADAS
    // ==========================================
    @Query("SELECT a FROM Asignacion a WHERE a.grupo.id = :grupoId AND a.activo = true")
    List<Asignacion> findActivasByGrupoId(@Param("grupoId") Long grupoId);

    @Query("SELECT a FROM Asignacion a WHERE a.profesor.id = :profesorId AND a.activo = true")
    List<Asignacion> findActivasByProfesorId(@Param("profesorId") Long profesorId);

    @Query("SELECT a FROM Asignacion a WHERE a.materia.id = :materiaId AND a.activo = true")
    List<Asignacion> findActivasByMateriaId(@Param("materiaId") Long materiaId);

    @Query("SELECT COUNT(a) FROM Asignacion a WHERE a.grupo.id = :grupoId AND a.activo = true")
    long countAsignacionesByGrupoId(@Param("grupoId") Long grupoId);

    @Query("SELECT COUNT(a) FROM Asignacion a WHERE a.profesor.id = :profesorId AND a.activo = true")
    long countAsignacionesByProfesorId(@Param("profesorId") Long profesorId);

    // ==========================================
    // 🔍 BÚSQUEDA POR TÉRMINO
    // ==========================================
    @Query("SELECT a FROM Asignacion a " +
    	       "LEFT JOIN a.grupo g " +
    	       "LEFT JOIN a.materia m " +
    	       "LEFT JOIN a.profesor p " +
    	       "WHERE a.activo = true AND (" +
    	       "LOWER(CONCAT(g.nombre, ' ', g.grado, ' ', g.letra)) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
    	       "LOWER(CONCAT(m.nombre, ' ', m.codigo)) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
    	       "LOWER(CONCAT(p.nombre, ' ', p.apellidoP, ' ', p.apellidoM)) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
    	       "LOWER(a.aula) LIKE LOWER(CONCAT('%', :termino, '%'))" +
    	       ")")
    	List<Asignacion> buscarPorTermino(@Param("termino") String termino);
    

boolean existsByGrupoIdAndMateriaIdAndProfesorIdAndDiaSemanaAndActivoTrue(
    Long grupoId, Long materiaId, Long profesorId, DiaSemana diaSemana);
}