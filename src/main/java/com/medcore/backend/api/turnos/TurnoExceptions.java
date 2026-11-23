package com.medcore.backend.api.turnos;

public final class TurnoExceptions {
    public static class TurnoNoEncontradoException extends RuntimeException { public TurnoNoEncontradoException(Long id) { super("Turno no encontrado: "+id); } }
    public static class TurnoInvalidoException extends RuntimeException { public TurnoInvalidoException(String m) { super(m); } }
    private TurnoExceptions() {}
}
