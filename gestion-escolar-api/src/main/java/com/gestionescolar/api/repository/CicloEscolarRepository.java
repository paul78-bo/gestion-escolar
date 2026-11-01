package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.CicloEscolar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CicloEscolarRepository extends JpaRepository<CicloEscolar, Long> {

	Optional<CicloEscolar> findByNombre(String nombre);

	boolean existsByNombre(String nombre);

	List<CicloEscolar> findByActivoTrue();

	Page<CicloEscolar> findByActivoTrue(Pageable pageable);

	List<CicloEscolar> findByFechaInicioAfter(LocalDate fecha);

	List<CicloEscolar> findByFechaFinBefore(LocalDate fecha);

	@Query("SELECT c FROM CicloEscolar c WHERE c.activo = true ORDER BY c.fechaInicio DESC")
	List<CicloEscolar> findActivosOrderByFechaInicioDesc();

	@Query("SELECT c FROM CicloEscolar c WHERE c.activo = true AND ("
			+ "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))" + ")")
	List<CicloEscolar> buscarPorTermino(@Param("termino") String termino);
}