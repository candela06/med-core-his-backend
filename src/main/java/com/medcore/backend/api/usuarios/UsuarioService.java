package com.medcore.backend.api.usuarios;

import com.medcore.backend.api.profesionales.EstadoProfesional;
import com.medcore.backend.api.profesionales.Profesional;
import com.medcore.backend.api.profesionales.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ProfesionalRepository profesionalRepository;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this(repository, passwordEncoder, null);
    }

    @Autowired
    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder,
                          ProfesionalRepository profesionalRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.profesionalRepository = profesionalRepository;
    }

    @Transactional
    public UsuarioResponse crear(CrearUsuarioRequest request) {
        String username = request.username().trim();
        if (repository.existsByUsername(username)) {
            throw new UsuarioExceptions.UsernameDuplicadoException(username);
        }

        validarAsociacionProfesional(request.rol(), request.profesionalId());

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setRol(request.rol());
        usuario.setProfesionalId(request.profesionalId());
        usuario.setActivo(true);

        try {
            return UsuarioResponse.from(repository.save(usuario));
        } catch (DataIntegrityViolationException ex) {
            throw new UsuarioExceptions.UsernameDuplicadoException(username);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioExceptions.UsuarioNoEncontradoException(id));
        return UsuarioResponse.from(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(UsuarioResponse::from);
    }

    @Transactional
    public UsuarioResponse cambiarEstado(Long id, CambioEstadoUsuarioRequest request) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioExceptions.UsuarioNoEncontradoException(id));
        usuario.setActivo(request.activo());
        return UsuarioResponse.from(repository.save(usuario));
    }

    private void validarAsociacionProfesional(RolUsuario rol, Long profesionalId) {
        if (rol == RolUsuario.MEDICO && profesionalId == null) {
            throw new UsuarioExceptions.AsociacionProfesionalException(
                    "Un usuario con rol MEDICO debe estar asociado a un profesional");
        }

        if (profesionalId == null) return;
        if (profesionalRepository == null) return;

        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new UsuarioExceptions.AsociacionProfesionalException(
                        "Profesional no encontrado: " + profesionalId));
        if (profesional.getEstado() != EstadoProfesional.ACTIVO) {
            throw new UsuarioExceptions.AsociacionProfesionalException(
                    "No se puede asociar un usuario a un profesional inactivo: " + profesionalId);
        }

        if (rol != RolUsuario.MEDICO && rol != RolUsuario.ENFERMERIA) {
            throw new UsuarioExceptions.AsociacionProfesionalException(
                    "El rol " + rol + " no debe estar asociado a un profesional");
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarAutenticable(String username) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExceptions.UsuarioNoEncontradoException(username));
        if (!usuario.isActivo()) {
            throw new UsuarioExceptions.UsuarioInactivoException();
        }
        return usuario;
    }
}
