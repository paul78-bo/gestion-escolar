package com.gestionescolar.api;

import com.gestionescolar.api.dto.AlumnoRequestDTO;
import com.gestionescolar.api.dto.AlumnoResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.mapper.AlumnoMapper;
import com.gestionescolar.api.repository.AlumnoRepository;
import com.gestionescolar.api.repository.UsuarioRepository;
import com.gestionescolar.api.service.AlumnoServiceImpl.AlumnoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // ← Activa Mockito
class AlumnoServiceImplTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private AlumnoMapper alumnoMapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    private AlumnoRequestDTO requestDTO;
    private Alumno alumnoEntity;
    private AlumnoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Se ejecuta ANTES de cada test
        requestDTO = new AlumnoRequestDTO();
        requestDTO.setNombre("Juan");
        requestDTO.setApellidoP("Pérez");
        requestDTO.setApellidoM("García");
        requestDTO.setEmail("juan.perez@test.com");
        requestDTO.setMatricula("A999");

        alumnoEntity = new Alumno();
        alumnoEntity.setId(1L);
        alumnoEntity.setNombre("Juan");
        alumnoEntity.setApellidoP("Pérez");
        alumnoEntity.setApellidoM("García");
        alumnoEntity.setEmail("juan.perez@test.com");
        alumnoEntity.setMatricula("A999");

        responseDTO = new AlumnoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Juan");
        responseDTO.setApellidoP("Pérez");
        responseDTO.setApellidoM("García");
        responseDTO.setEmail("juan.perez@test.com");
        responseDTO.setMatricula("A999");
    }

    // ==========================================
    // TEST 1: Crear alumno exitosamente
    // ==========================================
    @Test
    @DisplayName("Debe crear un alumno correctamente")
    void debeCrearAlumno() {
        // ARRANGE (preparar)
        when(alumnoRepository.existsByEmail(anyString())).thenReturn(false);
        when(alumnoRepository.existsByMatricula(anyString())).thenReturn(false);
        when(alumnoMapper.toEntity(any(AlumnoRequestDTO.class), any())).thenReturn(alumnoEntity);
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoEntity);
        when(alumnoMapper.toResponseDTO(any(Alumno.class))).thenReturn(responseDTO);

        // ACT (ejecutar)
        AlumnoResponseDTO resultado = alumnoService.crear(requestDTO);

        // ASSERT (verificar)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getEmail()).isEqualTo("juan.perez@test.com");

        // Verificar que se llamaron los métodos esperados
        verify(alumnoRepository, times(1)).existsByEmail("juan.perez@test.com");
        verify(alumnoRepository, times(1)).existsByMatricula("A999");
        verify(alumnoRepository, times(1)).save(any(Alumno.class));
    }

    // ==========================================
    // TEST 2: Crear alumno con email duplicado
    // ==========================================
    @Test
    @DisplayName("Debe lanzar excepción si el email ya existe")
    void debeFallarSiEmailExiste() {
        // ARRANGE
        when(alumnoRepository.existsByEmail("juan.perez@test.com")).thenReturn(true);

        // ACT + ASSERT
        assertThatThrownBy(() -> alumnoService.crear(requestDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Ya existe un alumno con el email");

        // Verificar que NO se llamó a save
        verify(alumnoRepository, never()).save(any(Alumno.class));
    }

    // ==========================================
    // TEST 3: Crear alumno con matrícula duplicada
    // ==========================================
    @Test
    @DisplayName("Debe lanzar excepción si la matrícula ya existe")
    void debeFallarSiMatriculaExiste() {
        // ARRANGE
        when(alumnoRepository.existsByEmail(anyString())).thenReturn(false);
        when(alumnoRepository.existsByMatricula("A999")).thenReturn(true);

        // ACT + ASSERT
        assertThatThrownBy(() -> alumnoService.crear(requestDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Ya existe un alumno con la matrícula");

        verify(alumnoRepository, never()).save(any(Alumno.class));
    }
}