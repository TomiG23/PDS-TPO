package com.example.model.State;

import com.example.model.entity.Partido;

public class NecesitamosJugadores implements IEstadoPartido {
    @Override
    public void agregarJugadorAPartido(Partido partido) {
        int n = partido.getJugadores().size();
        System.out.println("[NecesitamosJugadores] Hay " + n + " jugador(es). Al agregar uno, se revisará si pasa a PartidoArmado.");
        if (n == partido.getJugadoresRequeridos()) {
            partido.setEstado(new PartidoArmado());
        } else {
            System.out.println("[NecesitamosJugadores] Aún faltan jugadores para armar el partido.");
        }
        System.out.println("Se ha agregado al partido correctamente!");
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
        partido.setEstado(new PartidoCancelado());
    }

    @Override
    public void concluirConErrores(Partido partido) {
        System.out.println("[NecesitamosJugadores] Concluye con errores.");
        partido.setTerminadoConErrores(true);
        partido.setEstado(new PartidoFinalizado());
    }

    @Override
    public String getNombreEstado() {
        return "NecesitamosJugadores";
    }
}

