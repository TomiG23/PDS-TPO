package com.example.model;

import com.example.model.State.EstadoPartido;
import com.example.model.State.NecesitamosJugadores;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Partido (Contexto) con atributos y delegación al Estado (State pattern).
 * Nota: se asume que "DateTime" mapea a java.time.LocalDateTime.
 * No se crean clases placeholder (Jugador, TipoNivel, Zona) según tu indicación.
 */
public class Partido {
    private Deporte deporte;
    private Jugador organizador;
    private List<Jugador> jugadores = new ArrayList<>();
    private int jugadoresRequeridos;
    private LocalDateTime duracionEncuentro; // DateTime
    private Zona ubicacion;
    private LocalDateTime horario; // DateTime
    private TipoNivel tipoNivel;
    private EstadoPartido estado;
    private TipoNivel minNivel;
    private TipoNivel maxNivel;

    /**
     * Constructor principal. Establece el estado inicial en NecesitamosJugadores.
     */
    public Partido(Jugador organizador, Deporte deporte, int jugadoresRequeridos) {
        this.organizador = organizador;
        this.deporte = deporte;
        this.jugadoresRequeridos = jugadoresRequeridos;
        this.estado = new NecesitamosJugadores();
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

    // Métodos de gestión del estado
    public void setEstado(EstadoPartido estado) {
        System.out.println("[Partido] Transición de estado: " + (this.estado != null ? this.estado.getNombreEstado() : "<none>") + " -> " + (estado != null ? estado.getNombreEstado() : "<none>"));
        this.estado = estado;
    }

    public EstadoPartido getEstado() {
        return this.estado;
    }

    // Método de utilidad
    public String mostrarEstado() {
        String nombre = (estado != null) ? estado.getNombreEstado() : "SinEstado";
        System.out.println("Estado actual del partido: " + nombre);
        return nombre;
    }

    // Getters y setters
    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Jugador getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Jugador organizador) {
        this.organizador = organizador;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public int getJugadoresRequeridos() {
        return jugadoresRequeridos;
    }

    public void setJugadoresRequeridos(int jugadoresRequeridos) {
        this.jugadoresRequeridos = jugadoresRequeridos;
    }

    public LocalDateTime getDuracionEncuentro() {
        return duracionEncuentro;
    }

    public void setDuracionEncuentro(LocalDateTime duracionEncuentro) {
        this.duracionEncuentro = duracionEncuentro;
    }

    public Zona getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Zona ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public TipoNivel getTipoNivel() {
        return tipoNivel;
    }

    public void setTipoNivel(TipoNivel tipoNivel) {
        this.tipoNivel = tipoNivel;
    }

    public TipoNivel getMinNivel() {
        return minNivel;
    }

    public void setMinNivel(TipoNivel minNivel) {
        this.minNivel = minNivel;
    }

    public TipoNivel getMaxNivel() {
        return maxNivel;
    }

    public void setMaxNivel(TipoNivel maxNivel) {
        this.maxNivel = maxNivel;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "deporte=" + deporte +
                ", organizador=" + organizador +
                ", jugadores=" + (jugadores != null ? jugadores.size() : 0) +
                ", jugadoresRequeridos=" + jugadoresRequeridos +
                ", horario=" + horario +
                ", ubicacion=" + ubicacion +
                ", tipoNivel=" + tipoNivel +
                ", estado=" + (estado != null ? estado.getNombreEstado() : "<none>") +
                '}';
    }
}
