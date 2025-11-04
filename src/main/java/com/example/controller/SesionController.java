package com.example.controller;

import com.example.dto.AccesoDTO;

public class SesionController {
    private AccesoDTO usuario;

    public void ingresar(AccesoDTO usuario) {
        this.usuario = usuario;
    }

    public void salir(AccesoDTO usuario) {
        this.usuario = usuario;
    }
}
