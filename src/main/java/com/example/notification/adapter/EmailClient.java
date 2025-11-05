package com.example.notification.adapter;

public interface EmailClient {
    void send(String to, String subject, String body);
}
