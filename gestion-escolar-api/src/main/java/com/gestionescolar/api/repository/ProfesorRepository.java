package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Profesor;
import com.gestionescolar.api.enums.EstatusProfesor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

	Optional<Profesor> findByEmail(String email);

	Optional<Profesor> findByNumeroEmpleado(String numeroEmpleado);

	Optional<Profesor> findByCurp(String curp);

	Optional<Profesor> findByRfc(String rfc);

	Optional<Profesor> findByNss(String nss);

	boolean existsByEmail(String email);

	boolean existsByNumeroEmpleado(String numeroEmpleado);

	boolean existsByCurp(String curp);

	boolean existsByRfc(String rfc);

	boolean existsByNss(String nss);

	List<Profesor> findByActivoTrue();

	Page<Profesor> findByActivoTrue(Pageable pageable);

	List<Profesor> findByEstatus(EstatusProfesor estatus);

	List<Profesor> findByEspecialidadContainingIgnoreCase(String especialidad);

	Optional<Profesor> findByUsuarioId(Long usuarioId);
	
	@Query("SELECT p FROM Profesor p WHERE p.activo = true AND (" +
		       "LOWER(CONCAT(p.nombre, ' ', p.apellidoP, ' ', p.apellidoM)) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
		       "LOWER(p.numeroEmpleado) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
		       "LOWER(p.email) LIKE LOWER(CONCAT('%', :termino, '%'))" +
		       ")")
		List<Profesor> buscarPorTermino(@Param("termino") String termino);

	@Query("SELECT p FROM Profesor p WHERE p.activo = true AND ("
			+ "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(p.apellidoP) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(p.apellidoM) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(p.numeroEmpleado) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(p.especialidad) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(p.curp) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(p.rfc) LIKE LOWER(CONCAT('%', :termino, '%'))" + ")")
	Page<Profesor> buscarPorTerminoPaginado(@Param("termino") String termino, Pageable pageable);
}