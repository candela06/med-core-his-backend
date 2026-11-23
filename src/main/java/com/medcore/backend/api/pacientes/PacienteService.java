package com.medcore.backend.api.pacientes;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PacienteResponse crear(PacienteRequest request) {
        String documento = request.documento().trim();
        if (repository.existsByDocumento(documento)) {
            throw new DocumentoDuplicadoException(documento);
        }

        Paciente paciente = new Paciente();
        aplicarDatos(paciente, request);
        paciente.setDocumento(documento);
        try {
            return PacienteResponse.from(repository.save(paciente));
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentoDuplicadoException(documento);
        }
    }

    @Transactional(readOnly = true)
    public Page<PacienteResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(PacienteResponse::from);
    }

    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
        return PacienteResponse.from(buscar(id));
    }

    @Transactional
    public PacienteResponse modificar(Long id, PacienteRequest request) {
        Paciente paciente = buscar(id);
        String documento = request.documento().trim();

        if (repository.existsByDocumentoAndIdNot(documento, id)) {
            throw new DocumentoDuplicadoException(documento);
        }

        aplicarDatos(paciente, request);
        paciente.setDocumento(documento);
        try {
            return PacienteResponse.from(repository.save(paciente));
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentoDuplicadoException(documento);
        }
    }

    @Transactional
    public PacienteResponse cambiarEstado(Long id, CambioEstadoPacienteRequest request) {
        Paciente paciente = buscar(id);
        paciente.setEstado(request.estado());
        return PacienteResponse.from(repository.save(paciente));
    }

    private Paciente buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));
    }

    private void aplicarDatos(Paciente paciente, PacienteRequest request) {
        paciente.setNombre(request.nombre().trim());
        paciente.setApellido(request.apellido().trim());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setSexo(request.sexo());
        paciente.setTelefono(request.telefono() == null ? null : request.telefono().trim());
        paciente.setCorreoElectronico(normalizarOpcional(request.correoElectronico()));
        paciente.setDomicilio(request.domicilio().trim());
    }

    private String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
