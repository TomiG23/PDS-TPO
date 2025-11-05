package com.example.notification.adapter;

public class FirebasePushClientAdapter implements PushClient {
    @Override
    public void send(String pushToken, String title, String body) {
        // Placeholder de integración con Firebase
        System.out.println("[Push] token=" + pushToken + " | title=" + title + " | body=" + body);
    }
}
