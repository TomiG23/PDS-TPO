package com.example.model.entity;

import com.example.model.strategy.emparejamiento.GestorEmparejamiento;

import java.util.ArrayList;
import java.util.List;

public class Sesion {

    private static final Sesion INSTANCE = new Sesion();

    public static Sesion getInstance() {
        return INSTANCE;
    }

    private final List<Jugador> jugadores = new ArrayList<>();

    public void add(Jugador j) { if (j != null) jugadores.add(j); }

    public List<Jugador> getAllJugadores() { return new ArrayList<>(jugadores); }

    /**
     * Busca un jugador por su correo electrónico.  Si existen varios con el
     * mismo correo se devuelve el primero.
     *
     * @param email correo a buscar
     * @return jugador con ese correo o {@code null} si no existe
     */
    public Jugador findByEmail(String email) {
        if (email == null) return null;
        for (Jugador jugador : jugadores) {
            if (jugador != null && email.equalsIgnoreCase(jugador.getMail())) {
                return jugador;
            }
        }
        return null;
    }
}
