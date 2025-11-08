package com.example.model.strategy.emparejamiento;

import com.example.model.entity.*;
import com.example.model.strategy.tipoNivel.ITipoNivel;
import com.example.model.strategy.tipoNivel.Principiante;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Estrategia de emparejamiento que filtra partidos por una zona específica Y nivel del jugador.
 * Permite especificar una zona personalizada en lugar de usar la zona del jugador.
 */
public class EmparejamientoZonaNivelCustomImpl implements IEmparejamientoEstrategia {
    private final Zona zonaCustom;
    
    public EmparejamientoZonaNivelCustomImpl(Zona zonaCustom) {
        this.zonaCustom = zonaCustom;
    }

    private Optional<Deporte> conoceDeporteDelPartido(List<Deporte> deportesJugador, Deporte deportePartido) {
        return deportesJugador.stream().filter(habilidad -> habilidad.getTipo().getClass() == deportePartido.getTipo().getClass()).findFirst();
    }
    
    @Override
    public List<Partido> emparejar(List<Partido> partidos, Jugador jugador) {
        List<Partido> out = new ArrayList<>();
        if (partidos == null || jugador == null) return out;
        
        String zonaBuscada = zonaCustom != null ? zonaCustom.getNombre() : null;

        List<Habilidad> deportesJugador = jugador.getDeportes();
        Integer valorDeNivel;
        for (Partido p : partidos) {
            String zonaPartido = p.getUbicacion() != null ? p.getUbicacion().getNombre() : null;
            if (!Objects.equals(zonaBuscada, zonaPartido)) {
                continue;
            }
            
            Deporte partidoDeporte = p.getDeporte();
            Optional<Deporte> deporteConocido = conoceDeporteDelPartido(deportesJugador.stream().map(Habilidad::getDeporte).toList(), partidoDeporte);

            if (!deporteConocido.isEmpty()) {
                int index = deportesJugador.stream().map(Habilidad::getDeporte).toList().indexOf(deporteConocido.get());
                valorDeNivel = deportesJugador.get(index).getNivel().getValor();
            } else {
                valorDeNivel = new Principiante().getValor();
            }
            
            ITipoNivel min = p.getMinNivel();
            ITipoNivel max = p.getMaxNivel();
            boolean nivelOk = true;
            
            if (min != null && valorDeNivel != null) {
                nivelOk &= valorDeNivel >= min.getValor();
            }
            if (max != null && valorDeNivel != null) {
                nivelOk &= valorDeNivel <= max.getValor();
            }
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
