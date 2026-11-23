package com.medcore.backend.api.pacientes;

public class PacienteNotFoundException extends RuntimeException {
    public PacienteNotFoundException(Long id) {
        super("Paciente no encontrado: " + id);
    }
}
