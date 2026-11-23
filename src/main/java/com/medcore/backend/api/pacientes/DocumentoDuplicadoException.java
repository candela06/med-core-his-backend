package com.medcore.backend.api.pacientes;

public class DocumentoDuplicadoException extends RuntimeException {
    public DocumentoDuplicadoException(String documento) {
        super("El documento ya está registrado: " + documento);
    }
}
