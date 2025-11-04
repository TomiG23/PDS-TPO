package com.example.model.State;

import com.example.model.Partido;

public class PartidoFinalizado implements EstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        System.out.println("[PartidoFinalizado] No se pueden agregar jugadores: el partido ya finalizó.");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[PartidoFinalizado] No se puede confirmar: el partido ya finalizó.");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        System.out.println("[PartidoFinalizado] No se puede iniciar: el partido ya finalizó.");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        System.out.println("[PartidoFinalizado] El partido ya está finalizado.");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        System.out.println("[PartidoFinalizado] No se puede cancelar: el partido ya finalizó.");
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[PartidoFinalizado] Ya finalizado. Marcando que terminó con errores.");
        partido.setTerminadoConErrores(true);
    }

    @Override
    public String getNombreEstado() {
        return "PartidoFinalizado";
    }
}

