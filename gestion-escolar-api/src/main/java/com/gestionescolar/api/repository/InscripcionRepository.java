package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Inscripcion;
import com.gestionescolar.api.enums.EstatusInscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

	// ==========================================
	// BÚSQUEDAS BÁSICAS
	// ==========================================
	List<Inscripcion> findByAlumnoId(Long alumnoId);

	List<Inscripcion> findByGrupoId(Long grupoId);

	List<Inscripcion> findByEstatus(EstatusInscripcion estatus);

	List<Inscripcion> findByActivoTrue();

	Page<Inscripcion> findByActivoTrue(Pageable pageable);

	Optional<Inscripcion> findByAlumnoIdAndGrupoId(Long alumnoId, Long grupoId);

	// ==========================================
	// VALIDACIONES
	// ==========================================
	boolean existsByAlumnoIdAndGrupoId(Long alumnoId, Long grupoId);

	// ==========================================
	// CONSULTAS PERSONALIZADAS
	// ==========================================
	@Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :alumnoId AND i.activo = true")
	List<Inscripcion> findActivasByAlumnoId(@Param("alumnoId") Long alumnoId);

	@Query("SELECT i FROM Inscripcion i WHERE i.grupo.id = :grupoId AND i.activo = true")
	List<Inscripcion> findActivasByGrupoId(@Param("grupoId") Long grupoId);

	@Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.grupo.id = :grupoId AND i.activo = true")
	long countAlumnosByGrupoId(@Param("grupoId") Long grupoId);

	// ==========================================
	// 🔍 BÚSQUEDA POR TÉRMINO
	// ==========================================
	@Query("SELECT i FROM Inscripcion i " + "LEFT JOIN i.alumno a " + "LEFT JOIN i.grupo g " + "LEFT JOIN g.ciclo c "
			+ "WHERE i.activo = true AND ("
			+ "LOWER(CONCAT(a.nombre, ' ', a.apellidoP, ' ', a.apellidoM)) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(a.matricula) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(CONCAT(g.nombre, ' ', g.grado, ' ', g.letra)) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))" + ")")
	List<Inscripcion> buscarPorTermino(@Param("termino") String termino);
}