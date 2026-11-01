package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.BoletaResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Calificacion;
import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.entity.Inscripcion;
import com.gestionescolar.api.exception.AlumnoNoEncontradoException;
import com.gestionescolar.api.repository.AlumnoRepository;
import com.gestionescolar.api.repository.CalificacionRepository;
import com.gestionescolar.api.repository.InscripcionRepository;
import com.gestionescolar.api.service.BoletaService;
import com.gestionescolar.api.dto.CampoFormativoDTO;
import com.gestionescolar.api.dto.MateriaCalificacionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoletaServiceImpl implements BoletaService {

	private final AlumnoRepository alumnoRepository;
	private final InscripcionRepository inscripcionRepository;
	private final CalificacionRepository calificacionRepository;

	public BoletaServiceImpl(AlumnoRepository alumnoRepository, InscripcionRepository inscripcionRepository,
			CalificacionRepository calificacionRepository) {
		this.alumnoRepository = alumnoRepository;
		this.inscripcionRepository = inscripcionRepository;
		this.calificacionRepository = calificacionRepository;
	}

    @Override
    @Transactional(readOnly = true)
    public BoletaResponseDTO generarBoletaPorAlumno(Long alumnoId) {

        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new AlumnoNoEncontradoException(alumnoId));

        List<Inscripcion> inscripciones = inscripcionRepository.findActivasByAlumnoId(alumnoId);
        if (inscripciones.isEmpty()) {
            throw new RuntimeException("El alumno no tiene una inscripción activa");
        }

        Grupo grupo = inscripciones.get(0).getGrupo();
        List<Calificacion> todasCalificaciones = calificacionRepository.findCalificacionesConDetalles(alumnoId);

        Map<String, List<Calificacion>> califsPorMateria = todasCalificaciones.stream()
                .filter(c -> c.getAsignacion().getGrupo().getId().equals(grupo.getId()))
                .collect(Collectors.groupingBy(c -> c.getAsignacion().getMateria().getNombre()));

        String[] ordenOficial = {
                "Español", "Inglés", "Artes", "Matemáticas", "Biología", 
                "Geografía", "Historia", "Formación Cívica y Ética", "Tecnología", "Educación Física"
            };

        Map<String, CampoFormativoDTO> mapaCampos = new LinkedHashMap<>();
        BigDecimal sumaPromediosMaterias = BigDecimal.ZERO;
        boolean todasMateriasCalificadas = true; 

        // 👈 Variables para acumular las sumas de los trimestres
        double sumaT1 = 0.0, sumaT2 = 0.0, sumaT3 = 0.0;
        
        for (String nombreMateria : ordenOficial) {
            List<Calificacion> califs = califsPorMateria.getOrDefault(nombreMateria, new ArrayList<>());

            BigDecimal t1 = null, t2 = null, t3 = null;
            String codigo = "-";
            String campoNombre = "OTROS";

            for (Calificacion c : califs) {
                if (c.getPeriodo() != null && c.getCalificacion() != null) {
                    if (c.getPeriodo() == 1) t1 = c.getCalificacion();
                    if (c.getPeriodo() == 2) t2 = c.getCalificacion();
                    if (c.getPeriodo() == 3) t3 = c.getCalificacion();
                }
            }

            // 👈 AQUÍ SUMAMOS (Después de obtener los valores reales de t1, t2, t3)
            sumaT1 += (t1 != null) ? t1.doubleValue() : 0.0;
            sumaT2 += (t2 != null) ? t2.doubleValue() : 0.0;
            sumaT3 += (t3 != null) ? t3.doubleValue() : 0.0;

            // Si una materia no tiene calificación, la bandera se vuelve false
            if (t1 == null && t2 == null && t3 == null) {
                todasMateriasCalificadas = false;
            }

            if (nombreMateria.equals("Español") || nombreMateria.equals("Inglés") || nombreMateria.equals("Artes")) campoNombre = "LENGUAJES";
            if (nombreMateria.equals("Matemáticas") || nombreMateria.equals("Biología")) campoNombre = "SABERES_Y_PENSAMIENTO_CIENTIFICO";
            if (nombreMateria.equals("Geografía") || nombreMateria.equals("Historia") || nombreMateria.equals("Formación Cívica y Ética")) campoNombre = "ETICA_NATURALEZA_Y_SOCIEDADES";
            if (nombreMateria.equals("Tecnología") || nombreMateria.equals("Educación Física")) campoNombre = "DE_LO_HUMANO_Y_LO_COMUNITARIO";

            BigDecimal promedioMateria = null;
            if (t1 != null || t2 != null || t3 != null) {
                double suma = (t1 != null ? t1.doubleValue() : 0) + (t2 != null ? t2.doubleValue() : 0) + (t3 != null ? t3.doubleValue() : 0);
                int count = (t1 != null ? 1 : 0) + (t2 != null ? 1 : 0) + (t3 != null ? 1 : 0);
                promedioMateria = BigDecimal.valueOf(suma / count).setScale(1, RoundingMode.HALF_UP);
                sumaPromediosMaterias = sumaPromediosMaterias.add(promedioMateria);
            }

            MateriaCalificacionDTO dto = new MateriaCalificacionDTO();
            dto.setNombreMateria(nombreMateria);
            dto.setCodigoMateria(codigo);
            dto.setTrimestre1(t1);
            dto.setTrimestre2(t2);
            dto.setTrimestre3(t3);
            dto.setPromedioMateria(promedioMateria);

            CampoFormativoDTO campoDTO = mapaCampos.computeIfAbsent(campoNombre, k -> {
                CampoFormativoDTO nuevo = new CampoFormativoDTO();
                nuevo.setNombre(k);
                nuevo.setMaterias(new ArrayList<>());
                return nuevo;
            });
            campoDTO.getMaterias().add(dto);
        }

        // CÁLCULO FINAL DEL PROMEDIO GENERAL (Solo si todas están calificadas)
        BigDecimal promedioFinal = null; 
        if (todasMateriasCalificadas) {
            promedioFinal = sumaPromediosMaterias.divide(new BigDecimal(ordenOficial.length), 1, RoundingMode.HALF_UP);
        }

        // 👈 CÁLCULO DE PROMEDIOS POR TRIMESTRE (Solo si el trimestre tiene notas)
        BigDecimal promedioT1 = (sumaT1 > 0) ? BigDecimal.valueOf(sumaT1 / ordenOficial.length).setScale(1, RoundingMode.HALF_UP) : null;
        BigDecimal promedioT2 = (sumaT2 > 0) ? BigDecimal.valueOf(sumaT2 / ordenOficial.length).setScale(1, RoundingMode.HALF_UP) : null;
        BigDecimal promedioT3 = (sumaT3 > 0) ? BigDecimal.valueOf(sumaT3 / ordenOficial.length).setScale(1, RoundingMode.HALF_UP) : null;

        // ARMAMOS EL DTO (Solo una vez)
        BoletaResponseDTO boleta = new BoletaResponseDTO();
        boleta.setAlumnoId(alumno.getId());
        boleta.setMatricula(alumno.getMatricula());
        boleta.setNombreCompleto(alumno.getNombre() + " " + alumno.getApellidoP() + (alumno.getApellidoM() != null ? " " + alumno.getApellidoM() : ""));
        boleta.setGrupoNombre(grupo.getNombre());
        boleta.setCicloNombre(grupo.getCiclo().getNombre());
        
        // Seteamos los promedios
        boleta.setPromedioT1(promedioT1);
        boleta.setPromedioT2(promedioT2);
        boleta.setPromedioT3(promedioT3);
        boleta.setPromedioFinal(promedioFinal); 
        
        List<CampoFormativoDTO> listaFinal = new ArrayList<>();
        String[] ordenCampos = {"LENGUAJES", "SABERES_Y_PENSAMIENTO_CIENTIFICO", "ETICA_NATURALEZA_Y_SOCIEDADES", "DE_LO_HUMANO_Y_LO_COMUNITARIO"};
        for (String c : ordenCampos) {
            if (mapaCampos.containsKey(c)) listaFinal.add(mapaCampos.get(c));
        }
        boleta.setCamposFormativos(listaFinal);

        return boleta;
    }
}