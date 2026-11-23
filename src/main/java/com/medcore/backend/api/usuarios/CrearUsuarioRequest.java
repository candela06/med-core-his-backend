package com.medcore.backend.api.usuarios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(
        @NotBlank @Size(max = 100) String username,
        @NotBlank @Size(min = 8, max = 255) String password,
        @NotNull RolUsuario rol,
        Long profesionalId
) {
}
