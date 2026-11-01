package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.ProfesorRequestDTO;
import com.gestionescolar.api.dto.ProfesorResponseDTO;
import com.gestionescolar.api.entity.Contador;
import com.gestionescolar.api.entity.Profesor;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.exception.ProfesorNoEncontradoException;
import com.gestionescolar.api.mapper.ProfesorMapper;
import com.gestionescolar.api.repository.ContadorRepository;
import com.gestionescolar.api.repository.ProfesorRepository;
import com.gestionescolar.api.repository.UsuarioRepository;
import com.gestionescolar.api.service.ProfesorService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfesorServiceImpl implements ProfesorService {

	private static final String CONTADOR_PROFESOR = "profesor_numero_empleado";

	private final ProfesorRepository profesorRepository;
	private final ProfesorMapper profesorMapper;
	private final UsuarioRepository usuarioRepository;
	private final ContadorRepository contadorRepository;

	public ProfesorServiceImpl(ProfesorRepository profesorRepository, ProfesorMapper profesorMapper,
			UsuarioRepository usuarioRepository, ContadorRepository contadorRepository) {
		this.profesorRepository = profesorRepository;
		this.profesorMapper = profesorMapper;
		this.usuarioRepository = usuarioRepository;
		this.contadorRepository = contadorRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfesorResponseDTO> listarTodos() {
		return profesorRepository.findAll().stream().map(profesorMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfesorResponseDTO> listarActivos() {
		return profesorRepository.findByActivoTrue().stream().map(profesorMapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProfesorResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Profesor> paginaProfesores = profesorRepository.findByActivoTrue(pageable);
		return paginaProfesores.map(profesorMapper::toResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public ProfesorResponseDTO obtenerPorId(Long id) {
		Profesor p = profesorRepository.findById(id).orElseThrow(() -> new ProfesorNoEncontradoException(id));
		return profesorMapper.toResponseDTO(p);
	}

	@Override
	public ProfesorResponseDTO crear(ProfesorRequestDTO dto) {
		// Validaciones de unicidad
		if (profesorRepository.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Ya existe un profesor con el email: " + dto.getEmail());
		}
		if (profesorRepository.existsByCurp(dto.getCurp())) {
			throw new RuntimeException("Ya existe un profesor con la CURP: " + dto.getCurp());
		}
		if (profesorRepository.existsByRfc(dto.getRfc())) {
			throw new RuntimeException("Ya existe un profesor con el RFC: " + dto.getRfc());
		}
		if (profesorRepository.existsByNss(dto.getNss())) {
			throw new RuntimeException("Ya existe un profesor con el NSS: " + dto.getNss());
		}

		// Cargar usuario (si viene)
		Usuario usuario = null;
		if (dto.getUsuarioId() != null) {
			usuario = usuarioRepository.findById(dto.getUsuarioId())
					.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		}

		// Crear entidad
		Profesor p = profesorMapper.toEntity(dto, usuario);

		// 🔥 AUTO-GENERAR número de empleado
		p.setNumeroEmpleado(generarNumeroEmpleado());

		Profesor guardado = profesorRepository.save(p);
		return profesorMapper.toResponseDTO(guardado);
	}

	@Override
	public ProfesorResponseDTO actualizar(Long id, ProfesorRequestDTO dto) {
		Profesor p = profesorRepository.findById(id).orElseThrow(() -> new ProfesorNoEncontradoException(id));

		// Validar email único (si cambió)
		if (!p.getEmail().equals(dto.getEmail()) && profesorRepository.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Ya existe un profesor con el email: " + dto.getEmail());
		}

		// Validar CURP única (si cambió)
		if (!p.getCurp().equals(dto.getCurp()) && profesorRepository.existsByCurp(dto.getCurp())) {
			throw new RuntimeException("Ya existe un profesor con la CURP: " + dto.getCurp());
		}

		// Validar RFC único (si cambió)
		if (!p.getRfc().equals(dto.getRfc()) && profesorRepository.existsByRfc(dto.getRfc())) {
			throw new RuntimeException("Ya existe un profesor con el RFC: " + dto.getRfc());
		}

		// Validar NSS único (si cambió)
		if (!p.getNss().equals(dto.getNss()) && profesorRepository.existsByNss(dto.getNss())) {
			throw new RuntimeException("Ya existe un profesor con el NSS: " + dto.getNss());
		}

		// ⚠️ numeroEmpleado NO se actualiza (es inmutable)
		profesorMapper.updateEntity(p, dto);

		Profesor actualizado = profesorRepository.save(p);
		return profesorMapper.toResponseDTO(actualizado);
	}

	@Override
	public void eliminar(Long id) {
		Profesor p = profesorRepository.findById(id).orElseThrow(() -> new ProfesorNoEncontradoException(id));
		p.setActivo(false);
		profesorRepository.save(p);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfesorResponseDTO> buscarPorTermino(String termino) {
		return profesorRepository.buscarPorTermino(termino).stream().map(profesorMapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProfesorResponseDTO> buscarPaginado(String termino, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
		return profesorRepository.buscarPorTerminoPaginado(termino, pageable).map(profesorMapper::toResponseDTO);
	}

	// ==========================================
	// 🔥 MÉTODO PRIVADO: Generar número de empleado
	// Formato: PROF-2026-0001, PROF-2026-0002, ...
	// Reinicia cada año
	// ==========================================
	private String generarNumeroEmpleado() {
		int anioActual = LocalDate.now().getYear();

		// Obtener o crear el contador
		Contador contador = contadorRepository.findByNombre(CONTADOR_PROFESOR).orElseGet(() -> {
			Contador nuevo = new Contador(CONTADOR_PROFESOR);
			return contadorRepository.save(nuevo);
		});

		// Incrementar el contador
		long nuevoValor = contador.getValor() + 1;
		contador.setValor(nuevoValor);
		contadorRepository.save(contador);

		// Formatear: PROF-2026-0001
		return String.format("PROF-%d-%04d", anioActual, nuevoValor);
	}
}