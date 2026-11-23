package com.medcore.backend.api.usuarios;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String username,
        RolUsuario rol,
        Long profesionalId,
        boolean activo,
        LocalDateTime fechaCreacion
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getProfesionalId(),
                usuario.isActivo(),
                usuario.getFechaCreacion()
        );
    }
}
