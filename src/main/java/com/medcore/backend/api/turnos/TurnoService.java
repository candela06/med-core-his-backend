package com.medcore.backend.api.turnos;

import com.medcore.backend.api.pacientes.EstadoPaciente;
import com.medcore.backend.api.pacientes.Paciente;
import com.medcore.backend.api.pacientes.PacienteRepository;
import com.medcore.backend.api.profesionales.EstadoProfesional;
import com.medcore.backend.api.profesionales.Profesional;
import com.medcore.backend.api.profesionales.ProfesionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TurnoService {
    private final TurnoRepository repository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;

    public TurnoService(TurnoRepository repository, PacienteRepository pacienteRepository, ProfesionalRepository profesionalRepository) {
        this.repository=repository; this.pacienteRepository=pacienteRepository; this.profesionalRepository=profesionalRepository;
    }

    @Transactional
    public TurnoResponse crear(TurnoRequest request) {
        Paciente paciente=pacienteRepository.findById(request.pacienteId()).orElseThrow(() -> new TurnoExceptions.TurnoInvalidoException("Paciente no encontrado: "+request.pacienteId()));
        if(paciente.getEstado()!=EstadoPaciente.ACTIVO) throw new TurnoExceptions.TurnoInvalidoException("El paciente se encuentra inactivo");
        Profesional profesional=profesionalRepository.findById(request.profesionalId()).orElseThrow(() -> new TurnoExceptions.TurnoInvalidoException("Profesional no encontrado: "+request.profesionalId()));
        if(profesional.getEstado()!=EstadoProfesional.ACTIVO) throw new TurnoExceptions.TurnoInvalidoException("El profesional se encuentra inactivo");
        if(request.fechaHora().isBefore(LocalDateTime.now())) throw new TurnoExceptions.TurnoInvalidoException("No se pueden registrar turnos en fechas pasadas");

        LocalDateTime inicio=request.fechaHora(), fin=inicio.plusMinutes(request.duracionMinutos());
        if(repository.findByPacienteId(request.pacienteId()).stream().anyMatch(t -> activo(t) && solapa(t,inicio,fin))) throw new TurnoExceptions.TurnoInvalidoException("El paciente tiene un turno superpuesto");
        if(repository.findByProfesionalId(request.profesionalId()).stream().anyMatch(t -> activo(t) && solapa(t,inicio,fin))) throw new TurnoExceptions.TurnoInvalidoException("El profesional tiene un turno superpuesto");

        Turno turno=new Turno(); turno.setPacienteId(request.pacienteId()); turno.setProfesionalId(request.profesionalId()); turno.setFechaHora(request.fechaHora()); turno.setDuracionMinutos(request.duracionMinutos());
        return TurnoResponse.from(repository.save(turno));
    }

    @Transactional(readOnly=true) public Page<TurnoResponse> listar(Pageable pageable) { return repository.findAll(pageable).map(TurnoResponse::from); }
    @Transactional(readOnly=true) public TurnoResponse obtener(Long id) { return TurnoResponse.from(buscar(id)); }

    @Transactional
    public TurnoResponse cambiarEstado(Long id, CambioEstadoTurnoRequest request) {
        Turno turno=buscar(id); EstadoTurno nuevo=request.estado(); EstadoTurno actual=turno.getEstado();
        if(actual==EstadoTurno.CANCELADO && nuevo==EstadoTurno.CONFIRMADO) throw new TurnoExceptions.TurnoInvalidoException("Un turno cancelado no puede volver a confirmarse");
        if(actual==EstadoTurno.ATENDIDO && nuevo==EstadoTurno.CANCELADO) throw new TurnoExceptions.TurnoInvalidoException("Un turno atendido no puede cancelarse");
        turno.setEstado(nuevo); return TurnoResponse.from(repository.save(turno));
    }

    private Turno buscar(Long id) { return repository.findById(id).orElseThrow(() -> new TurnoExceptions.TurnoNoEncontradoException(id)); }
    private boolean activo(Turno t) { return t.getEstado()!=EstadoTurno.CANCELADO && t.getEstado()!=EstadoTurno.ATENDIDO && t.getEstado()!=EstadoTurno.AUSENTE; }
    private boolean solapa(Turno t, LocalDateTime inicio, LocalDateTime fin) { LocalDateTime inicioExistente=t.getFechaHora(); LocalDateTime finExistente=inicioExistente.plusMinutes(t.getDuracionMinutos()); return inicio.isBefore(finExistente) && inicioExistente.isBefore(fin); }
}
