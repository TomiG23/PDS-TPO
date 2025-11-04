package com.example.model.State;

import com.example.model.Partido;

/**
 * Interfaz EstadoPartido con los métodos requeridos.
 */
public interface EstadoPartido {
    void agregarJugadorAPartido(Partido partido);
    void confirmarPartido(Partido partido);
    void iniciarPartido(Partido partido);
    void finalizarPartido(Partido partido);
    void cancelarPartido(Partido partido);
    void concluirConErrores(Partido partido);
    String getNombreEstado();
}

