package com.medcore.backend.api.profesionales;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfesionalService {

    private final ProfesionalRepository repository;

    public ProfesionalService(ProfesionalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProfesionalResponse crear(ProfesionalRequest request) {
        String matricula = request.matricula().trim();
        if (repository.existsByMatricula(matricula)) {
            throw new ProfesionalExceptions.MatriculaDuplicadaException(matricula);
        }

        Profesional profesional = new Profesional();
        aplicarDatos(profesional, request);
        profesional.setMatricula(matricula);
        try {
            return ProfesionalResponse.from(repository.save(profesional));
        } catch (DataIntegrityViolationException ex) {
            throw new ProfesionalExceptions.MatriculaDuplicadaException(matricula);
        }
    }

    @Transactional(readOnly = true)
    public Page<ProfesionalResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(ProfesionalResponse::from);
    }

    @Transactional(readOnly = true)
    public ProfesionalResponse obtenerPorId(Long id) {
        return ProfesionalResponse.from(buscar(id));
    }

    @Transactional
    public ProfesionalResponse modificar(Long id, ProfesionalRequest request) {
        Profesional profesional = buscar(id);
        String matricula = request.matricula().trim();
        if (repository.existsByMatriculaAndIdNot(matricula, id)) {
            throw new ProfesionalExceptions.MatriculaDuplicadaException(matricula);
        }

        aplicarDatos(profesional, request);
        profesional.setMatricula(matricula);
        try {
            return ProfesionalResponse.from(repository.save(profesional));
        } catch (DataIntegrityViolationException ex) {
            throw new ProfesionalExceptions.MatriculaDuplicadaException(matricula);
        }
    }

    @Transactional
    public ProfesionalResponse cambiarEstado(Long id, CambioEstadoProfesionalRequest request) {
        Profesional profesional = buscar(id);
        profesional.setEstado(request.estado());
        return ProfesionalResponse.from(repository.save(profesional));
    }

    private Profesional buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProfesionalExceptions.ProfesionalNoEncontradoException(id));
    }

    private void aplicarDatos(Profesional profesional, ProfesionalRequest request) {
        profesional.setNombre(request.nombre().trim());
        profesional.setApellido(request.apellido().trim());
        profesional.setEspecialidad(request.especialidad());
        profesional.setTelefono(normalizarOpcional(request.telefono()));
        profesional.setCorreoElectronico(normalizarOpcional(request.correoElectronico()));
    }

    private String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) return null;
        return valor.trim();
    }
}
