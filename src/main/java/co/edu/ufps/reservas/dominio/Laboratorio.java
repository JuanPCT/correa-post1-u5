package co.edu.ufps.reservas.dominio;

import jakarta.persistence.*;

@Entity
public class Laboratorio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String codigo;
    @Column(nullable = false) private String nombre;
    private int capacidad;
    private boolean activo;
    protected Laboratorio() { }
    public Laboratorio(String codigo, String nombre, int capacidad, boolean activo) {
        this.codigo = codigo; this.nombre = nombre; this.capacidad = capacidad; this.activo = activo;
    }
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCapacidad() { return capacidad; }
    public boolean isActivo() { return activo; }
}
