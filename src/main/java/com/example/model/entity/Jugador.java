package com.example.model.entity;

import com.example.model.strategy.tipoNivel.ITipoNivel;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private String mail;
    private String password;
    private List<Habilidad> deportes = new ArrayList<>();
    private Habilidad deporteFavorito;
    private String pushToken;

    public Jugador(String nombre, String mail, String password) {
        this.nombre = nombre;
        this.mail = mail;
        this.password = password;
    }

    public Jugador(String nombre, String mail, String password, Habilidad deporteFavorito) {
        this.nombre = nombre;
        this.mail = mail;
        this.password = password;
        setDeporteFavorito(deporteFavorito);
    }

    public String getNombre() {
        return nombre;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public Deporte getDeporteFavorito() {
        return deporteFavorito.getDeporte();
    }

    public void setDeporteFavorito(Habilidad deporteFavorito) {
        this.deporteFavorito = deporteFavorito;
        deportes.add(deporteFavorito);
    }

    public void agregarDeporte(Habilidad habilidad) {
        if (!deportes.contains(habilidad)) {
            this.deportes.add(habilidad);
        }
    }

    public List<Habilidad> getDeportes() {
        return deportes;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
