package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.enums.EstatusAlumno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

	// ==========================================
	// BÚSQUEDAS BÁSICAS
	// ==========================================
	Optional<Alumno> findByEmail(String email);

	Optional<Alumno> findByMatricula(String matricula);

	Optional<Alumno> findByCurp(String curp);

	boolean existsByEmail(String email);

	boolean existsByMatricula(String matricula);

	boolean existsByCurp(String curp);

	List<Alumno> findByActivoTrue();

	Page<Alumno> findByActivoTrue(Pageable pageable);

	List<Alumno> findByEstatus(EstatusAlumno estatus);
	
	Optional<Alumno> findByUsuario_Id(Long usuarioId);

	// ==========================================
	// BÚSQUEDA POR TÉRMINO
	// ==========================================
	@Query("SELECT a FROM Alumno a WHERE a.activo = true AND (" +
		       "LOWER(CONCAT(a.nombre, ' ', a.apellidoP, ' ', a.apellidoM)) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
		       "LOWER(a.matricula) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
		       "LOWER(a.email) LIKE LOWER(CONCAT('%', :termino, '%'))" +
		       ")")
		List<Alumno> buscarPorTermino(@Param("termino") String termino);
}