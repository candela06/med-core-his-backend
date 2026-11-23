package com.medcore.backend.api.profesionales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "profesionales")
public class Profesional {

    private static final ZoneId ZONA_MEDCORE = ZoneId.of("America/Argentina/Buenos_Aires");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EspecialidadProfesional especialidad;

    @Column(length = 30)
    private String telefono;

    @Column(name = "correo_electronico", length = 150)
    private String correoElectronico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoProfesional estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    void inicializar() {
        if (estado == null) estado = EstadoProfesional.ACTIVO;
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now(ZONA_MEDCORE);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public EspecialidadProfesional getEspecialidad() { return especialidad; }
    public void setEspecialidad(EspecialidadProfesional especialidad) { this.especialidad = especialidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public EstadoProfesional getEstado() { return estado; }
    public void setEstado(EstadoProfesional estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
