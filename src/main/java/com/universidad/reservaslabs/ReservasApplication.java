package com.universidad.reservaslabs;

import com.universidad.reservaslabs.model.Laboratorio;
import com.universidad.reservaslabs.repository.LaboratorioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReservasApplication {
    public static void main(String[] args) { SpringApplication.run(ReservasApplication.class, args); }

    @Bean CommandLineRunner seed(LaboratorioRepository labs) {
        return args -> {
            if (labs.count() == 0) {
                labs.save(new Laboratorio("Laboratorio de Redes", "Bloque A · 101", 24, "Redes"));
                labs.save(new Laboratorio("Laboratorio de Sistemas", "Bloque B · 202", 30, "Sistemas"));
            }
        };
    }
}
