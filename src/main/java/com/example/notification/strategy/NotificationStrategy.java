package com.example.notification.strategy;

import com.example.model.entity.Jugador;
import com.example.notification.NotificationMessage;

public interface NotificationStrategy {
    void send(Jugador destinatario, NotificationMessage message);
}
