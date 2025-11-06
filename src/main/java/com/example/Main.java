package com.example;

import com.example.model.entity.*;
import com.example.notification.adapter.FirebasePushClientAdapter;
import com.example.notification.adapter.JavaMailEmailClientAdapter;
import com.example.notification.service.NotificationService;
import com.example.notification.strategy.EmailNotificationStrategy;
import com.example.notification.strategy.NotificationStrategy;
import com.example.notification.strategy.PushNotificationStrategy;
import java.util.List;

public class Main{
    public static void main(String[] args) {
        Sesion sesion = Sesion.getInstance();

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

        sesion.add(ana);
        sesion.add(bruno);
        sesion.add(caro);

        // Configurar estrategias y adaptadores
        NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());

        NotificationService notificationService = new NotificationService(email, push);

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

        // -------------------------------------------------------------------------
        // Segunda parte de la demo: Estrategias de emparejamiento (Strategy)
        // Se crea un conjunto de jugadores y partidos y se aplica cada una
        // de las estrategias implementadas: por zona, por nivel y por historial.
        System.out.println();
        System.out.println("==== DEMO Estrategias de Emparejamiento ====");

        // Crear nuevas zonas para los partidos de la demo
        Zona zonaNorte = new Zona("Norte");
        Zona zonaSur = new Zona("Sur");

        // Crear deporte común para los partidos (Fútbol) pero podrían ser distintos
        Deporte deporteFutbol = new Deporte("Futbol");

        // Crear jugadores con distintos niveles y zonas
        Jugador lucas = new Jugador("Lucas", "lucas@example.com", "pass", 30, deporteFutbol, zonaNorte);
        Jugador maria = new Jugador("Maria", "maria@example.com", "pass", 27, deporteFutbol, zonaNorte);
        Jugador pablo = new Jugador("Pablo", "pablo@example.com", "pass", 35, deporteFutbol, zonaSur);
        Jugador sofia = new Jugador("Sofia", "sofia@example.com", "pass", 20, deporteFutbol, zonaSur);
        Jugador juan = new Jugador("Juan", "juan@example.com", "pass", 32, deporteFutbol, zonaNorte);

        // Asignar niveles a cada jugador
        lucas.setNivel(new com.example.model.strategy.tipoNivel.Principiante());
        maria.setNivel(new com.example.model.strategy.tipoNivel.Intermedio());
        pablo.setNivel(new com.example.model.strategy.tipoNivel.Avanzado());
        sofia.setNivel(new com.example.model.strategy.tipoNivel.Principiante());
        juan.setNivel(new com.example.model.strategy.tipoNivel.Avanzado());

        // Obtener la instancia del gestor de emparejamientos (Singleton)
        com.example.model.strategy.emparejamiento.GestorEmparejamiento gestor =
                com.example.model.strategy.emparejamiento.GestorEmparejamiento.getInstance();

        // Crear algunos partidos y configurarlos con zona y rango de niveles
        // Partido 1: organizado por Lucas en zona Norte, acepta cualquier nivel
        Partido partido1 = gestor.crearPartido(lucas, deporteFutbol, 4);
        partido1.setUbicacion(zonaNorte);
        // rango completo: minimo principiante, máximo avanzado
        partido1.setMinNivel(new com.example.model.strategy.tipoNivel.Principiante());
        partido1.setMaxNivel(new com.example.model.strategy.tipoNivel.Avanzado());
        // Agregar un jugador invitado para simular cupo parcial
        partido1.agregarJugador(maria);

        // Partido 2: organizado por Pablo en zona Sur, admite desde Intermedio en adelante
        Partido partido2 = gestor.crearPartido(pablo, deporteFutbol, 4);
        partido2.setUbicacion(zonaSur);
        partido2.setMinNivel(new com.example.model.strategy.tipoNivel.Intermedio());
        partido2.setMaxNivel(new com.example.model.strategy.tipoNivel.Avanzado());
        partido2.agregarJugador(sofia);

        // Partido 3: organizado por Juan en zona Norte, sólo avanzado
        Partido partido3 = gestor.crearPartido(juan, deporteFutbol, 4);
        partido3.setUbicacion(zonaNorte);
        partido3.setMinNivel(new com.example.model.strategy.tipoNivel.Avanzado());
        partido3.setMaxNivel(new com.example.model.strategy.tipoNivel.Avanzado());

        // Registrar partido1 como jugado para generar historial entre Lucas y Maria
        com.example.model.entity.Historial.getInstance().registrarPartido(partido1);

        // Crear partido 4: organizado por Maria en zona Norte, lo utilizaremos para
        // demostrar emparejamiento por historial (Lucas ha jugado con Maria)
        Partido partido4 = gestor.crearPartido(maria, deporteFutbol, 4);
        partido4.setUbicacion(zonaNorte);
        partido4.setMinNivel(new com.example.model.strategy.tipoNivel.Principiante());
        partido4.setMaxNivel(new com.example.model.strategy.tipoNivel.Avanzado());

        // Aplicar estrategia por zona para Maria (zona Norte)
        gestor.setEstrategia(new com.example.model.strategy.emparejamiento.EmparejamientoZonaImpl());
        List<Partido> matchesZona = gestor.buscarPartidosPara(maria);
        System.out.println("\n-- Partidos sugeridos por zona para Maria (zona Norte) --");
        for (Partido p : matchesZona) {
            System.out.println("Partido en zona " + (p.getUbicacion() != null ? p.getUbicacion().getNombre() : "<sin zona>")
                    + " organizado por " + p.getOrganizador().getNombre());
        }

        // Aplicar estrategia por nivel para Sofia (Principiante)
        gestor.setEstrategia(new com.example.model.strategy.emparejamiento.EmparejamientoNivelImpl());
        List<Partido> matchesNivel = gestor.buscarPartidosPara(sofia);
        System.out.println("\n-- Partidos sugeridos por nivel para Sofia (Principiante) --");
        for (Partido p : matchesNivel) {
            String min = (p.getMinNivel() != null ? p.getMinNivel().getNombre() : "Cualquiera");
            String max = (p.getMaxNivel() != null ? p.getMaxNivel().getNombre() : "Cualquiera");
            System.out.println("Partido organizado por " + p.getOrganizador().getNombre()
                    + " [Nivel requerido: " + min + "-" + max + "]");
        }

        // Aplicar estrategia por historial para Lucas (ha jugado con Maria)
        gestor.setEstrategia(new com.example.model.strategy.emparejamiento.EmparejamientoHistorialImpl());
        List<Partido> matchesHistorial = gestor.buscarPartidosPara(lucas);
        System.out.println("\n-- Partidos sugeridos por historial para Lucas (conocidos) --");
        for (Partido p : matchesHistorial) {
            System.out.println("Partido organizado por " + p.getOrganizador().getNombre());
        }

        System.out.println("==== FIN DEMO ESTRATEGIAS ====");

        // -------------------------------------------------------------------------
        // Vista de menú interactivo
        System.out.println("\nAhora se iniciará la interfaz de consola para interactuar con el sistema.");
        com.example.view.MenuView menu = new com.example.view.MenuView();
        menu.run();
    }
}