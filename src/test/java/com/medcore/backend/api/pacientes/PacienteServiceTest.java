package com.medcore.backend.api.pacientes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @InjectMocks
    private PacienteService service;

    @Test
    void rechazaDocumentoDuplicado() {
        PacienteRequest request = request("45123456");
        when(repository.existsByDocumento("45123456")).thenReturn(true);

        assertThrows(DocumentoDuplicadoException.class, () -> service.crear(request));
    }

    @Test
    void creaPacienteActivoPorDefecto() {
        PacienteRequest request = request("45123457");
        when(repository.existsByDocumento("45123457")).thenReturn(false);
        when(repository.save(any(Paciente.class))).thenAnswer(invocation -> {
            Paciente paciente = invocation.getArgument(0);
            paciente.setId(1L);
            paciente.setEstado(paciente.getEstado() == null ? EstadoPaciente.ACTIVO : paciente.getEstado());
            paciente.setFechaRegistro(java.time.LocalDateTime.now());
            return paciente;
        });

        PacienteResponse response = service.crear(request);

        assertEquals(EstadoPaciente.ACTIVO, response.estado());
        verify(repository).save(any(Paciente.class));
    }

    @Test
    void rechazaPacienteInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PacienteNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    private PacienteRequest request(String documento) {
        return new PacienteRequest(
                documento,
                "Lucía",
                "Gómez",
                LocalDate.of(1998, 5, 12),
                Sexo.FEMENINO,
                "1123456789",
                "lucia@example.com",
                "Calle 123"
        );
    }
}
