package com.gestionescolar.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestionescolar.api.enums.Turno;

import jakarta.persistence.*;

@Entity
@Table(
    name = "grupos",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"nombre", "ciclo_id"},
        name = "uk_grupo_ciclo"
    )
)
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nombre; // Ejemplo: "1A", "2B"

    @Column(nullable = false)
    private Integer grado; // 1-6

    @Column(nullable = false, length = 5)
    private String letra; // A, B, C

    @Enumerated(EnumType.STRING)
    private Turno turno;

    @Column(nullable = false)
    private Integer capacidad = 30;

    @Column(nullable = false)
    private Integer inscritos = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciclo_id", nullable = false)
    private CicloEscolar ciclo;

    // ==========================================
    // RELACIONES
    // ==========================================

    @OneToMany(
        mappedBy = "grupo",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<Asignacion> asignaciones = new ArrayList<>();

    @OneToMany(
        mappedBy = "grupo",
        fetch = FetchType.LAZY
    )
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // ==========================================
    // ESTADO
    // ==========================================

    @Column(nullable = false)
    private Boolean activo = true;

    // ==========================================
    // AUDITORÍA
    // ==========================================

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ==========================================
    // CICLO DE VIDA JPA
    // ==========================================

    @PrePersist
    public void prePersist() {

        LocalDateTime ahora = LocalDateTime.now();

        this.fechaCreacion = ahora;
        this.fechaActualizacion = ahora;

        if (this.activo == null) {
            this.activo = true;
        }

        if (this.inscritos == null) {
            this.inscritos = 0;
        }

        if (this.capacidad == null) {
            this.capacidad = 30;
        }

        // Generar nombre automáticamente
        if (this.nombre == null && this.grado != null && this.letra != null) {
            this.nombre = this.grado + this.letra;
        }
    }

    @PreUpdate
    public void preUpdate() {

        this.fechaActualizacion = LocalDateTime.now();

        // Mantener nombre sincronizado con grado + letra
        if (this.grado != null && this.letra != null) {
            this.nombre = this.grado + this.letra;
        }
    }

    // ==========================================
    // INSCRIPCIONES
    // ==========================================

    public void addInscripcion(Inscripcion inscripcion) {

        if (inscripcion != null && !inscripciones.contains(inscripcion)) {
            inscripciones.add(inscripcion);
            inscripcion.setGrupo(this);
        }
    }

    public void removeInscripcion(Inscripcion inscripcion) {

        if (inscripcion != null && inscripciones.remove(inscripcion)) {
            inscripcion.setGrupo(null);
        }
    }

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    public Grupo() {
    }

    public Grupo(Integer grado, String letra, Turno turno, CicloEscolar ciclo) {
        this.grado = grado;
        this.letra = letra;
        this.nombre = grado + letra;
        this.turno = turno;
        this.ciclo = ciclo;
        this.activo = true;
        this.inscritos = 0;
        this.capacidad = 30;
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getGrado() {
        return grado;
    }

    public void setGrado(Integer grado) {
        this.grado = grado;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getInscritos() {
        return inscritos;
    }

    public void setInscritos(Integer inscritos) {
        this.inscritos = inscritos;
    }

    public CicloEscolar getCiclo() {
        return ciclo;
    }

    public void setCiclo(CicloEscolar ciclo) {
        this.ciclo = ciclo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
}