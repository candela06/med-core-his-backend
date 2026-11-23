package com.medcore.backend.api.turnos;

import java.time.LocalDateTime;

public record TurnoResponse(Long id, Long pacienteId, Long profesionalId, LocalDateTime fechaHora,
                            Integer duracionMinutos, EstadoTurno estado, LocalDateTime fechaCreacion) {
    public static TurnoResponse from(Turno turno) {
        return new TurnoResponse(turno.getId(), turno.getPacienteId(), turno.getProfesionalId(),
                turno.getFechaHora(), turno.getDuracionMinutos(), turno.getEstado(), turno.getFechaCreacion());
    }
}
