package com.medcore.backend.api.turnos;

import jakarta.validation.constraints.NotNull;

public record CambioEstadoTurnoRequest(@NotNull EstadoTurno estado) {}
