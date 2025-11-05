package com.example.notification.strategy;

import com.example.model.entity.Jugador;
import com.example.notification.NotificationMessage;
import com.example.notification.adapter.EmailClient;

public class EmailNotificationStrategy implements NotificationStrategy {
    private final EmailClient emailClient;

    public EmailNotificationStrategy(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    @Override
    public void send(Jugador destinatario, NotificationMessage message) {
        if (destinatario == null || destinatario.getMail() == null) return;
        emailClient.send(destinatario.getMail(), message.getSubject(), message.getBody());
    }
}
