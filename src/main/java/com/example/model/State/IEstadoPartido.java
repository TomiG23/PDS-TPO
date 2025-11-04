package com.example.model.State;

public interface IEstadoPartido {
    void agregarJugadorAPartido(Partido partido);
    void confirmarPartido(Partido partido);
    void iniciarPartido(Partido partido);
    void finalizarPartido(Partido partido);
    void cancelarPartido(Partido partido);
    void concluirConErrores(Partido partido);
    String getNombreEstado();
}
