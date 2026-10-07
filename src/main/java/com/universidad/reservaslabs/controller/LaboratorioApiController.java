package com.universidad.reservaslabs.controller;

import com.universidad.reservaslabs.model.Laboratorio;
import com.universidad.reservaslabs.service.ReservaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioApiController {
    private final ReservaService service;
    public LaboratorioApiController(ReservaService service) { this.service = service; }
    @GetMapping public List<Laboratorio> listar() { return service.listarLaboratorios(); }
    @GetMapping("/{id}") public Laboratorio obtener(@PathVariable long id) { return service.obtenerLaboratorio(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Laboratorio crear(@Valid @RequestBody Laboratorio laboratorio) {
        return service.crearLaboratorio(laboratorio);
    }
}
