package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Calificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

	// ==========================================
	// BÚSQUEDAS BÁSICAS
	// ==========================================
	List<Calificacion> findByActivoTrue();

	Page<Calificacion> findByActivoTrue(Pageable pageable);

	List<Calificacion> findByAlumnoId(Long alumnoId);

	List<Calificacion> findByAsignacionId(Long asignacionId);

	Optional<Calificacion> findByAsignacionIdAndAlumnoId(Long asignacionId, Long alumnoId);

	boolean existsByAsignacionIdAndAlumnoId(Long asignacionId, Long alumnoId);
    // 👈 AGREGA ESTE MÉTODO
    Optional<Calificacion> findByAlumnoIdAndAsignacionIdAndPeriodo(Long alumnoId, Long asignacionId, Integer periodo);

	// ==========================================
	// CONSULTAS PERSONALIZADAS
	// ==========================================
	@Query("SELECT c FROM Calificacion c WHERE c.alumno.id = :alumnoId AND c.activo = true")
	List<Calificacion> findActivasByAlumnoId(@Param("alumnoId") Long alumnoId);

	@Query("SELECT c FROM Calificacion c WHERE c.asignacion.id = :asignacionId AND c.activo = true")
	List<Calificacion> findActivasByAsignacionId(@Param("asignacionId") Long asignacionId);

	@Query("SELECT AVG(c.calificacion) FROM Calificacion c "
			+ "WHERE c.alumno.id = :alumnoId AND c.activo = true AND c.calificacion IS NOT NULL")
	Double calcularPromedioByAlumnoId(@Param("alumnoId") Long alumnoId);

	@Query("SELECT AVG(c.calificacion) FROM Calificacion c "
			+ "WHERE c.asignacion.id = :asignacionId AND c.activo = true AND c.calificacion IS NOT NULL")
	Double calcularPromedioByAsignacionId(@Param("asignacionId") Long asignacionId);

	@Query("SELECT c FROM Calificacion c " + "WHERE c.alumno.id = :alumnoId "
			+ "AND c.asignacion.grupo.ciclo.id = :cicloId " + "AND c.activo = true")
	List<Calificacion> findActivasByAlumnoIdAndCicloId(@Param("alumnoId") Long alumnoId,
			@Param("cicloId") Long cicloId);

	@Query("SELECT COUNT(c) FROM Calificacion c WHERE c.asignacion.id = :asignacionId AND c.activo = true")
	long countCalificacionesByAsignacionId(@Param("asignacionId") Long asignacionId);

	// ==========================================
	// 🔍 BÚSQUEDA POR TÉRMINO
	// ==========================================
	@Query("SELECT c FROM Calificacion c " + "LEFT JOIN c.alumno a " + "LEFT JOIN c.asignacion asig "
			+ "LEFT JOIN asig.materia m " + "LEFT JOIN asig.grupo g " + "WHERE c.activo = true AND ("
			+ "LOWER(CONCAT(a.nombre, ' ', a.apellidoP, ' ', a.apellidoM)) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(a.matricula) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(CONCAT(m.nombre, ' ', m.codigo)) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(CONCAT(g.nombre, ' ', g.grado, ' ', g.letra)) LIKE LOWER(CONCAT('%', :termino, '%'))" + ")")
	List<Calificacion> buscarPorTermino(@Param("termino") String termino);

	boolean existsByAsignacionIdAndAlumnoIdAndPeriodo(Long id, Long id2, int periodo);
	
    @Query("SELECT c FROM Calificacion c " +
            "JOIN FETCH c.asignacion a " +
            "JOIN FETCH a.grupo g " +
            "JOIN FETCH a.materia m " +
            "WHERE c.alumno.id = :alumnoId AND c.activo = true " +
            "ORDER BY c.fechaActualizacion DESC") // 👈 ESTO ES LA MAGIA
     List<Calificacion> findCalificacionesConDetalles(@Param("alumnoId") Long alumnoId);
}