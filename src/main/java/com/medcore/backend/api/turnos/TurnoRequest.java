package com.medcore.backend.api.turnos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TurnoRequest(
        @NotNull Long pacienteId,
        @NotNull Long profesionalId,
        @NotNull @FutureOrPresent LocalDateTime fechaHora,
        @NotNull @Positive Integer duracionMinutos
) {}
