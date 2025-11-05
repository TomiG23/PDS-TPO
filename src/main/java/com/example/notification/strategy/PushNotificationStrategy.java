package com.example.notification.strategy;

import com.example.model.entity.Jugador;
import com.example.notification.NotificationMessage;
import com.example.notification.adapter.PushClient;

public class PushNotificationStrategy implements NotificationStrategy {
    private final PushClient pushClient;

    public PushNotificationStrategy(PushClient pushClient) {
        this.pushClient = pushClient;
    }

    @Override
    public void send(Jugador destinatario, NotificationMessage message) {
        if (destinatario == null || destinatario.getPushToken() == null) return;
        pushClient.send(destinatario.getPushToken(), message.getSubject(), message.getBody());
    }
}
