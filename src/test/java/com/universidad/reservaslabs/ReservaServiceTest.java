package com.universidad.reservaslabs;

import static org.junit.jupiter.api.Assertions.*;
import com.universidad.reservaslabs.exception.DatosReservaInvalidosException;
import com.universidad.reservaslabs.exception.ReservaConflictException;
import com.universidad.reservaslabs.model.EstadoReserva;
import com.universidad.reservaslabs.repository.LaboratorioRepository;
import com.universidad.reservaslabs.repository.ReservaRepository;
import com.universidad.reservaslabs.service.ReservaRequest;
import com.universidad.reservaslabs.service.ReservaService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest @Transactional
class ReservaServiceTest {
    @Autowired ReservaService service;
    @Autowired LaboratorioRepository labs;
    @Autowired ReservaRepository reservas;
    private LocalDateTime at(int hour) { return LocalDate.now().plusDays(30).atTime(hour, 0); }
    private Long lab() { return labs.findAll().get(0).getId(); }
    private ReservaRequest request(int start, int end) {
        return new ReservaRequest(lab(), "Juan Pablo Correa Tarazona", "juan.correa@example.edu.co",
                at(start), at(end), "Práctica de integración");
    }

    @Test void createsPendingReservationAndRepositoryFindsStrictOverlap() {
        var created = service.crear(request(9, 10));
        assertEquals(EstadoReserva.PENDIENTE, created.getEstado());
        assertEquals(1, reservas.buscarSolapamientos(lab(), at(9).plusMinutes(30), at(10).plusMinutes(30)).size());
        assertTrue(reservas.buscarSolapamientos(lab(), at(10), at(11)).isEmpty());
        assertThrows(ReservaConflictException.class, () -> service.crear(request(9, 10)));
    }

    @Test void rejectsDurationOutsideThePublishedRangeAndInvalidEmail() {
        var shortRequest = new ReservaRequest(lab(), "Juan", "juan@example.edu.co", at(9), at(9).plusMinutes(20), "Prueba");
        assertThrows(DatosReservaInvalidosException.class, () -> service.crear(shortRequest));
        var invalidEmail = new ReservaRequest(lab(), "Juan", "correo-invalido", at(9), at(10), "Prueba");
        assertThrows(DatosReservaInvalidosException.class, () -> service.crear(invalidEmail));
    }

    @Test void cancellationMarksReservationInsteadOfDeletingItsHistory() {
        var created = service.crear(request(11, 12));
        var cancelled = service.cancelar(created.getId());
        assertEquals(EstadoReserva.CANCELADA, cancelled.getEstado());
        assertTrue(reservas.existsById(created.getId()));
        assertTrue(reservas.buscarSolapamientos(lab(), at(11), at(12)).isEmpty());
    }
}
