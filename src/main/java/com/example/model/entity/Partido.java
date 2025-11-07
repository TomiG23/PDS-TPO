package com.example.model.entity;

import com.example.model.State.IEstadoPartido;
import com.example.model.State.NecesitamosJugadores;

import com.example.model.strategy.tipoNivel.ITipoNivel;
import com.example.notification.observer.INotificationObserver;
import com.example.notification.observer.PartidoEvents;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Partido (Contexto) con atributos y delegación al Estado (State pattern).
 * Nota: se asume que "DateTime" mapea a java.time.LocalDateTime.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "estado")
@EqualsAndHashCode
public class Partido {
    private Deporte deporte;
    private Jugador organizador;
    private List<Jugador> jugadores = new ArrayList<>();
    private List<Jugador> jugadoresConfirmados = new ArrayList<>();
    private int jugadoresRequeridos;
    private LocalDateTime duracionEncuentro; // DateTime
    private Zona ubicacion;
    private LocalDateTime horario; // DateTime
    private ITipoNivel tipoNivel;
    private IEstadoPartido estado;
    private ITipoNivel minNivel;
    private ITipoNivel maxNivel;
    private boolean terminadoConErrores;
    private final List<INotificationObserver> observers = new ArrayList<>();

    public Partido(Jugador organizador, Deporte deporte, int jugadoresRequeridos) {
        this.organizador = organizador;
        this.deporte = deporte;
        this.jugadoresRequeridos = jugadoresRequeridos;
        this.estado = new NecesitamosJugadores();
        // El organizador se une automáticamente al partido
        if (organizador != null) {
            this.jugadores.add(organizador);
        }
    }
    public Partido(Jugador organizador, Deporte deporte, int jugadoresRequeridos, Zona zona) {
        this.organizador = organizador;
        this.deporte = deporte;
        this.jugadoresRequeridos = jugadoresRequeridos;
        this.estado = new NecesitamosJugadores();
        this.ubicacion = zona;
        // El organizador se une automáticamente al partido
        if (organizador != null) {
            this.jugadores.add(organizador);
        }
    }

    // Métodos del patrón State (delegación)
    public void agregarJugador(Jugador jugador) {
        if (jugador != null) {
            this.jugadores.add(jugador);
        }
        if (this.estado != null) {
            this.estado.agregarJugadorAPartido(this);
        }
    }

    public void cancelar() {
        if (this.estado != null) this.estado.cancelarPartido(this);
    }

    public void confirmar() {
        if (this.estado != null) this.estado.confirmarPartido(this);
    }

    public void iniciar() {
        if (this.estado != null) this.estado.iniciarPartido(this);
    }

    public void finalizar() {
        if (this.estado != null) this.estado.finalizarPartido(this);
    }

    public void confirmarAsistencia(Jugador jugador) {
        if (jugador == null) return;
        if (this.jugadores != null && this.jugadores.contains(jugador)) {
            if (!this.jugadoresConfirmados.contains(jugador)) {
                this.jugadoresConfirmados.add(jugador);
            }
        }
        if (jugadores.size() == jugadoresConfirmados.size()) {
            confirmar();
        }
    }

    // Métodos de gestión del estado
    public void setEstado(IEstadoPartido estado) {
        System.out.println("[Partido] Transición de estado: " + (this.estado != null ? this.estado.getNombreEstado() : "<none>") + " -> " + (estado != null ? estado.getNombreEstado() : "<none>"));
        this.estado = estado;
        String event = mapEstadoToEvent(estado);
        if (event != null) notifyObservers(event);
    }

    public IEstadoPartido getEstado() {
        return this.estado;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    // Método de utilidad
    public String mostrarEstado() {
        String nombre = (estado != null) ? estado.getNombreEstado() : "SinEstado";
        System.out.println("Estado actual del partido: " + nombre);
        return nombre;
    }

    // Observer API
    public void addObserver(INotificationObserver observer) {
        if (observer != null && !observers.contains(observer)) observers.add(observer);
    }

    public void removeObserver(INotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String event) {
        for (INotificationObserver obs : observers) {
            try {
                obs.onPartidoEvent(event, this);
            } catch (Exception e) {
                System.out.println("[Partido] Error notificando observer: " + e.getMessage());
            }
        }
    }

    private String mapEstadoToEvent(IEstadoPartido est) {
        if (est == null) return null;
        String n = est.getNombreEstado();
        if ("PartidoArmado".equals(n)) return PartidoEvents.PARTIDO_ARMADO;
        if ("PartidoConfirmado".equals(n)) return PartidoEvents.PARTIDO_CONFIRMADO;
        if ("PartidoEnJuego".equals(n)) return PartidoEvents.PARTIDO_EN_JUEGO;
        if ("PartidoFinalizado".equals(n)) return PartidoEvents.PARTIDO_FINALIZADO;
        if ("PartidoCancelado".equals(n)) return PartidoEvents.PARTIDO_CANCELADO;
        return null;
    }

    // Disparar evento de creación manualmente tras construir y suscribir observers
    public void publicarCreacion() {
        notifyObservers(PartidoEvents.PARTIDO_CREADO);
    }
}
