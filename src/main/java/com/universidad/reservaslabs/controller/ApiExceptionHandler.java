package com.universidad.reservaslabs.controller;

import com.universidad.reservaslabs.exception.DatosReservaInvalidosException;
import com.universidad.reservaslabs.exception.RecursoNoEncontradoException;
import com.universidad.reservaslabs.exception.ReservaConflictException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ReservaApiController.class, LaboratorioApiController.class})
public class ApiExceptionHandler {
    @ExceptionHandler(ReservaConflictException.class)
    public ResponseEntity<Map<String, String>> conflict(ReservaConflictException e) { return response(HttpStatus.CONFLICT, e.getMessage()); }
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> missing(RecursoNoEncontradoException e) { return response(HttpStatus.NOT_FOUND, e.getMessage()); }
    @ExceptionHandler({DatosReservaInvalidosException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> invalid(Exception e) {
        String message = e instanceof MethodArgumentNotValidException validation
                ? validation.getBindingResult().getFieldErrors().stream().findFirst().map(err -> err.getField() + ": dato inválido").orElse("Solicitud inválida")
                : e.getMessage();
        return response(HttpStatus.BAD_REQUEST, message);
    }
    private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
