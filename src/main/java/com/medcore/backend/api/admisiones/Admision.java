package com.medcore.backend.api.admisiones;

import jakarta.persistence.*;
import java.time.*;

@Entity @Table(name="admisiones")
public class Admision {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="paciente_id",nullable=false) private Long pacienteId;
    @Column(name="fecha_ingreso",nullable=false) private LocalDateTime fechaIngreso;
    @Enumerated(EnumType.STRING) @Column(name="tipo_ingreso",nullable=false) private TipoIngreso tipoIngreso;
    @Column(name="motivo_consulta",nullable=false,length=500) private String motivoConsulta;
    @Column(name="profesional_id") private Long profesionalId;
    @Column(name="turno_id") private Long turnoId;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private EstadoAdmision estado;

    @PrePersist void init(){ if(fechaIngreso==null) fechaIngreso=LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")); if(estado==null) estado=EstadoAdmision.EN_ESPERA; }
    public Long getId(){return id;} public void setId(Long v){id=v;} public Long getPacienteId(){return pacienteId;} public void setPacienteId(Long v){pacienteId=v;}
    public LocalDateTime getFechaIngreso(){return fechaIngreso;} public void setFechaIngreso(LocalDateTime v){fechaIngreso=v;} public TipoIngreso getTipoIngreso(){return tipoIngreso;} public void setTipoIngreso(TipoIngreso v){tipoIngreso=v;}
    public String getMotivoConsulta(){return motivoConsulta;} public void setMotivoConsulta(String v){motivoConsulta=v;} public Long getProfesionalId(){return profesionalId;} public void setProfesionalId(Long v){profesionalId=v;}
    public Long getTurnoId(){return turnoId;} public void setTurnoId(Long v){turnoId=v;} public EstadoAdmision getEstado(){return estado;} public void setEstado(EstadoAdmision v){estado=v;}
}
