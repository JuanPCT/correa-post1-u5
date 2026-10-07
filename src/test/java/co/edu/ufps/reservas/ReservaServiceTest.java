package co.edu.ufps.reservas;

import static org.junit.jupiter.api.Assertions.*;
import co.edu.ufps.reservas.dominio.*;
import co.edu.ufps.reservas.repositorio.*;
import co.edu.ufps.reservas.servicio.*;
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
    private LocalDateTime at(int hour) { return LocalDate.now().plusDays(2).atTime(hour, 0); }
    private Long lab() { return labs.findAll().get(0).getId(); }
    @Test void createsReservationAndRejectsOverlappingInterval() {
        var request = new ReservaRequest(lab(), "Juan Correa", at(9), at(10));
        var created = service.crear(request);
        assertEquals(EstadoReserva.ACTIVA, created.getEstado());
        var conflict = new ReservaRequest(lab(), "Ana Pérez", at(9).plusMinutes(30), at(10).plusMinutes(30));
        assertEquals(409, assertThrows(ReservaException.class, () -> service.crear(conflict)).status());
    }
    @Test void rejectsDurationOutsideThePublishedRange() {
        var request = new ReservaRequest(lab(), "Juan", at(9), at(9).plusMinutes(20));
        assertEquals(400, assertThrows(ReservaException.class, () -> service.crear(request)).status());
    }
    @Test void cancellationMarksReservationInsteadOfDeletingItsHistory() {
        var created = service.crear(new ReservaRequest(lab(), "Juan", at(11), at(12)));
        var cancelled = service.cancelar(created.getId());
        assertEquals(EstadoReserva.CANCELADA, cancelled.getEstado());
        assertTrue(reservas.existsById(created.getId()));
    }
}
