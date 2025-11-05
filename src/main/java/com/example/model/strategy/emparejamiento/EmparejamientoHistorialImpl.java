package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmparejamientoHistorialImpl implements IEmparejamientoEstrategia {
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;

        // Suponemos que Historial expone pares de jugadores que ya jugaron juntos.
        // Como la clase está vacía, aquí hacemos una lógica de ejemplo:
        // Si el organizador o algún jugador del partido tiene el mismo nombre que alguien con quien el jugador ya jugó,
        // consideramos que hay match por historial.

        Set<String> conocidos = knownPlayersFromHistorial(jugador);

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

    private Set<String> knownPlayersFromHistorial(Jugador jugador) {
        // Placeholder: sin datos reales, devolvemos set vacío.
        return new HashSet<>();
    }
}
