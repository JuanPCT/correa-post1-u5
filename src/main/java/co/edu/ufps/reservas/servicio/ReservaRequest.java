package co.edu.ufps.reservas.servicio;

import java.time.LocalDateTime;

public record ReservaRequest(Long laboratorioId, String solicitante, LocalDateTime inicio, LocalDateTime fin) { }
