package com.medcore.backend.api.infraestructura;import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;

import com.medcore.backend.api.infraestructura.camas.Cama;
import com.medcore.backend.api.infraestructura.camas.CamaRequest;
import com.medcore.backend.api.infraestructura.camas.CambioEstadoCamaRequest;
import com.medcore.backend.api.infraestructura.habitaciones.Habitacion;
import com.medcore.backend.api.infraestructura.habitaciones.HabitacionRequest;

import java.util.*;
@RestController public class InfraestructuraController{private final InfraestructuraService service;public InfraestructuraController(InfraestructuraService service){this.service=service;}@GetMapping("/api/habitaciones")public List<Habitacion>habitaciones(){return service.listar();}@PostMapping("/api/habitaciones")@PreAuthorize("hasRole('ADMIN')")public ResponseEntity<Habitacion>crearHabitacion(@Valid@RequestBody HabitacionRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.crearHabitacion(r));}@PostMapping("/api/habitaciones/{id}/camas")@PreAuthorize("hasRole('ADMIN')")public ResponseEntity<Cama>crearCama(@PathVariable Long id,@Valid@RequestBody CamaRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCama(id,r));}@GetMapping("/api/camas/disponibles")public List<Cama>disponibles(){return service.disponibles();}@PatchMapping("/api/camas/{id}/estado")@PreAuthorize("hasRole('ADMIN')")public Cama estado(@PathVariable Long id,@Valid@RequestBody CambioEstadoCamaRequest r){return service.estadoCama(id,r.estado());}}
