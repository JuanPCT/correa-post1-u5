package com.universidad.reservaslabs.repository;

import com.universidad.reservaslabs.model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("select r from Reserva r where r.laboratorio.id = :laboratorioId " +
           "and r.estado in (com.universidad.reservaslabs.model.EstadoReserva.PENDIENTE, " +
           "com.universidad.reservaslabs.model.EstadoReserva.CONFIRMADA) " +
           "and r.inicio < :fin and r.fin > :inicio")
    List<Reserva> buscarSolapamientos(@Param("laboratorioId") Long laboratorioId,
                                      @Param("inicio") LocalDateTime inicio,
                                      @Param("fin") LocalDateTime fin);

    List<Reserva> findAllByOrderByInicioAsc();
    List<Reserva> findAllByLaboratorioIdOrderByInicioAsc(Long laboratorioId);
}
