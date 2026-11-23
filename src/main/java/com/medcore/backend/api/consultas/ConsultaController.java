package com.medcore.backend.api.consultas;
import jakarta.validation.Valid;import org.springframework.data.domain.*;import org.springframework.data.web.PageableDefault;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;
@RestController
public class ConsultaController {
 private final ConsultaService service; public ConsultaController(ConsultaService service){this.service=service;}
 @PostMapping("/api/consultas") @PreAuthorize("hasRole('MEDICO')") public ResponseEntity<ConsultaResponse> crear(@Valid @RequestBody ConsultaRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(r));}
 @GetMapping("/api/consultas/{id}") public ConsultaResponse obtener(@PathVariable Long id){return service.obtener(id);}
 @GetMapping("/api/pacientes/{id}/consultas") public Page<ConsultaResponse> historial(@PathVariable("id") Long pacienteId,@PageableDefault(size=20,sort="fechaHora") Pageable p){return service.historial(pacienteId,p);}
 @PatchMapping("/api/consultas/{id}/finalizar") @PreAuthorize("hasRole('MEDICO')") public ConsultaResponse finalizar(@PathVariable Long id){return service.finalizar(id);}
}
