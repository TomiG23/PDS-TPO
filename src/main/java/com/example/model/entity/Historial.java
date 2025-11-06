package com.example.model.entity;

import java.util.*;

import com.example.model.entity.Partido;

/**
 * Historial actúa como un registro de encuentros disputados por cada jugador.
 * En esta implementación, el historial se gestiona de manera global y en
 * memoria mediante un mapa que relaciona a cada jugador con el conjunto de
 * jugadores con los que ya ha compartido partido.  Este enfoque permite
 * consultar rápidamente el conjunto de "conocidos" de un jugador para
 * implementar la estrategia de emparejamiento por historial.
 */
public class Historial {
    /**
     * Instancia singleton del historial.  Se utiliza para centralizar el
     * almacenamiento sin necesidad de gestionar múltiples instancias.
     */
    private static final Historial INSTANCE = new Historial();

    /**
     * Mapa que asocia un jugador a un conjunto de otros jugadores con los que
     * ya ha compartido al menos un partido.  La clave y los elementos del
     * conjunto corresponden a las instancias de {@link Jugador} que
     * participaron en encuentros registrados mediante
     * {@link #registrarPartido(Partido)}.
     */
    private final Map<Jugador, Set<Jugador>> conocidosPorJugador = new HashMap<>();

    /**
     * Constructor privado.  Utilice {@link #getInstance()} para obtener la
     * única instancia del historial.
     */
    private Historial() {
        // ocultar constructor
    }

    /**
     * Devuelve la instancia global del historial.
     *
     * @return historial global
     */
    public static Historial getInstance() {
        return INSTANCE;
    }

    /**
     * Registra un partido en el historial.  Todos los jugadores participantes,
     * incluyendo al organizador, quedarán registrados como "conocidos" entre
     * sí.  Este método no almacena el partido en sí, sino únicamente la
     * relación entre jugadores.
     *
     * @param partido partido a registrar
     */
    public void registrarPartido(Partido partido) {
        if (partido == null) return;
        // Construir lista de participantes: organizador + jugadores
        List<Jugador> participantes = new ArrayList<>();
        if (partido.getOrganizador() != null) participantes.add(partido.getOrganizador());
        if (partido.getJugadores() != null) participantes.addAll(partido.getJugadores());
        // Para cada jugador, registrar a los demás como conocidos
        for (Jugador j : participantes) {
            if (j == null) continue;
            conocidosPorJugador.putIfAbsent(j, new HashSet<>());
            Set<Jugador> conocidos = conocidosPorJugador.get(j);
            for (Jugador otro : participantes) {
                if (otro != null && !otro.equals(j)) {
                    conocidos.add(otro);
                }
            }
        }
    }

    /**
     * Devuelve el conjunto de jugadores con los que el jugador dado ha
     * compartido al menos un partido previamente.  Si el jugador no tiene
     * historial registrado, se devuelve un conjunto vacío.
     *
     * @param jugador jugador a consultar
     * @return conjunto de jugadores conocidos
     */
    public Set<Jugador> getJugadoresConocidos(Jugador jugador) {
        return Collections.unmodifiableSet(
                conocidosPorJugador.getOrDefault(jugador, Collections.emptySet())
        );
    }

    /**
     * Devuelve un conjunto con los nombres de los jugadores conocidos del
     * jugador dado.  Esta utilidad simplifica el uso desde clases que sólo
     * requieren comparar nombres de jugadores.
     *
     * @param jugador jugador a consultar
     * @return conjunto de nombres de jugadores conocidos
     */
    public Set<String> getNombresConocidos(Jugador jugador) {
        Set<String> nombres = new HashSet<>();
        for (Jugador j : getJugadoresConocidos(jugador)) {
            if (j != null && j.getNombre() != null) {
                nombres.add(j.getNombre());
            }
        }
        return nombres;
    }
}
