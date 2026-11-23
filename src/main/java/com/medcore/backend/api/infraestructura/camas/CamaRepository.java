package com.medcore.backend.api.infraestructura.camas;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;
import jakarta.persistence.LockModeType;

public interface CamaRepository extends JpaRepository<Cama, Long> {
    List<Cama> findByEstado(EstadoCama estado);

    List<Cama> findByHabitacionId(Long habitacionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cama c where c.id=:id")
    Optional<Cama> findByIdForUpdate(@Param("id") Long id);
}
