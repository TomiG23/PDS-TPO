package com.example.notification.adapter;

public interface PushClient {
    void send(String pushToken, String title, String body);
}
