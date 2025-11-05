package com.example.notification.adapter;

public class JavaMailEmailClientAdapter implements EmailClient {
    @Override
    public void send(String to, String subject, String body) {
        // Placeholder de integración con JavaMail u otra librería de email
        System.out.println("[Email] to=" + to + " | subject=" + subject + " | body=" + body);
    }
}
