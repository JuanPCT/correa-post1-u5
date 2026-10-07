package com.universidad.reservaslabs.web;

import java.time.LocalDateTime;

public class ReservaForm {
    private Long laboratorioId;
    private String nombreSolicitante;
    private String correoSolicitante;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String motivo;
    public Long getLaboratorioId() { return laboratorioId; }
    public void setLaboratorioId(Long laboratorioId) { this.laboratorioId = laboratorioId; }
    public String getNombreSolicitante() { return nombreSolicitante; }
    public void setNombreSolicitante(String nombreSolicitante) { this.nombreSolicitante = nombreSolicitante; }
    public String getCorreoSolicitante() { return correoSolicitante; }
    public void setCorreoSolicitante(String correoSolicitante) { this.correoSolicitante = correoSolicitante; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
