package com.example.model.entity;

import java.util.Objects;

public class Zona {
    private String nombre;
    private String codigo;

    public Zona() {}

    public Zona(String nombre) {
        this.nombre = nombre;
    }

    public Zona(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zona)) return false;
        Zona zona = (Zona) o;
        return Objects.equals(nombre, zona.nombre) && Objects.equals(codigo, zona.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, codigo);
    }
}
