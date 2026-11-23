package com.medcore.backend.api.infraestructura;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;

import com.medcore.backend.api.infraestructura.camas.Cama;
import com.medcore.backend.api.infraestructura.camas.CamaRepository;
import com.medcore.backend.api.infraestructura.camas.CamaRequest;
import com.medcore.backend.api.infraestructura.camas.EstadoCama;
import com.medcore.backend.api.infraestructura.habitaciones.Habitacion;
import com.medcore.backend.api.infraestructura.habitaciones.HabitacionRepository;
import com.medcore.backend.api.infraestructura.habitaciones.HabitacionRequest;

import java.util.*;
@Service public class InfraestructuraService{private final HabitacionRepository habitaciones;private final CamaRepository camas;public InfraestructuraService(HabitacionRepository habitaciones,CamaRepository camas){this.habitaciones=habitaciones;this.camas=camas;}@Transactional public Habitacion crearHabitacion(HabitacionRequest r){Habitacion h=new Habitacion();h.setNumero(r.numero().trim());h.setSector(r.sector().trim());h.setPiso(r.piso());return habitaciones.save(h);}@Transactional public Cama crearCama(Long habitacionId,CamaRequest r){Habitacion h=habitaciones.findById(habitacionId).orElseThrow(() -> new RuntimeException("Habitación no encontrada: "+habitacionId));Cama c=new Cama();c.setNumero(r.numero().trim());c.setHabitacionId(h.getId());return camas.save(c);}public List<Habitacion>listar(){return habitaciones.findAll();}public List<Cama>disponibles(){return camas.findByEstado(EstadoCama.DISPONIBLE);}@Transactional public Cama estadoCama(Long id,EstadoCama estado){Cama c=camas.findByIdForUpdate(id).orElseThrow(() -> new RuntimeException("Cama no encontrada: "+id));c.setEstado(estado);return camas.save(c);}}
