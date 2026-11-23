package com.medcore.backend.api.admisiones;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admisiones")
public class AdmisionController {
    private final AdmisionService service; public AdmisionController(AdmisionService service){this.service=service;}
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','RECEPCION','MEDICO')") public ResponseEntity<AdmisionResponse> crear(@Valid @RequestBody AdmisionRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(r));}
    @GetMapping public Page<AdmisionResponse> listar(@PageableDefault(size=20,sort="id") Pageable p){return service.listar(p);}
    @GetMapping("/{id}") public AdmisionResponse obtener(@PathVariable Long id){return service.obtener(id);}
    @PatchMapping("/{id}/estado") @PreAuthorize("hasAnyRole('ADMIN','RECEPCION','MEDICO')") public AdmisionResponse estado(@PathVariable Long id,@Valid @RequestBody CambioEstadoAdmisionRequest r){return service.cambiarEstado(id,r);}
}
