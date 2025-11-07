package com.example.model.entity;

import java.util.Objects;

public class Zona {
    private String nombre;
    private String codigo;
    private String ubicacion; // Dirección o lugar específico (ej. "Estadio Luna Park")

    public Zona() {}

    public Zona(String nombre) {
        this.nombre = capitalizar(nombre);
    }

    public Zona(String nombre, String codigo) {
        this.nombre = capitalizar(nombre);
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = capitalizar(nombre);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Capitaliza la primera letra de cada palabra en el nombre de la zona.
     * Por ejemplo: "norte" -> "Norte", "zona sur" -> "Zona Sur"
     */
    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < palabras.length; i++) {
            String palabra = palabras[i];
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)));
                if (palabra.length() > 1) {
                    resultado.append(palabra.substring(1).toLowerCase());
                }
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }
        
        return resultado.toString();
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
