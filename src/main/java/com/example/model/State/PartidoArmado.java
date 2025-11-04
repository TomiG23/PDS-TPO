package com.example.model.State;

public class PartidoArmado implements EstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        System.out.println("[PartidoArmado] Se puede seguir agregando jugadores. Estado permanece PartidoArmado.");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[PartidoArmado] Confirmando partido...");
        partido.setEstadoActual(new PartidoConfirmado());
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
        partido.setEstadoActual(new PartidoCancelado());
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[PartidoArmado] Concluye con errores.");
        partido.setTerminadoConErrores(true);
        partido.setEstadoActual(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "PartidoArmado";
    }
}

