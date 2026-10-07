package com.universidad.reservaslabs.web;

import com.universidad.reservaslabs.exception.DatosReservaInvalidosException;
import com.universidad.reservaslabs.exception.RecursoNoEncontradoException;
import com.universidad.reservaslabs.exception.ReservaConflictException;
import com.universidad.reservaslabs.service.ReservaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = ReservaPageController.class)
public class ReservaPageExceptionHandler {
    private final ReservaService reservas;
    public ReservaPageExceptionHandler(ReservaService reservas) { this.reservas = reservas; }

    @ExceptionHandler({ReservaConflictException.class, RecursoNoEncontradoException.class, DatosReservaInvalidosException.class})
    public String handle(RuntimeException exception, HttpServletRequest request, Model model) {
        model.addAttribute("error", exception.getMessage());
        model.addAttribute("volver", request.getRequestURI().startsWith("/reservas/nueva") ? "/reservas/nueva" : "/reservas");
        return "reservas/error";
    }
}
