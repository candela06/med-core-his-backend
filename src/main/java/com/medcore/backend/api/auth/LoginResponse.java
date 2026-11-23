package com.medcore.backend.api.auth;

import com.medcore.backend.api.usuarios.RolUsuario;

public record LoginResponse(
        String token,
        String tipo,
        Long usuarioId,
        String username,
        RolUsuario rol
) {
}
