package com.example.model.State;

public class NecesitamosJugadores implements EstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        int n = partido.getJugadores().size();
        System.out.println("[NecesitamosJugadores] Hay " + n + " jugador(es). Al agregar uno, se revisará si pasa a PartidoArmado.");
        if (n >= 2) {
            partido.setEstadoActual(new PartidoArmado());
        } else {
            System.out.println("[NecesitamosJugadores] Aún faltan jugadores para armar el partido.");
        }
    }

    @Override
    public void confirmarPartido(Partido partido) {
        System.out.println("[NecesitamosJugadores] No se puede confirmar: aún no hay suficientes jugadores.");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        System.out.println("[NecesitamosJugadores] No se puede iniciar: partido no armado.");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        System.out.println("[NecesitamosJugadores] No se puede finalizar: partido no iniciado.");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        System.out.println("[NecesitamosJugadores] Partido cancelado desde estado NecesitamosJugadores.");
        partido.setEstadoActual(new PartidoCancelado());
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[NecesitamosJugadores] Concluye con errores.");
        partido.setTerminadoConErrores(true);
        partido.setEstadoActual(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "NecesitamosJugadores";
    }
}

