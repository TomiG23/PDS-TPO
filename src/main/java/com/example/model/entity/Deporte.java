package com.example.model.entity;

import com.example.model.strategy.tipoDeporte.tipoNivel.ITipoDeporte;

public class Deporte {
    private ITipoDeporte tipoDeporte;
    public Deporte(ITipoDeporte tipoDeporte) {
        establecerTipo(tipoDeporte);
    }
    public String getNombre() {
        return this.tipoDeporte.verTipo();
    }
    public ITipoDeporte getTipo() {
        return tipoDeporte;
    }
    private void establecerTipo(ITipoDeporte tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    };

    public Deporte getDeporte() {
        return null;
    }
}
