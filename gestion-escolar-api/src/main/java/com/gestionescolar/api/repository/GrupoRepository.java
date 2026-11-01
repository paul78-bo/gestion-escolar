package com.gestionescolar.api.repository;

import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.enums.Turno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	// ==========================================
	// BÚSQUEDAS BÁSICAS
	// ==========================================
	List<Grupo> findByActivoTrue();

	Page<Grupo> findByActivoTrue(Pageable pageable);

	List<Grupo> findByCicloId(Long cicloId);

	List<Grupo> findByTurno(Turno turno);

	List<Grupo> findByGrado(Integer grado);

	Optional<Grupo> findByNombreAndCicloId(String nombre, Long cicloId);

	boolean existsByNombreAndCicloId(String nombre, Long cicloId);

	// ==========================================
	// CONSULTAS POR CICLO
	// ==========================================
	@Query("SELECT g FROM Grupo g WHERE g.ciclo.id = :cicloId AND g.activo = true")
	List<Grupo> findActivosByCicloId(@Param("cicloId") Long cicloId);

	@Query("SELECT g FROM Grupo g WHERE g.ciclo.id = :cicloId AND g.grado = :grado AND g.activo = true")
	List<Grupo> findActivosByCicloIdAndGrado(@Param("cicloId") Long cicloId, @Param("grado") Integer grado);

	// ==========================================
	// CONTADORES
	// ==========================================
	@Query("SELECT SUM(g.inscritos) FROM Grupo g WHERE g.ciclo.id = :cicloId AND g.activo = true")
	Long countInscritosByCicloId(@Param("cicloId") Long cicloId);

	// ==========================================
	// 🔍 BÚSQUEDA POR TÉRMINO
	// ==========================================
	@Query("SELECT g FROM Grupo g " + "LEFT JOIN g.ciclo c " + "WHERE g.activo = true AND ("
			+ "LOWER(CONCAT(g.nombre, ' ', g.grado, ' ', g.letra)) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(g.turno) LIKE LOWER(CONCAT('%', :termino, '%')) OR "
			+ "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))" + ")")
	List<Grupo> buscarPorTermino(@Param("termino") String termino);
}