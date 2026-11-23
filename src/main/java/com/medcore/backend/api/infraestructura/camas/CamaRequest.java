package com.medcore.backend.api.infraestructura.camas;import jakarta.validation.constraints.*;public record CamaRequest(@NotBlank @Size(max=20)String numero){}
