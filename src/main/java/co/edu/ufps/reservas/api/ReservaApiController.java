package co.edu.ufps.reservas.api;

import co.edu.ufps.reservas.dominio.Reserva;
import co.edu.ufps.reservas.servicio.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApiController {
    private final ReservaService service;
    public ReservaApiController(ReservaService service) { this.service = service; }
    @GetMapping public List<Reserva> list() { return service.listar(); }
    @GetMapping("/{id}") public Reserva get(@PathVariable long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Reserva create(@RequestBody ReservaRequest request) { return service.crear(request); }
    @DeleteMapping("/{id}") public Reserva cancel(@PathVariable long id) { return service.cancelar(id); }
}
