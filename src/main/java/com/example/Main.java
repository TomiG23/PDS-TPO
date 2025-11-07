package com.example;

import com.example.model.entity.Deporte;
import com.example.model.entity.Habilidad;
import com.example.model.entity.Jugador;
import com.example.model.entity.Sesion;
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

public class Main{
    public static void main(String[] args) {
        Sesion sesion = Sesion.getInstance();

        // Configurar estrategias y adaptadores de notificación
        NotificationStrategy email = new EmailNotificationStrategy(
            new JavaMailEmailClientAdapter("tytoapache@gmail.com", "cquzsqalpflvswxa")
        );
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());
        NotificationService notificationService = new NotificationService(email, push);

        // Configurar el GestorEmparejamiento para que todos los partidos creados
        // reciban notificaciones automáticamente
        com.example.model.strategy.emparejamiento.GestorEmparejamiento gestor =
                com.example.model.strategy.emparejamiento.GestorEmparejamiento.getInstance();
        gestor.setNotificationObserver(notificationService);

        // ============================================================================
        // CARGAR DATOS DE DEMO
        // Crear usuarios de prueba para que el menú tenga datos desde el inicio
        // ============================================================================
        cargarDatosDemo(sesion);

        // Iniciar el menú interactivo
        MenuView menu = new MenuView(notificationService);
        menu.run();
    }

    /**
     * Carga datos de demostración en el sistema para pruebas.
     * Incluye usuarios con diferentes deportes y niveles.
     */
    private static void cargarDatosDemo(Sesion sesion) {
        Deporte futbol = new Deporte("Futbol");
        Deporte basket = new Deporte("Basquet");
        Deporte softball = new Deporte("Softball");

        // Crear jugadores de prueba
        Jugador ana = new Jugador("Ana", "ana@example.com", "pass123", new Habilidad(futbol, new Principiante()));
        ana.setPushToken("token-ANA");
        
        Jugador bruno = new Jugador("Bruno", "bruno@example.com", "pass123", new Habilidad(futbol, new Principiante()));
        bruno.setPushToken("token-BRUNO");
        
        Jugador caro = new Jugador("Caro", "tomasgoncalves123@gmail.com", "pass123", new Habilidad(futbol, new Principiante()));
        caro.setPushToken("token-CARO");
        
        Jugador lucas = new Jugador("Lucas", "sebastindlf@gmail.com", "pass123", new Habilidad(futbol, new Principiante()));
        
        Jugador maria = new Jugador("Maria", "lofarobruno@gmail.com", "pass123", new Habilidad(futbol, new Intermedio()));
        
        Jugador pablo = new Jugador("Pablo", "micaela.palomino@gmail.com", "pass123", new Habilidad(futbol, new Avanzado()));
        
        Jugador sofia = new Jugador("Sofia", "sofia@example.com", "pass123", new Habilidad(futbol, new Principiante()));
        
        Jugador juan = new Jugador("Juan", "juan@example.com", "pass123", new Habilidad(futbol, new Avanzado()));

        // Registrar todos los jugadores
        sesion.registrar(ana);
        sesion.registrar(bruno);
        sesion.registrar(caro);
        sesion.registrar(lucas);
        sesion.registrar(maria);
        sesion.registrar(pablo);
        sesion.registrar(sofia);
        sesion.registrar(juan);
    }
}