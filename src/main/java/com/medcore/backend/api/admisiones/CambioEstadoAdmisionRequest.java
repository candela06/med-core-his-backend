package com.medcore.backend.api.admisiones;
import jakarta.validation.constraints.NotNull;
public record CambioEstadoAdmisionRequest(@NotNull EstadoAdmision estado) {}
