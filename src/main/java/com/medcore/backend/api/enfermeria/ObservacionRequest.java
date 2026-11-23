package com.medcore.backend.api.enfermeria;import jakarta.validation.constraints.*;public record ObservacionRequest(@NotBlank @Size(max=3000)String descripcion,@NotNull TipoObservacion tipo){}
