package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Deporte;
import com.example.model.entity.Habilidad;
import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.strategy.tipoNivel.ITipoNivel;
import com.example.model.strategy.tipoNivel.Principiante;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmparejamientoNivelImpl implements IEmparejamientoEstrategia {
    private Optional<Deporte> conoceDeporteDelPartido(List<Deporte> deportesJugador, Deporte deportePartido) {
        return deportesJugador.stream().filter(habilidad -> habilidad.getTipo().getClass() == deportePartido.getTipo().getClass()).findFirst();
    }

    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;

        Integer valorDeNivel;
        List<Habilidad> deportesJugador = jugador.getDeportes();
        for (Partido partido : partidos) {
            Deporte partidoDeporte = partido.getDeporte();
            Optional<Deporte> deporteConocido = conoceDeporteDelPartido(deportesJugador.stream().map(Habilidad::getDeporte).toList(), partidoDeporte);
            if (!deporteConocido.isEmpty()) {
                int index = deportesJugador.stream().map(Habilidad::getDeporte).toList().indexOf(deporteConocido.get());
                valorDeNivel = deportesJugador.get(index).getNivel().getValor();
            } else {
                valorDeNivel = new Principiante().getValor();
            }

            ITipoNivel min = partido.getMinNivel();
            ITipoNivel max = partido.getMaxNivel();
            boolean ok = true;
            if (min != null && valorDeNivel != null) {
                ok &= valorDeNivel >= min.getValor();
            }
            if (max != null && valorDeNivel != null) {
                ok &= valorDeNivel <= max.getValor();
            }
            if (valorDeNivel == null && (min != null || max != null)) ok = false;
            if (ok) out.add(partido);
        }
        return out;
    }

}
