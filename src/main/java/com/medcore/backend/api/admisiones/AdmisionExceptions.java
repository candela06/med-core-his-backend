package com.medcore.backend.api.admisiones;
public final class AdmisionExceptions {
    public static class AdmisionNoEncontradaException extends RuntimeException { public AdmisionNoEncontradaException(Long id){super("Admisión no encontrada: "+id);} }
    public static class AdmisionInvalidaException extends RuntimeException { public AdmisionInvalidaException(String m){super(m);} }
    private AdmisionExceptions(){}
}
