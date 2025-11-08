package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.model.entity.Historial;

public class EmparejamientoHistorialImpl implements IEmparejamientoEstrategia {
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;
        Set<String> conocidos = Historial.getInstance().getNombresConocidos(jugador);
        for (Partido p : partidos) {
            Jugador org = p.getOrganizador();
            boolean match = false;
            if (org != null && conocidos.contains(org.getNombre())) match = true;
            if (!match && p.getJugadores() != null) {
                for (Jugador j : p.getJugadores()) {
                    if (j != null && conocidos.contains(j.getNombre())) { match = true; break; }
                }
            }
            if (match) out.add(p);
        }
        return out;
    }
}
