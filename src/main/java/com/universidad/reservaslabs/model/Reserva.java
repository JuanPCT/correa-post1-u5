package com.universidad.reservaslabs.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "idx_reserva_lab_inicio", columnList = "laboratorio_id,inicio"))
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private Laboratorio laboratorio;
    @Column(nullable = false) private String nombreSolicitante;
    @Column(nullable = false) private String correoSolicitante;
    @Column(nullable = false) private LocalDateTime inicio;
    @Column(nullable = false) private LocalDateTime fin;
    @Column(nullable = false, length = 500) private String motivo;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoReserva estado;

    protected Reserva() { }
    public Reserva(Laboratorio laboratorio, String nombreSolicitante, String correoSolicitante,
                   LocalDateTime inicio, LocalDateTime fin, String motivo) {
        this.laboratorio = laboratorio; this.nombreSolicitante = nombreSolicitante;
        this.correoSolicitante = correoSolicitante; this.inicio = inicio; this.fin = fin;
        this.motivo = motivo; this.estado = EstadoReserva.PENDIENTE;
    }
    public Long getId() { return id; }
    public Laboratorio getLaboratorio() { return laboratorio; }
    public String getNombreSolicitante() { return nombreSolicitante; }
    public String getCorreoSolicitante() { return correoSolicitante; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public String getMotivo() { return motivo; }
    public EstadoReserva getEstado() { return estado; }
    public void cancelar() { this.estado = EstadoReserva.CANCELADA; }
}
