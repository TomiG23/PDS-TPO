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

        // Crear algunos usuarios
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

        // Configurar estrategias y adaptadores
        NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());
        NotificationService notificationService = new NotificationService(email, push);

        // Crear partido de Futbol requiriendo 2 jugadores
        Partido partido = new Partido(ana, futbol, 2, zonaNorte);

        //demo filtros
        // partido intermedio avanzado en Futbol zona norte - 2/5
        // partido principiante avanzado de Futbol zona sur - 1/9

        // demo cambio de estado
        // partido principiante avanzado de Cualquier deporte zona norte - 3/4

        // Obtener la instancia del gestor de emparejamientos (Singleton)
//        com.example.model.strategy.emparejamiento.GestorEmparejamiento gestor =
//                com.example.model.strategy.emparejamiento.GestorEmparejamiento.getInstance();
//        // Aplicar estrategia por historial para Lucas (ha jugado con Maria)
//        gestor.setEstrategia(new com.example.model.strategy.emparejamiento.EmparejamientoHistorialImpl());
//        List<Partido> matchesHistorial = gestor.buscarPartidosPara(lucas);
//        System.out.println("\n-- Partidos sugeridos por historial para Lucas (conocidos) --");
//        for (Partido p : matchesHistorial) {
//            System.out.println("Partido organizado por " + p.getOrganizador().getNombre());
//        }

        MenuView menu = new MenuView(notificationService);
        menu.run();
    }
}