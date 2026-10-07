package co.edu.ufps.reservas.api;

import co.edu.ufps.reservas.servicio.ReservaException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ReservaApiController.class)
public class ApiExceptionHandler {
    @ExceptionHandler(ReservaException.class)
    public ResponseEntity<Map<String, String>> handle(ReservaException exception) {
        return ResponseEntity.status(exception.status()).body(Map.of("error", exception.getMessage()));
    }
}
