package com.medcore.backend.api.consultas;
import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.domain.*;
public interface ConsultaRepository extends JpaRepository<Consulta,Long>{Page<Consulta> findByPacienteId(Long pacienteId,Pageable pageable);}
