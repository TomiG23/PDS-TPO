package com.example.model.strategy.tipoNivel;

public class Principiante implements ITipoNivel {
    @Override
    public String getNombre() {
        return "Principiante";
    }

    @Override
    public int getValor() {
        return 1;
    }
}
