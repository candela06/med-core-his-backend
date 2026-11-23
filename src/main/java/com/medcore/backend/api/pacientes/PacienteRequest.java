package com.medcore.backend.api.pacientes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PacienteRequest(
        @NotBlank @Size(max = 30) String documento,
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellido,
        @NotNull @PastOrPresent LocalDate fechaNacimiento,
        @NotNull Sexo sexo,
        @Size(max = 30) String telefono,
        @Email @Size(max = 150) String correoElectronico,
        @NotBlank @Size(max = 255) String domicilio
) {
}
