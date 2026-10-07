package co.edu.ufps.reservas.repositorio;

import co.edu.ufps.reservas.dominio.EstadoReserva;
import co.edu.ufps.reservas.dominio.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("select count(r) > 0 from Reserva r where r.laboratorio.id = :labId " +
           "and r.estado = :estado and r.inicio < :fin and r.fin > :inicio")
    boolean existeSolapamiento(@Param("labId") Long labId, @Param("estado") EstadoReserva estado,
                               @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    List<Reserva> findAllByOrderByInicioAsc();
}
