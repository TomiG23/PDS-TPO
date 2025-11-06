package com.example.notification.service;

import com.example.model.entity.Jugador;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación en memoria de {@link UserDirectory}.  Almacena los
 * jugadores registrados durante la ejecución de la aplicación y permite
 * consultar el listado completo.  No implementa ningún mecanismo de
 * persistencia, por lo que la información se perderá al finalizar el
 * proceso.
 */
public class InMemoryUserDirectory implements UserDirectory {
    private final List<Jugador> jugadores = new ArrayList<>();

    /**
     * Agrega un nuevo jugador al directorio.  Si el jugador es {@code null}
     * la operación se ignora.
     *
     * @param jugador jugador a almacenar
     */
    public void add(Jugador jugador) {
        if (jugador != null) {
            jugadores.add(jugador);
        }
    }

    @Override
    public List<Jugador> getAllJugadores() {
        return new ArrayList<>(jugadores);
    }

    /**
     * Busca un jugador por su correo electrónico.  Si existen varios con el
     * mismo correo se devuelve el primero.
     *
     * @param email correo a buscar
     * @return jugador con ese correo o {@code null} si no existe
     */
    public Jugador findByEmail(String email) {
        if (email == null) return null;
        for (Jugador j : jugadores) {
            if (j != null && email.equalsIgnoreCase(j.getMail())) {
                return j;
            }
        }
        return null;
    }
}