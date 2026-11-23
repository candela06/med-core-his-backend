package com.medcore.backend.api.usuarios;

import jakarta.validation.constraints.NotNull;

public record CambioEstadoUsuarioRequest(
        @NotNull Boolean activo
) {
}
