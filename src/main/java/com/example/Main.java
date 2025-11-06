package com.example;

import com.example.model.entity.Deporte;
import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.entity.Ubicacion;
import com.example.model.strategy.emparejamiento.EmparejamientoZonaImpl;
import com.example.model.strategy.tipoNivel.Avanzado;
import com.example.model.strategy.tipoNivel.Intermedio;
import com.example.model.strategy.tipoNivel.Principiante;
import com.example.notification.adapter.FirebasePushClientAdapter;
import com.example.notification.adapter.JavaMailEmailClientAdapter;
import com.example.notification.service.NotificationService;
import com.example.notification.service.UserDirectory;
import com.example.notification.strategy.EmailNotificationStrategy;
import com.example.notification.strategy.NotificationStrategy;
import com.example.notification.strategy.PushNotificationStrategy;

import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) {
        System.out.println("==== DEMO Notificaciones y Estados de Partido ====");

        // Directory de usuarios en memoria
        InMemoryUserDirectory userDirectory = new InMemoryUserDirectory();

        // Crear ubicaciones de ejemplo (coordenadas de Buenos Aires)
        Ubicacion ubicacionAna = new Ubicacion(-34.6037, -58.3816, "Av. Corrientes 1234", "Cerca del Obelisco");
        Ubicacion ubicacionBruno = new Ubicacion(-34.5981, -58.3860, "Av. Santa Fe 2000", "Cerca de Recoleta");
        Ubicacion ubicacionCaro = new Ubicacion(-34.5995, -58.3805, "Av. Callao 800", "Cerca de Callao y Santa Fe");
        Ubicacion ubicacionLejos = new Ubicacion(-34.9222, -57.9544, "La Plata", "Lejos de CABA");

        // Crear algunos usuarios
        Deporte futbol = new Deporte("Futbol");
        Deporte basket = new Deporte("Basquet");

        Jugador ana = new Jugador("Ana", "ana@example.com", "pass", 25, futbol, ubicacionAna);
        ana.setPushToken("token-ANA");
        ana.setNivel(new Intermedio());
        
        Jugador bruno = new Jugador("Bruno", "bruno@example.com", "pass", 28, futbol, ubicacionBruno);
        bruno.setPushToken("token-BRUNO");
        bruno.setNivel(new Avanzado());
        
        Jugador caro = new Jugador("Caro", "caro@example.com", "pass", 22, basket, ubicacionCaro);
        caro.setPushToken("token-CARO");
        caro.setNivel(new Principiante());

        userDirectory.add(ana);
        userDirectory.add(bruno);
        userDirectory.add(caro);

        // Configurar estrategias y adaptadores
        NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());

        NotificationService notificationService = new NotificationService(email, push, userDirectory);

        // Crear partidos de ejemplo
        Partido partidoCerca1 = new Partido(ana, futbol, 4, ubicacionAna);
        partidoCerca1.setMinNivel(new Principiante());
        partidoCerca1.setMaxNivel(new Avanzado());
        partidoCerca1.addObserver(notificationService);
        
        Partido partidoCerca2 = new Partido(bruno, futbol, 6, ubicacionBruno);
        partidoCerca2.setMinNivel(new Intermedio());
        partidoCerca2.addObserver(notificationService);
        
        Partido partidoLejos = new Partido(caro, basket, 10, ubicacionLejos);
        partidoLejos.addObserver(notificationService);
        
        List<Partido> todosLosPartidos = List.of(partidoCerca1, partidoCerca2, partidoLejos);
        
        // Demostrar búsqueda por ubicación
        System.out.println("\n=== BUSCANDO PARTIDOS CERCANOS ===");
        EmparejamientoZonaImpl buscador = new EmparejamientoZonaImpl();
        
        // Ana busca partidos cercanos
        List<Partido> partidosCercaDeAna = buscador.emparejar(todosLosPartidos, ana);
        System.out.println("\nPartidos cerca de Ana (dentro de 3km): " + partidosCercaDeAna.size());
        partidosCercaDeAna.forEach(p -> System.out.println(" - " + p.getDeporte().getNombre() + " en " + p.getUbicacion().getDireccion()));
        
        // Bruno busca partidos cercanos
        List<Partido> partidosCercaDeBruno = buscador.emparejar(todosLosPartidos, bruno);
        System.out.println("\nPartidos cerca de Bruno (dentro de 3km): " + partidosCercaDeBruno.size());
        partidosCercaDeBruno.forEach(p -> System.out.println(" - " + p.getDeporte().getNombre() + " en " + p.getUbicacion().getDireccion()));
        
        // Caro busca partidos cercanos
        List<Partido> partidosCercaDeCaro = buscador.emparejar(todosLosPartidos, caro);
        System.out.println("\nPartidos cerca de Caro (dentro de 3km): " + partidosCercaDeCaro.size());
        partidosCercaDeCaro.forEach(p -> System.out.println(" - " + p.getDeporte().getNombre() + " en " + p.getUbicacion().getDireccion()));
        
        // Usar el primer partido para la demo de notificaciones
        Partido partido = partidoCerca1;

        System.out.println("\n=== DEMO DE NOTIFICACIONES ===");
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
    }
}

class InMemoryUserDirectory implements UserDirectory {
    private final List<Jugador> jugadores = new ArrayList<>();

    public void add(Jugador j) { if (j != null) jugadores.add(j); }

    @Override
    public List<Jugador> getAllJugadores() { return new ArrayList<>(jugadores); }
}
