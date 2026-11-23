package com.medcore.backend.api.auth;

import com.medcore.backend.api.usuarios.Usuario;
import com.medcore.backend.api.usuarios.UsuarioExceptions;
import com.medcore.backend.api.usuarios.UsuarioRepository;
import com.medcore.backend.api.usuarios.UsuarioResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username().trim())
                .orElseThrow(() -> new UsuarioExceptions.UsuarioNoEncontradoException(request.username().trim()));

        if (!usuario.isActivo()) {
            throw new UsuarioExceptions.UsuarioInactivoException();
        }
        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        String token = jwtService.generarToken(usuario);
        return new LoginResponse(token, "Bearer", usuario.getId(), usuario.getUsername(), usuario.getRol());
    }

    public UsuarioResponse me(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExceptions.UsuarioNoEncontradoException(username));
        if (!usuario.isActivo()) {
            throw new UsuarioExceptions.UsuarioInactivoException();
        }
        return UsuarioResponse.from(usuario);
    }
}
