//package com.gestionescolar.api.config;
//
//import com.gestionescolar.api.entity.*;
//import com.gestionescolar.api.enums.*;
//import com.gestionescolar.api.repository.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
////@Configuration
//public class DataInitializer {
//
//	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
//
//	//@Bean
//	public CommandLineRunner initData(UsuarioRepository usuarioRepository, ProfesorRepository profesorRepository,
//			AlumnoRepository alumnoRepository, MateriaRepository materiaRepository,
//			CicloEscolarRepository cicloRepository, GrupoRepository grupoRepository,
//			AsignacionRepository asignacionRepository, InscripcionRepository inscripcionRepository, // 👈 AGREGAR
//			CalificacionRepository calificacionRepository, // 👈 AGREGAR
//			PasswordEncoder passwordEncoder) {
//
//		return args -> {
//			log.info("========================================================");
//			log.info("🚀 INICIANDO CARGA DE DATOS DE PRUEBA");
//			log.info("========================================================");
//
//			// ==========================================
//			// 1. CREAR USUARIO ADMIN
//			// ==========================================
//			Usuario admin;
//			if (!usuarioRepository.existsByUsername("admin")) {
//				admin = new Usuario();
//				admin.setUsername("admin");
//				admin.setPassword(passwordEncoder.encode("admin123"));
//				admin.setEmail("admin@gestionescolar.com");
//				admin.setRol(Rol.ADMIN);
//				admin.setActivo(true);
//				usuarioRepository.save(admin);
//				log.info("✅ Admin creado: admin / admin123");
//			} else {
//				admin = usuarioRepository.findByUsername("admin").orElseThrow();
//				log.info("ℹ️ Admin ya existía");
//			}
//
//			// ==========================================
//			// 2. CREAR USUARIOS DE PROFESORES
//			// ==========================================
//			Usuario userProf1 = null;
//			if (!usuarioRepository.existsByUsername("juan.perez")) {
//				userProf1 = new Usuario();
//				userProf1.setUsername("juan.perez");
//				userProf1.setPassword(passwordEncoder.encode("profesor123"));
//				userProf1.setEmail("juan.perez@gestionescolar.com");
//				userProf1.setRol(Rol.PROFESOR);
//				userProf1.setActivo(true);
//				userProf1 = usuarioRepository.save(userProf1);
//				log.info("✅ Usuario profesor creado: juan.perez / profesor123");
//			} else {
//				userProf1 = usuarioRepository.findByUsername("juan.perez").orElseThrow();
//			}
//
//			Usuario userProf2 = null;
//			if (!usuarioRepository.existsByUsername("maria.garcia")) {
//				userProf2 = new Usuario();
//				userProf2.setUsername("maria.garcia");
//				userProf2.setPassword(passwordEncoder.encode("profesor123"));
//				userProf2.setEmail("maria.garcia@gestionescolar.com");
//				userProf2.setRol(Rol.PROFESOR);
//				userProf2.setActivo(true);
//				userProf2 = usuarioRepository.save(userProf2);
//				log.info("✅ Usuario profesor creado: maria.garcia / profesor123");
//			} else {
//				userProf2 = usuarioRepository.findByUsername("maria.garcia").orElseThrow();
//			}
//
//			// ==========================================
//			// 3. CREAR USUARIOS DE ALUMNOS
//			// ==========================================
//			Usuario userAlum1 = null;
//			if (!usuarioRepository.existsByUsername("carlos.ramirez")) {
//				userAlum1 = new Usuario();
//				userAlum1.setUsername("carlos.ramirez");
//				userAlum1.setPassword(passwordEncoder.encode("alumno123"));
//				userAlum1.setEmail("carlos.ramirez@gestionescolar.com");
//				userAlum1.setRol(Rol.ALUMNO);
//				userAlum1.setActivo(true);
//				userAlum1 = usuarioRepository.save(userAlum1);
//				log.info("✅ Usuario alumno creado: carlos.ramirez / alumno123");
//			} else {
//				userAlum1 = usuarioRepository.findByUsername("carlos.ramirez").orElseThrow();
//			}
//
//			Usuario userAlum2 = null;
//			if (!usuarioRepository.existsByUsername("sofia.hernandez")) {
//				userAlum2 = new Usuario();
//				userAlum2.setUsername("sofia.hernandez");
//				userAlum2.setPassword(passwordEncoder.encode("alumno123"));
//				userAlum2.setEmail("sofia.hernandez@gestionescolar.com");
//				userAlum2.setRol(Rol.ALUMNO);
//				userAlum2.setActivo(true);
//				userAlum2 = usuarioRepository.save(userAlum2);
//				log.info("✅ Usuario alumno creado: sofia.hernandez / alumno123");
//			} else {
//				userAlum2 = usuarioRepository.findByUsername("sofia.hernandez").orElseThrow();
//			}
//
//			Usuario userAlum3 = null;
//			if (!usuarioRepository.existsByUsername("miguel.torres")) {
//				userAlum3 = new Usuario();
//				userAlum3.setUsername("miguel.torres");
//				userAlum3.setPassword(passwordEncoder.encode("alumno123"));
//				userAlum3.setEmail("miguel.torres@gestionescolar.com");
//				userAlum3.setRol(Rol.ALUMNO);
//				userAlum3.setActivo(true);
//				userAlum3 = usuarioRepository.save(userAlum3);
//				log.info("✅ Usuario alumno creado: miguel.torres / alumno123");
//			} else {
//				userAlum3 = usuarioRepository.findByUsername("miguel.torres").orElseThrow();
//			}
//
//			// ==========================================
//			// 4. CREAR PROFESORES
//			// ==========================================
//			Profesor prof1 = null;
//			if (!profesorRepository.existsByNumeroEmpleado("PROF001")) {
//				prof1 = new Profesor();
//				prof1.setNombre("Juan");
//				prof1.setApellidoP("Pérez");
//				prof1.setApellidoM("García");
//				prof1.setEmail("juan.perez@gestionescolar.com");
//				prof1.setTelefono("6141234567");
//				prof1.setNumeroEmpleado("PROF001");
//				prof1.setEspecialidad("Matemáticas");
//				prof1.setGradoAcademico(GradoAcademico.MAESTRIA);
//				prof1.setFechaIngreso(LocalDate.of(2020, 8, 15));
//				prof1.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
//				prof1.setSalario(new BigDecimal("15000.00"));
//				prof1.setGenero(Genero.MASCULINO);
//				prof1.setEstatus(EstatusProfesor.ACTIVO);
//				prof1.setUsuario(userProf1);
//				prof1.setActivo(true);
//				prof1 = profesorRepository.save(prof1);
//				log.info("✅ Profesor creado: {}", prof1.getNombre() + " " + prof1.getApellidoP());
//			} else {
//				prof1 = profesorRepository.findByNumeroEmpleado("PROF001").orElseThrow();
//			}
//
//			Profesor prof2 = null;
//			if (!profesorRepository.existsByNumeroEmpleado("PROF002")) {
//				prof2 = new Profesor();
//				prof2.setNombre("María");
//				prof2.setApellidoP("García");
//				prof2.setApellidoM("López");
//				prof2.setEmail("maria.garcia@gestionescolar.com");
//				prof2.setTelefono("6149876543");
//				prof2.setNumeroEmpleado("PROF002");
//				prof2.setEspecialidad("Literatura");
//				prof2.setGradoAcademico(GradoAcademico.LICENCIATURA);
//				prof2.setFechaIngreso(LocalDate.of(2021, 1, 10));
//				prof2.setTipoContrato(TipoContrato.MEDIO_TIEMPO);
//				prof2.setSalario(new BigDecimal("9000.00"));
//				prof2.setGenero(Genero.FEMENINO);
//				prof2.setEstatus(EstatusProfesor.ACTIVO);
//				prof2.setUsuario(userProf2);
//				prof2.setActivo(true);
//				prof2 = profesorRepository.save(prof2);
//				log.info("✅ Profesor creado: {}", prof2.getNombre() + " " + prof2.getApellidoP());
//			} else {
//				prof2 = profesorRepository.findByNumeroEmpleado("PROF002").orElseThrow();
//			}
//
//			// ==========================================
//			// 5. CREAR MATERIAS
//			// ==========================================
//			Materia matematicas = null;
//			if (!materiaRepository.existsByCodigo("MAT101")) {
//				matematicas = new Materia();
//				matematicas.setNombre("Matemáticas");
//				matematicas.setCodigo("MAT101");
//				matematicas.setCreditos(8);
//				matematicas.setHorasSemana(5);
//				matematicas.setArea(AreaConocimiento.MATEMATICAS);
//				matematicas.setDescripcion("Fundamentos de álgebra y aritmética");
//				matematicas.setActivo(true);
//				matematicas = materiaRepository.save(matematicas);
//				log.info("✅ Materia creada: {}", matematicas.getNombre());
//			} else {
//				matematicas = materiaRepository.findByCodigo("MAT101").orElseThrow();
//			}
//
//			Materia espanol = null;
//			if (!materiaRepository.existsByCodigo("ESP101")) {
//				espanol = new Materia();
//				espanol.setNombre("Español");
//				espanol.setCodigo("ESP101");
//				espanol.setCreditos(6);
//				espanol.setHorasSemana(4);
//				espanol.setArea(AreaConocimiento.LENGUAS);
//				espanol.setDescripcion("Comprensión lectora y redacción");
//				espanol.setActivo(true);
//				espanol = materiaRepository.save(espanol);
//				log.info("✅ Materia creada: {}", espanol.getNombre());
//			} else {
//				espanol = materiaRepository.findByCodigo("ESP101").orElseThrow();
//			}
//
//			Materia ingles = null;
//			if (!materiaRepository.existsByCodigo("ING101")) {
//				ingles = new Materia();
//				ingles.setNombre("Inglés");
//				ingles.setCodigo("ING101");
//				ingles.setCreditos(6);
//				ingles.setHorasSemana(4);
//				ingles.setArea(AreaConocimiento.LENGUAS);
//				ingles.setDescripcion("Inglés básico");
//				ingles.setActivo(true);
//				ingles = materiaRepository.save(ingles);
//				log.info("✅ Materia creada: {}", ingles.getNombre());
//			} else {
//				ingles = materiaRepository.findByCodigo("ING101").orElseThrow();
//			}
//
//			Materia fisica = null;
//			if (!materiaRepository.existsByCodigo("FIS101")) {
//				fisica = new Materia();
//				fisica.setNombre("Física");
//				fisica.setCodigo("FIS101");
//				fisica.setCreditos(8);
//				fisica.setHorasSemana(5);
//				fisica.setArea(AreaConocimiento.CIENCIAS_BASICAS);
//				fisica.setDescripcion("Fundamentos de mecánica");
//				fisica.setActivo(true);
//				fisica = materiaRepository.save(fisica);
//				log.info("✅ Materia creada: {}", fisica.getNombre());
//			} else {
//				fisica = materiaRepository.findByCodigo("FIS101").orElseThrow();
//			}
//
//			// ==========================================
//			// 6. CREAR CICLO ESCOLAR
//			// ==========================================
//			CicloEscolar ciclo2026 = null;
//			if (!cicloRepository.existsByNombre("2026-2027")) {
//				ciclo2026 = new CicloEscolar();
//				ciclo2026.setNombre("2026-2027");
//				ciclo2026.setFechaInicio(LocalDate.of(2026, 8, 1));
//				ciclo2026.setFechaFin(LocalDate.of(2027, 6, 30));
//				ciclo2026.setActivo(true);
//				ciclo2026 = cicloRepository.save(ciclo2026);
//				log.info("✅ Ciclo escolar creado: {}", ciclo2026.getNombre());
//			} else {
//				ciclo2026 = cicloRepository.findByNombre("2026-2027").orElseThrow();
//			}
//
//			// ==========================================
//			// 7. CREAR GRUPOS
//			// ==========================================
//			Grupo grupo1A = null;
//			if (!grupoRepository.existsByNombreAndCicloId("1A", ciclo2026.getId())) {
//				grupo1A = new Grupo();
//				grupo1A.setGrado(1);
//				grupo1A.setLetra("A");
//				grupo1A.setNombre("1A");
//				grupo1A.setTurno(Turno.MATUTINO);
//				grupo1A.setCapacidad(30);
//				grupo1A.setInscritos(0);
//				grupo1A.setCiclo(ciclo2026);
//				grupo1A.setActivo(true);
//				grupo1A = grupoRepository.save(grupo1A);
//				log.info("✅ Grupo creado: {}", grupo1A.getNombre());
//			} else {
//				grupo1A = grupoRepository.findByNombreAndCicloId("1A", ciclo2026.getId()).orElseThrow();
//			}
//
//			Grupo grupo1B = null;
//			if (!grupoRepository.existsByNombreAndCicloId("1B", ciclo2026.getId())) {
//				grupo1B = new Grupo();
//				grupo1B.setGrado(1);
//				grupo1B.setLetra("B");
//				grupo1B.setNombre("1B");
//				grupo1B.setTurno(Turno.VESPERTINO);
//				grupo1B.setCapacidad(30);
//				grupo1B.setInscritos(0);
//				grupo1B.setCiclo(ciclo2026);
//				grupo1B.setActivo(true);
//				grupo1B = grupoRepository.save(grupo1B);
//				log.info("✅ Grupo creado: {}", grupo1B.getNombre());
//			} else {
//				grupo1B = grupoRepository.findByNombreAndCicloId("1B", ciclo2026.getId()).orElseThrow();
//			}
//
//			// ==========================================
//			// 8. CREAR ASIGNACIONES (Materia + Profesor + Grupo)
//			// ==========================================
//
//			// Asignaciones para 1A
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1A, matematicas, prof1, "A-101",
//					"Lunes y Miércoles 8:00-10:00", log);
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1A, espanol, prof2, "A-102",
//					"Martes y Jueves 8:00-10:00", log);
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1A, ingles, prof2, "A-103", "Viernes 8:00-11:00", log);
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1A, fisica, prof1, "LAB-1",
//					"Lunes y Miércoles 10:00-12:00", log);
//
//			// Asignaciones para 1B
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1B, matematicas, prof1, "B-101",
//					"Lunes y Miércoles 14:00-16:00", log);
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1B, espanol, prof2, "B-102",
//					"Martes y Jueves 14:00-16:00", log);
//			crearAsignacionSiNoExiste(asignacionRepository, grupo1B, ingles, prof2, "B-103", "Viernes 14:00-17:00",
//					log);
//
//			// ==========================================
//			// 9. CREAR ALUMNOS
//			// ==========================================
//			if (!alumnoRepository.existsByMatricula("A001")) {
//				Alumno alumno1 = new Alumno();
//				alumno1.setNombre("Carlos");
//				alumno1.setApellidoP("Ramírez");
//				alumno1.setApellidoM("López");
//				alumno1.setEmail("carlos.ramirez@gestionescolar.com");
//				alumno1.setMatricula("A001");
//				alumno1.setTelefono("6141111111");
//				alumno1.setGenero(Genero.MASCULINO);
//				alumno1.setFechaNacimiento(LocalDate.of(2010, 5, 15));
//				alumno1.setFechaIngreso(LocalDate.of(2026, 8, 1));
//				alumno1.setEstatus(EstatusAlumno.ACTIVO);
//				alumno1.setTutorNombre("Roberto Ramírez");
//				alumno1.setTutorTelefono("6141110000");
//				alumno1.setTutorEmail("roberto.ramirez@example.com");
//				alumno1.setUsuario(userAlum1);
//				alumno1.setActivo(true);
//				alumnoRepository.save(alumno1);
//				log.info("✅ Alumno creado: Carlos Ramírez (A001)");
//			}
//
//			if (!alumnoRepository.existsByMatricula("A002")) {
//				Alumno alumno2 = new Alumno();
//				alumno2.setNombre("Sofía");
//				alumno2.setApellidoP("Hernández");
//				alumno2.setApellidoM("Ruiz");
//				alumno2.setEmail("sofia.hernandez@gestionescolar.com");
//				alumno2.setMatricula("A002");
//				alumno2.setTelefono("6142222222");
//				alumno2.setGenero(Genero.FEMENINO);
//				alumno2.setFechaNacimiento(LocalDate.of(2010, 8, 22));
//				alumno2.setFechaIngreso(LocalDate.of(2026, 8, 1));
//				alumno2.setEstatus(EstatusAlumno.ACTIVO);
//				alumno2.setTutorNombre("Ana Ruiz");
//				alumno2.setTutorTelefono("6142220000");
//				alumno2.setTutorEmail("ana.ruiz@example.com");
//				alumno2.setUsuario(userAlum2);
//				alumno2.setActivo(true);
//				alumnoRepository.save(alumno2);
//				log.info("✅ Alumno creado: Sofía Hernández (A002)");
//			}
//
//			if (!alumnoRepository.existsByMatricula("A003")) {
//				Alumno alumno3 = new Alumno();
//				alumno3.setNombre("Miguel");
//				alumno3.setApellidoP("Torres");
//				alumno3.setApellidoM("Sánchez");
//				alumno3.setEmail("miguel.torres@gestionescolar.com");
//				alumno3.setMatricula("A003");
//				alumno3.setTelefono("6143333333");
//				alumno3.setGenero(Genero.MASCULINO);
//				alumno3.setFechaNacimiento(LocalDate.of(2010, 3, 10));
//				alumno3.setFechaIngreso(LocalDate.of(2026, 8, 1));
//				alumno3.setEstatus(EstatusAlumno.ACTIVO);
//				alumno3.setTutorNombre("Laura Sánchez");
//				alumno3.setTutorTelefono("6143330000");
//				alumno3.setTutorEmail("laura.sanchez@example.com");
//				alumno3.setUsuario(userAlum3);
//				alumno3.setActivo(true);
//				alumnoRepository.save(alumno3);
//				log.info("✅ Alumno creado: Miguel Torres (A003)");
//			}
//
//			// ==========================================
//			// 10. CREAR INSCRIPCIONES
//			// ==========================================
//			log.info("--- Creando inscripciones ---");
//
//			// Recargar alumnos (para tenerlos disponibles con id)
//			Alumno carlos = alumnoRepository.findByMatricula("A001").orElse(null);
//			Alumno sofia = alumnoRepository.findByMatricula("A002").orElse(null);
//			Alumno miguel = alumnoRepository.findByMatricula("A003").orElse(null);
//
//			// Inscribir a Carlos en 1A
//			if (carlos != null && !inscripcionRepository.existsByAlumnoIdAndGrupoId(carlos.getId(), grupo1A.getId())) {
//				Inscripcion insc = new Inscripcion();
//				insc.setAlumno(carlos);
//				insc.setGrupo(grupo1A);
//				insc.setFechaInscripcion(LocalDate.of(2026, 8, 1));
//				insc.setEstatus(EstatusInscripcion.ACTIVA);
//				insc.setActivo(true);
//				inscripcionRepository.save(insc);
//
//				// Actualizar contador
//				grupo1A.setInscritos(grupo1A.getInscritos() + 1);
//				grupoRepository.save(grupo1A);
//
//				log.info("✅ Carlos Ramírez inscrito en 1A");
//
//				// Crear calificaciones para todas las asignaciones de 1A
//				crearCalificacionesParaAlumno(calificacionRepository, asignacionRepository, carlos, grupo1A, log);
//			}
//
//			// Inscribir a Sofía en 1A
//			if (sofia != null && !inscripcionRepository.existsByAlumnoIdAndGrupoId(sofia.getId(), grupo1A.getId())) {
//				Inscripcion insc = new Inscripcion();
//				insc.setAlumno(sofia);
//				insc.setGrupo(grupo1A);
//				insc.setFechaInscripcion(LocalDate.of(2026, 8, 1));
//				insc.setEstatus(EstatusInscripcion.ACTIVA);
//				insc.setActivo(true);
//				inscripcionRepository.save(insc);
//
//				grupo1A.setInscritos(grupo1A.getInscritos() + 1);
//				grupoRepository.save(grupo1A);
//
//				log.info("✅ Sofía Hernández inscrita en 1A");
//
//				crearCalificacionesParaAlumno(calificacionRepository, asignacionRepository, sofia, grupo1A, log);
//			}
//
//			// Inscribir a Miguel en 1B
//			if (miguel != null && !inscripcionRepository.existsByAlumnoIdAndGrupoId(miguel.getId(), grupo1B.getId())) {
//				Inscripcion insc = new Inscripcion();
//				insc.setAlumno(miguel);
//				insc.setGrupo(grupo1B);
//				insc.setFechaInscripcion(LocalDate.of(2026, 8, 1));
//				insc.setEstatus(EstatusInscripcion.ACTIVA);
//				insc.setActivo(true);
//				inscripcionRepository.save(insc);
//
//				grupo1B.setInscritos(grupo1B.getInscritos() + 1);
//				grupoRepository.save(grupo1B);
//
//				log.info("✅ Miguel Torres inscrito en 1B");
//
//				crearCalificacionesParaAlumno(calificacionRepository, asignacionRepository, miguel, grupo1B, log);
//			}
//
//			// ==========================================
//			// CREAR USUARIO DIRECTOR
//			// ==========================================
//			if (!usuarioRepository.existsByUsername("director")) {
//				Usuario director = new Usuario();
//				director.setUsername("director");
//				director.setPassword(passwordEncoder.encode("director123"));
//				director.setEmail("director@gestionescolar.com");
//				director.setRol(Rol.DIRECTOR);
//				director.setActivo(true);
//				usuarioRepository.save(director);
//				log.info("✅ Director creado: director / director123");
//			}
//
//			// ==========================================
//			// CREAR USUARIO SECRETARIO
//			// ==========================================
//			if (!usuarioRepository.existsByUsername("secretario")) {
//				Usuario secretario = new Usuario();
//				secretario.setUsername("secretario");
//				secretario.setPassword(passwordEncoder.encode("secretario123"));
//				secretario.setEmail("secretario@gestionescolar.com");
//				secretario.setRol(Rol.SECRETARIO);
//				secretario.setActivo(true);
//				usuarioRepository.save(secretario);
//				log.info("✅ Secretario creado: secretario / secretario123");
//			}
//
//			// ==========================================
//			// CREAR USUARIO INTENDENTE
//			// ==========================================
//			if (!usuarioRepository.existsByUsername("intendente")) {
//				Usuario intendente = new Usuario();
//				intendente.setUsername("intendente");
//				intendente.setPassword(passwordEncoder.encode("intendente123"));
//				intendente.setEmail("intendente@gestionescolar.com");
//				intendente.setRol(Rol.INTENDENTE);
//				intendente.setActivo(true);
//				usuarioRepository.save(intendente);
//				log.info("✅ Intendente creado: intendente / intendente123");
//			}
//
//			log.info("========================================================");
//			log.info("🎉 CARGA DE DATOS COMPLETADA");
//			log.info("========================================================");
//			log.info("📋 CREDENCIALES DE PRUEBA:");
//			log.info("   ADMIN:      admin / admin123");
//			log.info("   DIRECTOR:   director / director123");
//			log.info("   SECRETARIO: secretario / secretario123");
//			log.info("   INTENDENTE: intendente / intendente123");
//			log.info("   PROFESOR:   juan.perez / profesor123");
//			log.info("   PROFESOR:   maria.garcia / profesor123");
//			log.info("   ALUMNO:     carlos.ramirez / alumno123");
//			log.info("   ALUMNO:     sofia.hernandez / alumno123");
//			log.info("   ALUMNO:     miguel.torres / alumno123");
//		};
//	}
//
//	// ==========================================
//	// HELPER: crear asignación si no existe
//	// ==========================================
//	private void crearAsignacionSiNoExiste(AsignacionRepository repo, Grupo grupo, Materia materia, Profesor profesor,
//			String aula, String horario, Logger log) {
//		if (!repo.existsByGrupoIdAndMateriaId(grupo.getId(), materia.getId())) {
//			Asignacion a = new Asignacion();
//			a.setGrupo(grupo);
//			a.setMateria(materia);
//			a.setProfesor(profesor);
//			a.setAula(aula);
//			a.setHorario(horario);
//			a.setActivo(true);
//			repo.save(a);
//			log.info("✅ Asignación creada: {} → {} ({})", grupo.getNombre(), materia.getNombre(), profesor.getNombre());
//		}
//	}
//
//	// ==========================================
//	// HELPER: crear calificaciones para todas las asignaciones de un grupo
//	// ==========================================
//	private void crearCalificacionesParaAlumno(CalificacionRepository calificacionRepo,
//			AsignacionRepository asignacionRepo, Alumno alumno, Grupo grupo, Logger log) {
//		List<Asignacion> asignaciones = asignacionRepo.findActivasByGrupoId(grupo.getId());
//		for (Asignacion asignacion : asignaciones) {
//			if (!calificacionRepo.existsByAsignacionIdAndAlumnoId(asignacion.getId(), alumno.getId())) {
//				Calificacion c = new Calificacion();
//				c.setAsignacion(asignacion);
//				c.setAlumno(alumno);
//				c.setCalificacion(null); // Sin calificar aún
//				c.setObservaciones("Sin calificar");
//				c.setActivo(true);
//				calificacionRepo.save(c);
//				log.info("   ↳ Calificación creada: {} - {} (sin calificar)", alumno.getNombre(),
//						asignacion.getMateria().getNombre());
//			}
//		}
//
//	}
//}