package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Materia;
import com.gestionescolar.api.enums.CampoFormativo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {

	Optional<Materia> findByCodigo(String codigo);

	boolean existsByCodigo(String codigo);

	boolean existsByNombre(String nombre);

	List<Materia> findByActivoTrue();

	Page<Materia> findByActivoTrue(Pageable pageable);

	List<Materia> findByCampoFormativo(CampoFormativo campoFormativo);
	
	@Query("SELECT m FROM Materia m WHERE m.activo = true AND ("
			+ "LOWER(CONCAT(m.nombre, ' ', m.codigo)) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(m.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))" + ")")
	List<Materia> buscarPorTermino(@Param("termino") String termino);
}