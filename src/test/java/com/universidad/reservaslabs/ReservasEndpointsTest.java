package com.universidad.reservaslabs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.universidad.reservaslabs.service.ReservaRequest;
import com.universidad.reservaslabs.service.ReservaService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest @AutoConfigureMockMvc @Transactional
class ReservasEndpointsTest {
    @Autowired MockMvc mvc;
    @Autowired ReservaService service;

    @Test void labCatalogIsAvailableAsJsonAndMvcForm() throws Exception {
        mvc.perform(get("/api/laboratorios"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].ubicacion").exists());
        mvc.perform(get("/reservas/nueva"))
                .andExpect(status().isOk()).andExpect(view().name("reservas/nueva"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Correo electrónico")));
    }

    @Test void restAndMvcReturnTheSameOverlapDomainMessage() throws Exception {
        Long labId = service.listarLaboratorios().get(0).getId();
        LocalDate date = LocalDate.now().plusDays(40);
        LocalDateTime start = date.atTime(9, 0), end = date.atTime(10, 0);
        service.crear(new ReservaRequest(labId, "Juan", "juan@example.edu.co", start, end, "Prueba"));

        mvc.perform(post("/api/reservas").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"laboratorioId":%d,"nombreSolicitante":"Ana","correoSolicitante":"ana@example.edu.co","inicio":"%s","fin":"%s","motivo":"Prueba de colisión"}
                                """.formatted(labId, start.plusMinutes(30), end.plusMinutes(30))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El laboratorio ya tiene una reserva en ese intervalo"));

        mvc.perform(post("/reservas").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("laboratorioId", labId.toString())
                        .param("nombreSolicitante", "Ana")
                        .param("correoSolicitante", "ana@example.edu.co")
                        .param("inicio", start.plusMinutes(30).toString())
                        .param("fin", end.plusMinutes(30).toString())
                        .param("motivo", "Prueba de colisión"))
                .andExpect(status().isOk()).andExpect(view().name("reservas/error"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("El laboratorio ya tiene una reserva en ese intervalo")));
    }
}
