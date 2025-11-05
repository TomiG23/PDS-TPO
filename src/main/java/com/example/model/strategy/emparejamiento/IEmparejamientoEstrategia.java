package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import java.util.List;

public interface IEmparejamientoEstrategia {
    List<Partido> emparejar(List<Partido> partidos, Jugador jugador);
}
