package com.example.model.strategy.tipoNivel;

public class Intermedio implements ITipoNivel {
    @Override
    public String getNombre() {
        return "Intermedio";
    }

    @Override
    public int getValor() {
        return 2;
    }
}
