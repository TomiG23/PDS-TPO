package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.entity.Ubicacion;
import com.example.util.CalculadoraDistancias;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Estrategia de emparejamiento basada en la proximidad geográfica.
 * Filtra los partidos que están dentro de un radio de 3km de la ubicación del jugador.
 */
public class EmparejamientoZonaImpl implements IEmparejamientoEstrategia {
    
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> partidosCercanos = new ArrayList<>();
        if (partidos == null || jugador == null) {
            return partidosCercanos;
        }

        Ubicacion ubicacionJugador = jugador.getUbicacion();
        if (ubicacionJugador == null) {
            return partidosCercanos;
        }

        for (Partido partido : partidos) {
            Ubicacion ubicacionPartido = partido.getUbicacion();
            if (ubicacionPartido != null && 
                CalculadoraDistancias.estanCercanas(ubicacionJugador, ubicacionPartido)) {
                partidosCercanos.add(partido);
            }
        }
        
        return partidosCercanos;
    }
}
