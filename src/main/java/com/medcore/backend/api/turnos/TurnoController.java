package com.medcore.backend.api.turnos;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private final TurnoService service;
    public TurnoController(TurnoService service) { this.service=service; }

    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public ResponseEntity<TurnoResponse> crear(@Valid @RequestBody TurnoRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public Page<TurnoResponse> listar(@PageableDefault(size=20, sort="id") Pageable pageable) { return service.listar(pageable); }
    @GetMapping("/{id}") public TurnoResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @PatchMapping("/{id}/estado") @PreAuthorize("hasAnyRole('ADMIN','RECEPCION','MEDICO')")
    public TurnoResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoTurnoRequest request) { return service.cambiarEstado(id, request); }
}
