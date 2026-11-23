package com.medcore.backend.api.pacientes;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteResponse(
        Long id,
        String documento,
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        Sexo sexo,
        String telefono,
        String correoElectronico,
        String domicilio,
        LocalDateTime fechaRegistro,
        EstadoPaciente estado
) {
    public static PacienteResponse from(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(),
                paciente.getDocumento(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getFechaNacimiento(),
                paciente.getSexo(),
                paciente.getTelefono(),
                paciente.getCorreoElectronico(),
                paciente.getDomicilio(),
                paciente.getFechaRegistro(),
                paciente.getEstado()
        );
    }
}
