package com.example.notification.observer;

import com.example.model.entity.Partido;

public interface INotificationObserver {
    void onPartidoEvent(String eventName, Partido partido);
}
