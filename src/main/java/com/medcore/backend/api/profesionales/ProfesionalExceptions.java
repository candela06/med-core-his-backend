package com.medcore.backend.api.profesionales;

public final class ProfesionalExceptions {

    public static class ProfesionalNoEncontradoException extends RuntimeException {
        public ProfesionalNoEncontradoException(Long id) { super("Profesional no encontrado: " + id); }
    }

    public static class MatriculaDuplicadaException extends RuntimeException {
        public MatriculaDuplicadaException(String matricula) {
            super("La matrícula profesional ya está registrada: " + matricula);
        }
    }

    public static class ProfesionalInactivoException extends RuntimeException {
        public ProfesionalInactivoException(Long id) { super("El profesional se encuentra inactivo: " + id); }
    }

    private ProfesionalExceptions() {}
}
