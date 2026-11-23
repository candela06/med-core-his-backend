package com.medcore.backend.api.profesionales;

import jakarta.validation.constraints.NotNull;

public record CambioEstadoProfesionalRequest(
        @NotNull EstadoProfesional estado
) {
}
