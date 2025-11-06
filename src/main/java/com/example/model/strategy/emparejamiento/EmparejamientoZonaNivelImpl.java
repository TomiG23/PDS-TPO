package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.strategy.tipoNivel.ITipoNivel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Estrategia de emparejamiento que filtra partidos por zona Y nivel.
 * Un jugador solo verá partidos de su zona que además coincidan con su nivel.
 */
public class EmparejamientoZonaNivelImpl implements IEmparejamientoEstrategia {
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;
        
        String zonaJugador = jugador.getZona() != null ? jugador.getZona().getNombre() : null;
        ITipoNivel nivelJ = jugador.getNivel();
        Integer valJ = (nivelJ != null) ? nivelJ.getValor() : null;
        
        for (Partido p : partidos) {
            // Verificar zona
            String zonaPartido = p.getUbicacion() != null ? p.getUbicacion().getNombre() : null;
            if (!Objects.equals(zonaJugador, zonaPartido)) {
                continue; // No es de la misma zona, saltar
            }
            
            // Verificar nivel
            ITipoNivel min = p.getMinNivel();
            ITipoNivel max = p.getMaxNivel();
            boolean nivelOk = true;
            
            if (min != null && valJ != null) {
                nivelOk &= valJ >= min.getValor();
            }
            if (max != null && valJ != null) {
                nivelOk &= valJ <= max.getValor();
            }
            // Si jugador no tiene nivel y el partido exige min/max, no lo incluimos
            if (valJ == null && (min != null || max != null)) {
                nivelOk = false;
            }
            
            if (nivelOk) {
                out.add(p);
            }
        }
        return out;
    }
}
