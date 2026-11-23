package com.medcore.backend.api.admisiones;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AdmisionRepository extends JpaRepository<Admision,Long> {
    List<Admision> findByPacienteIdAndEstadoIn(Long pacienteId, java.util.Collection<EstadoAdmision> estados);
}
