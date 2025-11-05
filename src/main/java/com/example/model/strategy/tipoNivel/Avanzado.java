package com.example.model.strategy.tipoNivel;

public class Avanzado implements ITipoNivel {
    @Override
    public String getNombre() {
        return "Avanzado";
    }

    @Override
    public int getValor() {
        return 3;
    }
}
