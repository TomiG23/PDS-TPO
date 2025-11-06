package com.example.model.entity;

import java.util.Objects;

public class Ubicacion {
    private double latitud;
    private double longitud;
    private String direccion;
    private String referencia;

    public Ubicacion() {}

    public Ubicacion(double latitud, double longitud, String direccion, String referencia) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.referencia = referencia;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ubicacion ubicacion = (Ubicacion) o;
        return Double.compare(ubicacion.latitud, latitud) == 0 &&
               Double.compare(ubicacion.longitud, longitud) == 0 &&
               Objects.equals(direccion, ubicacion.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitud, longitud, direccion);
    }

    @Override
    public String toString() {
        return String.format("Ubicacion{latitud=%.6f, longitud=%.6f, direccion='%s', referencia='%s'}",
                latitud, longitud, direccion, referencia);
    }
}
