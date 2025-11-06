package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.entity.Zona;
import com.example.model.strategy.tipoNivel.ITipoNivel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Estrategia de emparejamiento que filtra partidos por una zona específica Y nivel del jugador.
 * Permite especificar una zona personalizada en lugar de usar la zona del jugador.
 */
public class EmparejamientoZonaNivelCustomImpl implements IEmparejamientoEstrategia {
    private final Zona zonaCustom;
    
    public EmparejamientoZonaNivelCustomImpl(Zona zonaCustom) {
        this.zonaCustom = zonaCustom;
    }
    
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;
        
        String zonaBuscada = zonaCustom != null ? zonaCustom.getNombre() : null;
        ITipoNivel nivelJ = jugador.getNivel();
        Integer valJ = (nivelJ != null) ? nivelJ.getValor() : null;
        
        for (Partido p : partidos) {
            // Verificar zona
            String zonaPartido = p.getUbicacion() != null ? p.getUbicacion().getNombre() : null;
            if (!Objects.equals(zonaBuscada, zonaPartido)) {
                continue; // No es de la zona buscada, saltar
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
