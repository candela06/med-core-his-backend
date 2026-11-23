package com.medcore.backend.api.profesionales;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfesionalRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellido,
        @NotBlank @Size(max = 50) String matricula,
        @NotNull EspecialidadProfesional especialidad,
        @Size(max = 30) String telefono,
        @Email @Size(max = 150) String correoElectronico
) {
}
