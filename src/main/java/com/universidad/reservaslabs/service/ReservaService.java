package com.universidad.reservaslabs.service;

import com.universidad.reservaslabs.exception.DatosReservaInvalidosException;
import com.universidad.reservaslabs.exception.RecursoNoEncontradoException;
import com.universidad.reservaslabs.exception.ReservaConflictException;
import com.universidad.reservaslabs.model.Laboratorio;
import com.universidad.reservaslabs.model.Reserva;
import com.universidad.reservaslabs.repository.LaboratorioRepository;
import com.universidad.reservaslabs.repository.ReservaRepository;
import java.time.Duration;
import java.time.LocalDateTime;
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
    @Transactional(readOnly = true) public List<Reserva> listarPorLaboratorio(long id) {
        if (!laboratorios.existsById(id)) throw new RecursoNoEncontradoException("Laboratorio no encontrado");
        return reservas.findAllByLaboratorioIdOrderByInicioAsc(id);
    }
    @Transactional(readOnly = true) public Reserva obtener(long id) {
        return reservas.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada"));
    }
    @Transactional(readOnly = true) public Laboratorio obtenerLaboratorio(long id) {
        return laboratorios.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado"));
    }
    @Transactional public Laboratorio crearLaboratorio(Laboratorio lab) {
        if (lab == null || blank(lab.getNombre()) || blank(lab.getUbicacion()) || blank(lab.getTipo()) || lab.getCapacidad() < 1)
            throw new DatosReservaInvalidosException("Nombre, ubicación, tipo y capacidad positiva son obligatorios");
        return laboratorios.save(lab);
    }
    @Transactional(readOnly = true) public List<Laboratorio> listarLaboratorios() { return laboratorios.findAll(); }

    @Transactional public Reserva crear(ReservaRequest request) {
        validar(request);
        Laboratorio laboratorio = obtenerLaboratorio(request.laboratorioId());
        if (!reservas.buscarSolapamientos(laboratorio.getId(), request.inicio(), request.fin()).isEmpty())
            throw new ReservaConflictException("El laboratorio ya tiene una reserva en ese intervalo");
        Reserva nueva = new Reserva(laboratorio, request.nombreSolicitante().trim(), request.correoSolicitante().trim(),
                request.inicio(), request.fin(), request.motivo().trim());
        return reservas.save(nueva);
    }
    private void validar(ReservaRequest r) {
        if (r == null || r.laboratorioId() == null || blank(r.nombreSolicitante()) || blank(r.correoSolicitante())
                || blank(r.motivo()) || r.inicio() == null || r.fin() == null)
            throw new DatosReservaInvalidosException("Laboratorio, nombre, correo, motivo, inicio y fin son obligatorios");
        if (!r.correoSolicitante().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new DatosReservaInvalidosException("El correo del solicitante no es válido");
        if (!r.fin().isAfter(r.inicio())) throw new DatosReservaInvalidosException("El fin debe ser posterior al inicio");
        long minutes = Duration.between(r.inicio(), r.fin()).toMinutes();
        if (minutes < 30 || minutes > 180) throw new DatosReservaInvalidosException("La duración debe estar entre 30 minutos y 3 horas");
        if (r.inicio().isBefore(LocalDateTime.now())) throw new DatosReservaInvalidosException("No se permiten reservas en horas pasadas");
        if (!r.inicio().toLocalDate().equals(r.fin().toLocalDate())) throw new DatosReservaInvalidosException("La reserva debe terminar el mismo día");
        LocalTime start = r.inicio().toLocalTime(), end = r.fin().toLocalTime();
        if (start.isBefore(LocalTime.of(7, 0)) || end.isAfter(LocalTime.of(21, 0)))
            throw new DatosReservaInvalidosException("El horario permitido es de 07:00 a 21:00");
    }
    private boolean blank(String s) { return s == null || s.isBlank(); }

    @Transactional public Reserva cancelar(long id) {
        Reserva reserva = obtener(id);
        if (reserva.getEstado() == com.universidad.reservaslabs.model.EstadoReserva.CANCELADA) return reserva;
        if (!reserva.getInicio().isAfter(LocalDateTime.now()))
            throw new ReservaConflictException("No se puede cancelar una reserva que ya inició");
        reserva.cancelar();
        return reserva;
    }
}
