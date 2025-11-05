package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.strategy.tipoNivel.ITipoNivel;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoNivelImpl implements IEmparejamientoEstrategia {
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;
        ITipoNivel nivelJ = jugador.getNivel();
        Integer valJ = (nivelJ != null) ? nivelJ.getValor() : null;
        for (Partido p : partidos) {
            ITipoNivel min = p.getMinNivel();
            ITipoNivel max = p.getMaxNivel();
            boolean ok = true;
            if (min != null && valJ != null) {
                ok &= valJ >= min.getValor();
            }
            if (max != null && valJ != null) {
                ok &= valJ <= max.getValor();
            }
            // Si jugador no tiene nivel y el partido exige min/max, no lo incluimos
            if (valJ == null && (min != null || max != null)) ok = false;
            if (ok) out.add(p);
        }
        return out;
    }
}
