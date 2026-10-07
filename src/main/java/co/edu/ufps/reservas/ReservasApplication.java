package co.edu.ufps.reservas;

import co.edu.ufps.reservas.dominio.Laboratorio;
import co.edu.ufps.reservas.repositorio.LaboratorioRepository;
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
                labs.save(new Laboratorio("LAB-101", "Laboratorio de sistemas 101", 24, true));
                labs.save(new Laboratorio("LAB-202", "Laboratorio de redes 202", 18, true));
            }
        };
    }
}
