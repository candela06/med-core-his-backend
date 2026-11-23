package com.medcore.backend.api.usuarios;

public class UsuarioExceptions {
    public static class UsuarioNoEncontradoException extends RuntimeException {
        public UsuarioNoEncontradoException(String username) { super("Usuario no encontrado: " + username); }
        public UsuarioNoEncontradoException(Long id) { super("Usuario no encontrado: " + id); }
    }

    public static class UsernameDuplicadoException extends RuntimeException {
        public UsernameDuplicadoException(String username) { super("El nombre de usuario ya está registrado: " + username); }
    }

    public static class UsuarioInactivoException extends RuntimeException {
        public UsuarioInactivoException() { super("El usuario se encuentra inactivo"); }
    }

    public static class IntegracionException extends RuntimeException {
        public IntegracionException(String message) { super("Error de integración: " + message); }
    }

    public static class PermisoDenegadoException extends RuntimeException {
        public PermisoDenegadoException() { super("Permiso denegado para el usuario"); }
    }

    public static class AsociacionProfesionalException extends RuntimeException {
        public AsociacionProfesionalException() { super("El usuario no tiene una asociación profesional válida"); }
        public AsociacionProfesionalException(String message) { super(message); }
    }

    private UsuarioExceptions() {}
}
