package com.example.model.entity;

import com.example.model.strategy.tipoNivel.Avanzado;
import com.example.model.strategy.tipoNivel.ITipoNivel;
import com.example.model.strategy.tipoNivel.Intermedio;
import com.example.model.strategy.tipoNivel.Principiante;

public class Habilidad {
    private Deporte deporte;
    private ITipoNivel nivel;

    public Habilidad() { }

    public Habilidad(Deporte deporte, ITipoNivel nivel) {
        this.deporte = deporte;
        this.nivel = nivel;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public ITipoNivel getNivel() {
        return nivel;
    }

    public ITipoNivel seleccionarNivel(int val) {
        return switch (val) {
            case 1 -> new Principiante();
            case 2 -> new Intermedio();
            case 3 -> new Avanzado();
            default -> null;
        };
    }
}
