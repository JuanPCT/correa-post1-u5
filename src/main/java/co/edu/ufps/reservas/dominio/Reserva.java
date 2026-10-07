package co.edu.ufps.reservas.dominio;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(indexes = @Index(name = "idx_reserva_laboratorio_inicio", columnList = "laboratorio_id,inicio"))
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Laboratorio laboratorio;
    @Column(nullable = false) private String solicitante;
    @Column(nullable = false) private LocalDateTime inicio;
    @Column(nullable = false) private LocalDateTime fin;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoReserva estado = EstadoReserva.ACTIVA;
    protected Reserva() { }
    public Reserva(Laboratorio laboratorio, String solicitante, LocalDateTime inicio, LocalDateTime fin) {
        this.laboratorio = laboratorio; this.solicitante = solicitante; this.inicio = inicio; this.fin = fin;
    }
    public Long getId() { return id; }
    public Laboratorio getLaboratorio() { return laboratorio; }
    public String getSolicitante() { return solicitante; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public EstadoReserva getEstado() { return estado; }
    public void cancelar() { estado = EstadoReserva.CANCELADA; }
}
