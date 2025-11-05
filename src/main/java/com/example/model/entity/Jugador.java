package com.example.model.entity;

import com.example.model.strategy.tipoNivel.ITipoNivel;

public class Jugador {
    private String nombre;
    private String mail;
    private String password;
    private int edad;
    private Deporte deporteFavorito;
    private Zona zona;
    private String pushToken;
    private ITipoNivel nivel;

    public Jugador() {}

    public Jugador(String nombre, String mail, String password, int edad, Deporte deporteFavorito, Zona zona) {
        this.nombre = nombre;
        this.mail = mail;
        this.password = password;
        this.edad = edad;
        this.deporteFavorito = deporteFavorito;
        this.zona = zona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Deporte getDeporteFavorito() {
        return deporteFavorito;
    }

    public void setDeporteFavorito(Deporte deporteFavorito) {
        this.deporteFavorito = deporteFavorito;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public ITipoNivel getNivel() {
        return nivel;
    }

    public void setNivel(ITipoNivel nivel) {
        this.nivel = nivel;
    }
}
