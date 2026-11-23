package com.medcore.backend.api.config;

import com.medcore.backend.api.usuarios.RolUsuario;
import com.medcore.backend.api.usuarios.Usuario;
import com.medcore.backend.api.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    public ApplicationRunner crearAdministradorInicial(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${MEDCORE_ADMIN_USERNAME:}") String username,
            @Value("${MEDCORE_ADMIN_PASSWORD:}") String password) {
        return args -> {
            if (username.isBlank() || password.isBlank() || repository.count() > 0) {
                return;
            }

            Usuario administrador = new Usuario();
            administrador.setUsername(username.trim());
            administrador.setPasswordHash(passwordEncoder.encode(password));
            administrador.setRol(RolUsuario.ADMIN);
            administrador.setProfesionalId(null);
            administrador.setActivo(true);
            repository.save(administrador);
        };
    }
}
