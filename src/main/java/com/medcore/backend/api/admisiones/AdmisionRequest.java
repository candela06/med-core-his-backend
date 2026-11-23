package com.medcore.backend.api.admisiones;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdmisionRequest(
        @NotNull Long pacienteId,
        @NotNull TipoIngreso tipoIngreso,
        @NotBlank @Size(max=500) String motivoConsulta,
        Long profesionalId,
        Long turnoId
) {}
