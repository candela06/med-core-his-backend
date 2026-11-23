package com.medcore.backend.api.profesionales;

import java.time.LocalDateTime;

public record ProfesionalResponse(
        Long id,
        String nombre,
        String apellido,
        String matricula,
        EspecialidadProfesional especialidad,
        String telefono,
        String correoElectronico,
        EstadoProfesional estado,
        LocalDateTime fechaRegistro
) {
    public static ProfesionalResponse from(Profesional profesional) {
        return new ProfesionalResponse(
                profesional.getId(),
                profesional.getNombre(),
                profesional.getApellido(),
                profesional.getMatricula(),
                profesional.getEspecialidad(),
                profesional.getTelefono(),
                profesional.getCorreoElectronico(),
                profesional.getEstado(),
                profesional.getFechaRegistro()
        );
    }
}
