package com.universidad.reservaslabs.web;

import com.universidad.reservaslabs.service.ReservaRequest;
import com.universidad.reservaslabs.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservaPageController {
    private final ReservaService reservas;
    public ReservaPageController(ReservaService reservas) { this.reservas = reservas; }
    @GetMapping("/reservas") public String listar(Model model) {
        model.addAttribute("reservas", reservas.listar());
        return "reservas/lista";
    }
    @GetMapping("/reservas/nueva") public String formulario(Model model) {
        model.addAttribute("laboratorios", reservas.listarLaboratorios());
        model.addAttribute("form", new ReservaForm());
        return "reservas/nueva";
    }
    @PostMapping("/reservas") public String crear(@ModelAttribute("form") ReservaForm form) {
        reservas.crear(new ReservaRequest(form.getLaboratorioId(), form.getNombreSolicitante(), form.getCorreoSolicitante(),
                form.getInicio(), form.getFin(), form.getMotivo()));
        return "redirect:/reservas?creada";
    }
    @PostMapping("/reservas/{id}/cancelar") public String cancelar(@PathVariable long id) {
        reservas.cancelar(id);
        return "redirect:/reservas?cancelada";
    }
}
