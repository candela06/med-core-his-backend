package com.medcore.backend.api.pacientes;

import jakarta.validation.constraints.NotNull;

public record CambioEstadoPacienteRequest(
        @NotNull EstadoPaciente estado
) {
}
