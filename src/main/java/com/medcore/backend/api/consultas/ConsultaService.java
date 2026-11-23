package com.medcore.backend.api.consultas;

import com.medcore.backend.api.admisiones.*;
import com.medcore.backend.api.usuarios.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {
    private final ConsultaRepository repository;
    private final AdmisionRepository admisiones;
    private final UsuarioRepository usuarios;

    public ConsultaService(ConsultaRepository repository, AdmisionRepository admisiones,
                           UsuarioRepository usuarios) {
        this.repository = repository;
        this.admisiones = admisiones;
        this.usuarios = usuarios;
    }

    @Transactional
    public ConsultaResponse crear(ConsultaRequest r) {
        Admision admision = admisiones.findById(r.admisionId())
                .orElseThrow(() -> new ConsultaExceptions.ConsultaInvalidaException(
                        "Admisión no encontrada: " + r.admisionId()));
        if (!admision.getPacienteId().equals(r.pacienteId()))
            throw new ConsultaExceptions.ConsultaInvalidaException("La admisión no corresponde al paciente");
        if (admision.getEstado() == EstadoAdmision.CANCELADA || admision.getEstado() == EstadoAdmision.FINALIZADA)
            throw new ConsultaExceptions.ConsultaInvalidaException("La admisión no admite consultas");

        Usuario usuario = usuarioActual();
        if (usuario.getProfesionalId() == null)
            throw new ConsultaExceptions.ConsultaInvalidaException("El usuario médico no tiene profesional asociado");
        if (admision.getProfesionalId() != null
                && !admision.getProfesionalId().equals(usuario.getProfesionalId()))
            throw new ConsultaExceptions.ConsultaInvalidaException("El profesional no tiene asignada esta atención");

        Consulta consulta = new Consulta();
        consulta.setPacienteId(r.pacienteId());
        consulta.setProfesionalId(usuario.getProfesionalId());
        consulta.setAdmisionId(r.admisionId());
        consulta.setMotivoConsulta(r.motivoConsulta().trim());
        consulta.setAnamnesis(r.anamnesis());
        consulta.setObservacionesClinicas(r.observacionesClinicas());
        consulta.setDiagnostico(r.diagnostico());
        consulta.setAutor(usuario.getUsername());
        return ConsultaResponse.from(repository.save(consulta));
    }

    @Transactional(readOnly = true)
    public ConsultaResponse obtener(Long id) {
        return ConsultaResponse.from(repository.findById(id)
                .orElseThrow(() -> new ConsultaExceptions.ConsultaNoEncontradaException(id)));
    }

    @Transactional(readOnly = true)
    public Page<ConsultaResponse> historial(Long pacienteId, Pageable pageable) {
        return repository.findByPacienteId(pacienteId, pageable).map(ConsultaResponse::from);
    }

    @Transactional
    public ConsultaResponse finalizar(Long id) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new ConsultaExceptions.ConsultaNoEncontradaException(id));
        if (consulta.getEstado() == EstadoConsulta.FINALIZADA)
            throw new ConsultaExceptions.ConsultaInvalidaException("La consulta ya está finalizada");

        Usuario usuario = usuarioActual();
        if (usuario.getProfesionalId() == null
                || !usuario.getProfesionalId().equals(consulta.getProfesionalId()))
            throw new ConsultaExceptions.ConsultaInvalidaException("Solo el profesional responsable puede finalizar la consulta");

        consulta.setEstado(EstadoConsulta.FINALIZADA);
        return ConsultaResponse.from(repository.save(consulta));
    }

    private Usuario usuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarios.findByUsername(username)
                .orElseThrow(() -> new UsuarioExceptions.UsuarioNoEncontradoException(username));
    }
}
