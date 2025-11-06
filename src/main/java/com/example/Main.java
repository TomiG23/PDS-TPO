package com.example;

import com.example.model.entity.Deporte;
import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.entity.Zona;
import com.example.notification.adapter.FirebasePushClientAdapter;
import com.example.notification.adapter.JavaMailEmailClientAdapter;
import com.example.notification.service.NotificationService;
import com.example.notification.service.UserDirectory;
import com.example.notification.strategy.EmailNotificationStrategy;
import com.example.notification.strategy.NotificationStrategy;
import com.example.notification.strategy.PushNotificationStrategy;
import com.example.view.MenuAcceso;
import com.example.view.MenuAcciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        System.out.println("==== DEMO Notificaciones y Estados de Partido ====");

        // Directory de usuarios en memoria
        InMemoryUserDirectory userDirectory = new InMemoryUserDirectory();

        // Crear algunos usuarios
        Deporte futbol = new Deporte("Futbol");
        Deporte basket = new Deporte("Basquet");
        Zona caba = new Zona();

        Jugador ana = new Jugador("Ana", "ana@example.com", "pass", 25, futbol, caba);
        ana.setPushToken("token-ANA");
        Jugador bruno = new Jugador("Bruno", "bruno@example.com", "pass", 28, futbol, caba);
        bruno.setPushToken("token-BRUNO");
        Jugador caro = new Jugador("Caro", "caro@example.com", "pass", 22, basket, caba);
        caro.setPushToken("token-CARO");

        userDirectory.add(ana);
        userDirectory.add(bruno);
        userDirectory.add(caro);

        // Configurar estrategias y adaptadores
        NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());

        NotificationService notificationService = new NotificationService(email, push, userDirectory);

        // Crear partido de Futbol requiriendo 2 jugadores
        Jugador organizador = ana;
        Partido partido = new Partido(organizador, futbol, 2);
        partido.addObserver(notificationService);

        System.out.println("-- Publicando creación de partido --");
        partido.publicarCreacion();

        System.out.println("-- Agregando jugador Bruno --");
        partido.agregarJugador(bruno); // aún no arma partido si requiere 2 y solo hay 1

        System.out.println("-- Agregando jugador Ana (ya estaba organizando, se suma como jugador) --");
        partido.agregarJugador(ana); // ahora debería pasar a PartidoArmado

        System.out.println("-- Confirmando partido --");
        partido.confirmar();

        System.out.println("-- Iniciando partido --");
        partido.iniciar();

        System.out.println("-- Finalizando partido --");
        partido.finalizar();

        System.out.println("==== FIN DEMO ====");
        Scanner scanner = new Scanner(System.in);
        MenuAcceso menu = new MenuAcceso(scanner);
        menu.mostrarMenu(scanner);

        System.out.println("\nBienvenido {Usuario}\n");

        MenuAcciones menuAcciones = new MenuAcciones(scanner);
        menuAcciones.mostrarMenu();
        scanner.close();
    }
}

class InMemoryUserDirectory implements UserDirectory {
    private final List<Jugador> jugadores = new ArrayList<>();

    public void add(Jugador j) { if (j != null) jugadores.add(j); }

    @Override
    public List<Jugador> getAllJugadores() { return new ArrayList<>(jugadores); }
}
