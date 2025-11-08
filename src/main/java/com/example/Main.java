package com.example;

import com.example.model.entity.*;
import com.example.model.strategy.tipoDeporte.tipoNivel.Basket;
import com.example.model.strategy.tipoDeporte.tipoNivel.Futbol;
import com.example.model.strategy.tipoDeporte.tipoNivel.Voley;
import com.example.model.strategy.tipoNivel.Avanzado;
import com.example.model.strategy.tipoNivel.Intermedio;
import com.example.model.strategy.tipoNivel.Principiante;
import com.example.notification.adapter.FirebasePushClientAdapter;
import com.example.notification.adapter.JavaMailEmailClientAdapter;
import com.example.notification.service.NotificationService;
import com.example.notification.strategy.EmailNotificationStrategy;
import com.example.notification.strategy.NotificationStrategy;
import com.example.notification.strategy.PushNotificationStrategy;
import com.example.view.MenuView;

import java.util.List;

public class Main{
    public static void main(String[] args) {
        Sesion sesion = Sesion.getInstance();

        Deporte futbol = new Deporte(new Futbol());
        Deporte basket = new Deporte(new Basket());
        Deporte voley = new Deporte(new Voley());

        Zona caba = new Zona();
        Zona zonaNorte = new Zona("Norte");
        Zona zonaSur = new Zona("Sur");

        Jugador ana = new Jugador("Ana", "tomasgoncalves123@gmail.com", "pass", new Habilidad(basket, new Principiante()));
        ana.setPushToken("token-ANA");
        Jugador bruno = new Jugador("Bruno", "micaela.palomino@gmail.com", "pass", new Habilidad(futbol, new Principiante()));
        bruno.setPushToken("token-BRUNO");
        Jugador caro = new Jugador("Caro", "tytoapache@gmail.com", "pass", new Habilidad(futbol, new Principiante()));
        caro.setPushToken("token-CARO");

        sesion.registrar(ana);
        sesion.registrar(bruno);
        sesion.registrar(caro);

        NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());
        NotificationService notificationService = new NotificationService(email, push);

        Partido partido = new Partido(ana, futbol, 2, zonaNorte);

        MenuView menu = new MenuView(notificationService);
        menu.run();
    }
}