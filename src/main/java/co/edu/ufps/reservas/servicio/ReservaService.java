package co.edu.ufps.reservas.servicio;

import co.edu.ufps.reservas.dominio.*;
import co.edu.ufps.reservas.repositorio.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
    private final ReservaRepository reservas;
    private final LaboratorioRepository laboratorios;
    public ReservaService(ReservaRepository reservas, LaboratorioRepository laboratorios) {
        this.reservas = reservas; this.laboratorios = laboratorios;
    }
    @Transactional(readOnly = true) public List<Reserva> listar() { return reservas.findAllByOrderByInicioAsc(); }
    @Transactional(readOnly = true) public Reserva obtener(long id) {
        return reservas.findById(id).orElseThrow(() -> new ReservaException(404, "Reserva no encontrada"));
    }
    @Transactional public Reserva crear(ReservaRequest request) {
        if (request == null || request.laboratorioId() == null || request.solicitante() == null
                || request.solicitante().isBlank() || request.inicio() == null || request.fin() == null) {
            throw new ReservaException(400, "Laboratorio, solicitante, inicio y fin son obligatorios");
        }
        if (!request.fin().isAfter(request.inicio())) throw new ReservaException(400, "El fin debe ser posterior al inicio");
        long minutes = Duration.between(request.inicio(), request.fin()).toMinutes();
        if (minutes < 30 || minutes > 180) throw new ReservaException(400, "La duración debe estar entre 30 minutos y 3 horas");
        if (request.inicio().isBefore(java.time.LocalDateTime.now())) throw new ReservaException(400, "No se permiten reservas en horas pasadas");
        if (!request.inicio().toLocalDate().equals(request.fin().toLocalDate())) {
            throw new ReservaException(400, "La reserva debe terminar el mismo día");
        }
        LocalTime start = request.inicio().toLocalTime(), end = request.fin().toLocalTime();
        if (start.isBefore(LocalTime.of(7, 0)) || end.isAfter(LocalTime.of(21, 0))) {
            throw new ReservaException(400, "El horario permitido es de 07:00 a 21:00");
        }
        Laboratorio lab = laboratorios.findById(request.laboratorioId())
                .orElseThrow(() -> new ReservaException(404, "Laboratorio no encontrado"));
        if (!lab.isActivo()) throw new ReservaException(409, "El laboratorio no está disponible");
        if (reservas.existeSolapamiento(lab.getId(), EstadoReserva.ACTIVA, request.inicio(), request.fin())) {
            throw new ReservaException(409, "El laboratorio ya tiene una reserva en ese intervalo");
        }
        return reservas.save(new Reserva(lab, request.solicitante().trim(), request.inicio(), request.fin()));
    }
    @Transactional public Reserva cancelar(long id) {
        Reserva reserva = obtener(id);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) return reserva;
        if (!reserva.getInicio().isAfter(java.time.LocalDateTime.now())) {
            throw new ReservaException(409, "No se puede cancelar una reserva que ya inició");
        }
        reserva.cancelar();
        return reserva;
    }
}
