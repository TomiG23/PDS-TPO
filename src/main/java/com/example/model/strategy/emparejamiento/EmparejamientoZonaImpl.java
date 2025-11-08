package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmparejamientoZonaImpl implements IEmparejamientoEstrategia {
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;
        for (Partido p : partidos) {
            String zonaPartido = p.getUbicacion() != null ? p.getUbicacion().getNombre() : null;
        }
        return out;
    }
}
