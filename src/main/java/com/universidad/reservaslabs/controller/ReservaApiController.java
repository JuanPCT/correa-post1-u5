package com.universidad.reservaslabs.controller;

import com.universidad.reservaslabs.model.Reserva;
import com.universidad.reservaslabs.service.ReservaRequest;
import com.universidad.reservaslabs.service.ReservaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApiController {
    private final ReservaService service;
    public ReservaApiController(ReservaService service) { this.service = service; }
    @GetMapping public List<Reserva> listar() { return service.listar(); }
    @GetMapping("/{id}") public Reserva obtener(@PathVariable long id) { return service.obtener(id); }
    @GetMapping("/laboratorio/{id}") public List<Reserva> porLaboratorio(@PathVariable long id) { return service.listarPorLaboratorio(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Reserva crear(@Valid @RequestBody ReservaRequest request) { return service.crear(request); }
    @DeleteMapping("/{id}") public Reserva cancelar(@PathVariable long id) { return service.cancelar(id); }
}
