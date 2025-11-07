package com.example.model.strategy.emparejamiento;

import com.example.model.entity.*;
import com.example.model.strategy.tipoNivel.ITipoNivel;
import com.example.model.strategy.tipoNivel.Principiante;

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

        List<Habilidad> deportesJugador = jugador.getDeportes();

        for (Partido p : partidos) {
            // Verificar zona
            String zonaPartido = p.getUbicacion() != null ? p.getUbicacion().getNombre() : null;
            if (!Objects.equals(zonaBuscada, zonaPartido)) {
                continue; // No es de la zona buscada, saltar
            }
            
            Deporte partidoDeporte = p.getDeporte();
            Integer valorDeNivel = null;

            if (deportesJugador.stream().map(Habilidad::getDeporte).toList().contains(partidoDeporte)) {
                Integer index = deportesJugador.stream().map(Habilidad::getDeporte).toList().indexOf(partidoDeporte);
                valorDeNivel = deportesJugador.get(index).getNivel().getValor();
            } else {
                valorDeNivel = new Principiante().getValor();
            }
            
            // Verificar nivel
            ITipoNivel min = p.getMinNivel();
            ITipoNivel max = p.getMaxNivel();
            boolean nivelOk = true;
            
            if (min != null && valorDeNivel != null) {
                nivelOk &= valorDeNivel >= min.getValor();
            }
            if (max != null && valorDeNivel != null) {
                nivelOk &= valorDeNivel <= max.getValor();
            }
            // Si jugador no tiene nivel y el partido exige min/max, no lo incluimos
            if (valorDeNivel == null && (min != null || max != null)) {
                nivelOk = false;
            }
            
            if (nivelOk) {
                out.add(p);
            }
        }
        return out;
    }
}
