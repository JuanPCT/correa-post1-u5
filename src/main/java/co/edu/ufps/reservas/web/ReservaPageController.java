package co.edu.ufps.reservas.web;

import co.edu.ufps.reservas.repositorio.LaboratorioRepository;
import co.edu.ufps.reservas.servicio.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservaPageController {
    private final ReservaService reservas;
    private final LaboratorioRepository laboratorios;
    public ReservaPageController(ReservaService reservas, LaboratorioRepository laboratorios) {
        this.reservas = reservas; this.laboratorios = laboratorios;
    }
    @GetMapping("/reservas") public String list(Model model) {
        model.addAttribute("reservas", reservas.listar());
        return "reservas/lista";
    }
    @GetMapping("/reservas/nueva") public String form(Model model) {
        model.addAttribute("laboratorios", laboratorios.findAll());
        model.addAttribute("form", new ReservaForm());
        return "reservas/nueva";
    }
    @PostMapping("/reservas") public String create(@ModelAttribute("form") ReservaForm form, Model model) {
        try {
            reservas.crear(new ReservaRequest(form.getLaboratorioId(), form.getSolicitante(), form.getInicio(), form.getFin()));
            return "redirect:/reservas?creada";
        } catch (ReservaException exception) {
            model.addAttribute("laboratorios", laboratorios.findAll());
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("form", form);
            return "reservas/nueva";
        }
    }
    @PostMapping("/reservas/{id}/cancelar") public String cancel(@PathVariable long id, Model model) {
        try { reservas.cancelar(id); return "redirect:/reservas?cancelada"; }
        catch (ReservaException exception) {
            model.addAttribute("reservas", reservas.listar()); model.addAttribute("error", exception.getMessage());
            return "reservas/lista";
        }
    }
}
