package com.medcore.backend.api.turnos;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "turnos")
public class Turno {
    private static final ZoneId ZONA = ZoneId.of("America/Argentina/Buenos_Aires");
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="paciente_id", nullable=false) private Long pacienteId;
    @Column(name="profesional_id", nullable=false) private Long profesionalId;
    @Column(name="fecha_hora", nullable=false) private LocalDateTime fechaHora;
    @Column(name="duracion_minutos", nullable=false) private Integer duracionMinutos;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private EstadoTurno estado;
    @Column(name="fecha_creacion", nullable=false) private LocalDateTime fechaCreacion;

    @PrePersist void init() { if (estado == null) estado = EstadoTurno.PROGRAMADO; if (fechaCreacion == null) fechaCreacion = LocalDateTime.now(ZONA); }
    public Long getId() { return id; } public void setId(Long id) { this.id=id; }
    public Long getPacienteId() { return pacienteId; } public void setPacienteId(Long v) { pacienteId=v; }
    public Long getProfesionalId() { return profesionalId; } public void setProfesionalId(Long v) { profesionalId=v; }
    public LocalDateTime getFechaHora() { return fechaHora; } public void setFechaHora(LocalDateTime v) { fechaHora=v; }
    public Integer getDuracionMinutos() { return duracionMinutos; } public void setDuracionMinutos(Integer v) { duracionMinutos=v; }
    public EstadoTurno getEstado() { return estado; } public void setEstado(EstadoTurno v) { estado=v; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; } public void setFechaCreacion(LocalDateTime v) { fechaCreacion=v; }
}
