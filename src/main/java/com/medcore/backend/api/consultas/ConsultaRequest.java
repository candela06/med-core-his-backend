package com.medcore.backend.api.consultas;
import jakarta.validation.constraints.*;
public record ConsultaRequest(@NotNull Long pacienteId,@NotNull Long admisionId,@NotBlank @Size(max=500) String motivoConsulta,@Size(max=3000) String anamnesis,@Size(max=3000) String observacionesClinicas,@Size(max=3000) String diagnostico) {}
