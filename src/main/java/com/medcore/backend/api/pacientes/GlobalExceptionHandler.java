package com.medcore.backend.api.pacientes;

import com.medcore.backend.api.admisiones.AdmisionExceptions;
import com.medcore.backend.api.auth.CredencialesInvalidasException;
import com.medcore.backend.api.consultas.ConsultaExceptions;
import com.medcore.backend.api.internaciones.InternacionExceptions;
import com.medcore.backend.api.profesionales.ProfesionalExceptions;
import com.medcore.backend.api.turnos.TurnoExceptions;
import com.medcore.backend.api.usuarios.UsuarioExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final ZoneId ZONA_MEDCORE = ZoneId.of("America/Argentina/Buenos_Aires");

    @ExceptionHandler(PacienteNotFoundException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(PacienteNotFoundException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<ApiError> manejarDuplicado(DocumentoDuplicadoException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiError> manejarCredenciales(CredencialesInvalidasException ex) {
        return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler({UsuarioExceptions.UsuarioNoEncontradoException.class,
            UsuarioExceptions.UsuarioInactivoException.class})
    public ResponseEntity<ApiError> manejarUsuario(RuntimeException ex) {
        return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UsuarioExceptions.UsernameDuplicadoException.class)
    public ResponseEntity<ApiError> manejarUsernameDuplicado(UsuarioExceptions.UsernameDuplicadoException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UsuarioExceptions.AsociacionProfesionalException.class)
    public ResponseEntity<ApiError> manejarAsociacionProfesional(UsuarioExceptions.AsociacionProfesionalException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ProfesionalExceptions.ProfesionalNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarProfesionalNoEncontrado(ProfesionalExceptions.ProfesionalNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ProfesionalExceptions.MatriculaDuplicadaException.class)
    public ResponseEntity<ApiError> manejarMatriculaDuplicada(ProfesionalExceptions.MatriculaDuplicadaException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(TurnoExceptions.TurnoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarTurnoNoEncontrado(TurnoExceptions.TurnoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TurnoExceptions.TurnoInvalidoException.class)
    public ResponseEntity<ApiError> manejarTurnoInvalido(TurnoExceptions.TurnoInvalidoException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AdmisionExceptions.AdmisionNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarAdmisionNoEncontrada(AdmisionExceptions.AdmisionNoEncontradaException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AdmisionExceptions.AdmisionInvalidaException.class)
    public ResponseEntity<ApiError> manejarAdmisionInvalida(AdmisionExceptions.AdmisionInvalidaException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConsultaExceptions.ConsultaNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarConsultaNoEncontrada(ConsultaExceptions.ConsultaNoEncontradaException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConsultaExceptions.ConsultaInvalidaException.class)
    public ResponseEntity<ApiError> manejarConsultaInvalida(ConsultaExceptions.ConsultaInvalidaException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InternacionExceptions.InternacionNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarInternacionNoEncontrada(InternacionExceptions.InternacionNoEncontradaException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InternacionExceptions.InternacionInvalidaException.class)
    public ResponseEntity<ApiError> manejarInternacionInvalida(InternacionExceptions.InternacionInvalidaException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));
        return respuesta(HttpStatus.BAD_REQUEST,
                mensaje.isBlank() ? "Los datos enviados no son válidos" : mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInesperado(Exception ex) {
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno inesperado");
    }

    private ResponseEntity<ApiError> respuesta(HttpStatus estado, String mensaje) {
        ApiError error = new ApiError(estado.value(), estado.name(), mensaje,
                LocalDateTime.now(ZONA_MEDCORE));
        return ResponseEntity.status(estado).body(error);
    }
}
