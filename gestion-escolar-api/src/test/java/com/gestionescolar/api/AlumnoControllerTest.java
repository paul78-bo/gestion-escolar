package com.gestionescolar.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionescolar.api.controller.AlumnoController;
import com.gestionescolar.api.dto.AlumnoRequestDTO;
import com.gestionescolar.api.dto.AlumnoResponseDTO;
import com.gestionescolar.api.security.JwtService;
import com.gestionescolar.api.security.SecurityConfig;
import com.gestionescolar.api.service.AlumnoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;   // 🎯 IMPORT
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlumnoController.class)
@Import(SecurityConfig.class)
class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlumnoService alumnoService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private AlumnoRequestDTO crearRequestValido() {
        AlumnoRequestDTO request = new AlumnoRequestDTO();
        request.setNombre("Juan");
        request.setApellidoP("Pérez");
        request.setApellidoM("García");
        request.setCurp("PEGJ800515HCHRRR01");
        request.setEmail("juan@test.com");
        request.setTelefono("6141111111");
       //request.setDireccion("AV. TEST #100");
        request.setGenero(com.gestionescolar.api.enums.Genero.MASCULINO);
        request.setFechaNacimiento(LocalDate.of(2010, 5, 15));
        request.setEstadoCivil("SOLTERO");
        request.setNacionalidad("MEXICANA");
        request.setPaisNacimiento("MÉXICO");
        request.setEntidadNacimiento("CHIHUAHUA");
        //request.setLugarNacimiento("CHIHUAHUA");
        request.setMunicipio("CHIHUAHUA");
        request.setColonia("CENTRO");
        request.setCodigoPostal("31000");
        request.setCalle("AV. TEST");
        request.setNumeroExterior("100");
        request.setMatricula("A001");
        request.setTutorNombre("Tutor Test");
        request.setTutorTelefono("6141111111");
        request.setTutorEmail("tutor@test.com");
        return request;
    }

    // ==========================================
    // TEST 1: Crear alumno exitosamente (201)
    // ==========================================
    @Test
    @DisplayName("POST /api/alumnos debe crear un alumno y devolver 201")
    void debeCrearAlumno() throws Exception {
        AlumnoRequestDTO request = crearRequestValido();

        AlumnoResponseDTO response = new AlumnoResponseDTO();
        response.setId(1L);
        response.setNombre("Juan");
        response.setApellidoP("Pérez");
        response.setEmail("juan@test.com");
        response.setMatricula("A001");

        when(alumnoService.crear(any(AlumnoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/alumnos")
               
                .with(user("admin").roles("ADMIN"))   // 🎯 USER CON ROL ADMIN
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    // ==========================================
    // TEST 2: Datos inválidos (400)
    // ==========================================
    @Test
    @DisplayName("POST /api/alumnos debe devolver 400 si los datos son inválidos")
    void debeDevolver400ConDatosInvalidos() throws Exception {
        AlumnoRequestDTO request = new AlumnoRequestDTO();

        mockMvc.perform(post("/api/alumnos")
                
                .with(user("admin").roles("ADMIN"))   // 🎯 USER CON ROL ADMIN
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // ==========================================
    // TEST 3: Sin autenticación (401)
    // ==========================================
    @Test
    @DisplayName("POST /api/alumnos debe devolver 401 si no está autenticado")
    void debeDevolver401SinAutenticacion() throws Exception {
        AlumnoRequestDTO request = crearRequestValido();

        mockMvc.perform(post("/api/alumnos")
               
                // 🎯 SIN user() → sin autenticación
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    // ==========================================
    // TEST 4: Autenticado sin permisos (403)
    // ==========================================
    @Test
    @DisplayName("POST /api/alumnos debe devolver 403 si el usuario no tiene permisos")
    void debeDevolver403SinPermisos() throws Exception {
        AlumnoRequestDTO request = crearRequestValido();

        mockMvc.perform(post("/api/alumnos")
               
                .with(user("alumno").roles("ALUMNO"))   // 🎯 USER CON ROL ALUMNO (sin permiso)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }
}