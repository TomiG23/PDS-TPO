package com.example.notification.service;

import com.example.model.entity.Deporte;
import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.entity.Sesion;
import com.example.notification.NotificationMessage;
import com.example.notification.observer.INotificationObserver;
import com.example.notification.observer.PartidoEvents;
import com.example.notification.strategy.NotificationStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NotificationService implements INotificationObserver {
    private final NotificationStrategy emailStrategy;
    private final NotificationStrategy pushStrategy;
    private final Sesion sesion = Sesion.getInstance();

    public NotificationService(NotificationStrategy emailStrategy,
                               NotificationStrategy pushStrategy
                               ) {
        this.emailStrategy = emailStrategy;
        this.pushStrategy = pushStrategy;
    }

    @Override
    public void onPartidoEvent(String eventName, Partido partido) {
        List<Jugador> destinatarios = recipientsFor(eventName, partido);
        NotificationMessage message = buildMessage(eventName, partido);
        for (Jugador j : destinatarios) {
            if (emailStrategy != null) emailStrategy.send(j, message);
            if (pushStrategy != null) pushStrategy.send(j, message);
        }
    }

    private List<Jugador> recipientsFor(String eventName, Partido partido) {
        List<Jugador> result = new ArrayList<>();
        
        if (PartidoEvents.PARTIDO_CREADO.equals(eventName)) {
            // Notificar a usuarios cuyo deporte favorito coincide
            Deporte dep = partido.getDeporte();
            String nombreDep = dep != null ? dep.getNombre() : null;
            if (sesion != null && nombreDep != null) {
                for (Jugador j : sesion.getAllJugadores()) {
                    if (j != null && j.getDeporteFavorito() != null &&
                            Objects.equals(nombreDep, j.getDeporteFavorito().getNombre())) {
                        result.add(j);
                    }
                }
            }
        } else {
            // Para todos los demás eventos: notificar a organizador + jugadores del partido
            Set<Jugador> set = new HashSet<>();
            if (partido.getOrganizador() != null) set.add(partido.getOrganizador());
            if (partido.getJugadores() != null) set.addAll(partido.getJugadores());
            result.addAll(set);
        }
        
        return result;
    }

    private NotificationMessage buildMessage(String eventName, Partido partido) {
        String deporte = partido.getDeporte() != null ? partido.getDeporte().getNombre() : "";
        String subj;
        String body;
        switch (eventName) {
            case PartidoEvents.PARTIDO_CREADO -> {
                subj = "Nuevo partido de " + deporte;
                body = "Se ha creado un nuevo partido de " + deporte + ". ¡Sumate!";
            }
            case PartidoEvents.PARTIDO_ARMADO -> {
                subj = "Partido armado";
                body = "Se alcanzó el cupo de jugadores. El partido está armado.";
            }
            case PartidoEvents.PARTIDO_CONFIRMADO -> {
                subj = "Partido confirmado";
                body = "Todos confirmaron su participación. ¡Listo para jugar!";
            }
            case PartidoEvents.PARTIDO_EN_JUEGO -> {
                subj = "Partido en juego";
                body = "El partido ha comenzado.";
            }
            case PartidoEvents.PARTIDO_FINALIZADO -> {
                subj = "Partido finalizado";
                body = "El partido ha finalizado. Podés dejar comentarios o estadísticas.";
            }
            case PartidoEvents.PARTIDO_CANCELADO -> {
                subj = "Partido cancelado";
                body = "El organizador canceló el partido.";
            }
            default -> {
                subj = "Actualización de partido";
                body = "El partido cambió de estado.";
            }
        }
        return new NotificationMessage(subj, body);
    }
}
