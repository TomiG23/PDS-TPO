package com.example.model.State;

public class PartidoCancelado implements EstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        System.out.println("[PartidoCancelado] No se puede agregar jugadores: el partido está cancelado.");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[PartidoCancelado] No se puede confirmar: el partido fue cancelado.");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        System.out.println("[PartidoCancelado] No se puede iniciar: el partido fue cancelado.");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        System.out.println("[PartidoCancelado] No se puede finalizar: el partido fue cancelado.");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        System.out.println("[PartidoCancelado] El partido ya está cancelado.");
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[PartidoCancelado] Concluir con errores desde partido cancelado.");
        partido.setTerminadoConErrores(true);
        partido.setEstadoActual(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "PartidoCancelado";
    }
}

