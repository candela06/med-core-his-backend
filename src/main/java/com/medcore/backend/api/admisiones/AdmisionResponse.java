package com.medcore.backend.api.admisiones;
import java.time.LocalDateTime;
public record AdmisionResponse(Long id, Long pacienteId, LocalDateTime fechaIngreso, TipoIngreso tipoIngreso,
                               String motivoConsulta, Long profesionalId, Long turnoId, EstadoAdmision estado) {
    public static AdmisionResponse from(Admision a){ return new AdmisionResponse(a.getId(),a.getPacienteId(),a.getFechaIngreso(),a.getTipoIngreso(),a.getMotivoConsulta(),a.getProfesionalId(),a.getTurnoId(),a.getEstado()); }
}
