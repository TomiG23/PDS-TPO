package com.example.model.State;

import com.example.model.entity.Partido;

public class PartidoArmado implements IEstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        System.out.println("[PartidoArmado] No se puede seguir agregando jugadores.");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[PartidoArmado] Confirmando partido...");
        partido.setEstado(new PartidoConfirmado());
    }

    @Override
    public void iniciarPartido(Partido partido) {
        System.out.println("[PartidoArmado] No se puede iniciar: el partido debe ser confirmado primero.");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        System.out.println("[PartidoArmado] No se puede finalizar: el partido no ha iniciado.");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        System.out.println("[PartidoArmado] Partido cancelado desde PartidoArmado.");
        partido.setEstado(new PartidoCancelado());
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[PartidoArmado] Concluye con errores.");
        partido.setTerminadoConErrores(true);
        partido.setEstado(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "PartidoArmado";
    }
}

