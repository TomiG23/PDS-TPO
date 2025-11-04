package com.example.model.State;

public class PartidoEnJuego implements EstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        System.out.println("[PartidoEnJuego] No se pueden agregar jugadores mientras el partido está en curso.");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[PartidoEnJuego] El partido ya está en juego.");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        System.out.println("[PartidoEnJuego] El partido ya ha iniciado.");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        System.out.println("[PartidoEnJuego] Finalizando partido... ");
        partido.setEstadoActual(new PartidoFinalizado());
    }

    @Override
    public void cancelarPartido(Partido partido) {
        System.out.println("[PartidoEnJuego] No se recomienda cancelar un partido en curso; se cancelará y pasará a PartidoCancelado.");
        partido.setEstadoActual(new PartidoCancelado());
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[PartidoEnJuego] Concluye con errores durante el juego.");
        partido.setTerminadoConErrores(true);
        partido.setEstadoActual(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "PartidoEnJuego";
    }
}

