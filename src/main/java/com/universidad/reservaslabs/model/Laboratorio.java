package com.universidad.reservaslabs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Laboratorio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false) private String nombre;
    @NotBlank @Column(nullable = false) private String ubicacion;
    @Min(1) private int capacidad;
    @NotBlank @Column(nullable = false) private String tipo;

    protected Laboratorio() { }
    public Laboratorio(String nombre, String ubicacion, int capacidad, String tipo) {
        this.nombre = nombre; this.ubicacion = ubicacion; this.capacidad = capacidad; this.tipo = tipo;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public int getCapacidad() { return capacidad; }
    public String getTipo() { return tipo; }
}
