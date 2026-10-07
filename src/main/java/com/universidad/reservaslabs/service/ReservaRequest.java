package com.universidad.reservaslabs.service;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservaRequest(@NotNull Long laboratorioId, @NotBlank String nombreSolicitante,
                             @NotBlank @Email String correoSolicitante, @NotNull LocalDateTime inicio,
                             @NotNull LocalDateTime fin, @NotBlank String motivo) { }
