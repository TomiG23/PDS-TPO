package com.example.model.State;

import com.example.model.entity.Partido;

public class PartidoConfirmado implements IEstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        System.out.println("[PartidoConfirmado] No se recomienda agregar jugadores después de la confirmación, pero es posible.");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[PartidoConfirmado] El partido ya está confirmado.");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        System.out.println("[PartidoConfirmado] Iniciando partido...");
        partido.setEstado(new PartidoEnJuego());
    }

    @Override
    public void finalizarPartido(Partido partido) {
        System.out.println("[PartidoConfirmado] No se puede finalizar: el partido no ha iniciado.");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        System.out.println("[PartidoConfirmado] Partido cancelado desde PartidoConfirmado.");
        partido.setEstado(new PartidoCancelado());
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[PartidoConfirmado] Concluye con errores.");
        partido.setTerminadoConErrores(true);
        partido.setEstado(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "PartidoConfirmado";
    }
}


