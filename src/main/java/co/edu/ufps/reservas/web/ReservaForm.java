package co.edu.ufps.reservas.web;

import java.time.LocalDateTime;

public class ReservaForm {
    private Long laboratorioId;
    private String solicitante;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    public Long getLaboratorioId() { return laboratorioId; }
    public void setLaboratorioId(Long laboratorioId) { this.laboratorioId = laboratorioId; }
    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
}
