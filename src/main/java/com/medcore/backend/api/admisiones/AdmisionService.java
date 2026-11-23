package com.medcore.backend.api.admisiones;

import com.medcore.backend.api.pacientes.EstadoPaciente;
import com.medcore.backend.api.pacientes.Paciente;
import com.medcore.backend.api.pacientes.PacienteRepository;
import com.medcore.backend.api.profesionales.EstadoProfesional;
import com.medcore.backend.api.profesionales.Profesional;
import com.medcore.backend.api.profesionales.ProfesionalRepository;
import com.medcore.backend.api.turnos.EstadoTurno;
import com.medcore.backend.api.turnos.Turno;
import com.medcore.backend.api.turnos.TurnoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdmisionService {
    private final AdmisionRepository repository;
    private final PacienteRepository pacientes;
    private final ProfesionalRepository profesionales;
    private final TurnoRepository turnos;

    public AdmisionService(AdmisionRepository repository, PacienteRepository pacientes,
                           ProfesionalRepository profesionales, TurnoRepository turnos) {
        this.repository = repository;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
        this.turnos = turnos;
    }

    @Transactional
    public AdmisionResponse crear(AdmisionRequest r) {
        Paciente paciente = pacientes.findById(r.pacienteId())
                .orElseThrow(() -> new AdmisionExceptions.AdmisionInvalidaException(
                        "Paciente no encontrado: " + r.pacienteId()));
        if (paciente.getEstado() != EstadoPaciente.ACTIVO)
            throw new AdmisionExceptions.AdmisionInvalidaException("El paciente se encuentra inactivo");

        List<Admision> abiertas = repository.findByPacienteIdAndEstadoIn(r.pacienteId(),
                List.of(EstadoAdmision.EN_ESPERA, EstadoAdmision.EN_ATENCION));
        if (!abiertas.isEmpty())
            throw new AdmisionExceptions.AdmisionInvalidaException("El paciente ya tiene una admisión abierta");

        Long profesionalSeleccionado = r.profesionalId();
        if (r.tipoIngreso() == TipoIngreso.PROGRAMADO) {
            if (r.turnoId() == null)
                throw new AdmisionExceptions.AdmisionInvalidaException("Una admisión programada debe indicar el turno");
            Turno turno = turnos.findById(r.turnoId())
                    .orElseThrow(() -> new AdmisionExceptions.AdmisionInvalidaException(
                            "Turno no encontrado: " + r.turnoId()));
            if (!turno.getPacienteId().equals(r.pacienteId()) || turno.getEstado() == EstadoTurno.CANCELADO)
                throw new AdmisionExceptions.AdmisionInvalidaException("El turno no corresponde a la admisión");
            profesionalSeleccionado = turno.getProfesionalId();
        }

        final Long profesionalId = profesionalSeleccionado;
        if (profesionalId != null) {
            Profesional profesional = profesionales.findById(profesionalId)
                    .orElseThrow(() -> new AdmisionExceptions.AdmisionInvalidaException(
                            "Profesional no encontrado: " + profesionalId));
            if (profesional.getEstado() != EstadoProfesional.ACTIVO)
                throw new AdmisionExceptions.AdmisionInvalidaException("El profesional se encuentra inactivo");
        }

        Admision admision = new Admision();
        admision.setPacienteId(r.pacienteId());
        admision.setTipoIngreso(r.tipoIngreso());
        admision.setMotivoConsulta(r.motivoConsulta().trim());
        admision.setProfesionalId(profesionalId);
        admision.setTurnoId(r.turnoId());
        return AdmisionResponse.from(repository.save(admision));
    }

    @Transactional(readOnly = true)
    public Page<AdmisionResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(AdmisionResponse::from);
    }

    @Transactional(readOnly = true)
    public AdmisionResponse obtener(Long id) {
        return AdmisionResponse.from(buscar(id));
    }

    @Transactional
    public AdmisionResponse cambiarEstado(Long id, CambioEstadoAdmisionRequest r) {
        Admision admision = buscar(id);
        EstadoAdmision actual = admision.getEstado();
        EstadoAdmision nuevo = r.estado();
        if (actual == EstadoAdmision.CANCELADA)
            throw new AdmisionExceptions.AdmisionInvalidaException("Una admisión cancelada no puede continuar");
        if (actual == EstadoAdmision.FINALIZADA)
            throw new AdmisionExceptions.AdmisionInvalidaException("Una admisión finalizada no puede modificarse");
        if (nuevo == EstadoAdmision.EN_ATENCION && admision.getTurnoId() != null) {
            Turno turno = turnos.findById(admision.getTurnoId()).orElse(null);
            if (turno != null) {
                turno.setEstado(EstadoTurno.ATENDIDO);
                turnos.save(turno);
            }
        }
        admision.setEstado(nuevo);
        return AdmisionResponse.from(repository.save(admision));
    }

    private Admision buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AdmisionExceptions.AdmisionNoEncontradaException(id));
    }
}
