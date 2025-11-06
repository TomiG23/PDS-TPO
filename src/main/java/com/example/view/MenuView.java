package com.example.view;

import com.example.model.entity.Deporte;
import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.model.entity.Zona;
import com.example.model.strategy.emparejamiento.EmparejamientoHistorialImpl;
import com.example.model.strategy.emparejamiento.EmparejamientoNivelImpl;
import com.example.model.strategy.emparejamiento.EmparejamientoZonaImpl;
import com.example.model.strategy.emparejamiento.GestorEmparejamiento;
import com.example.model.strategy.tipoNivel.Avanzado;
import com.example.model.strategy.tipoNivel.Intermedio;
import com.example.model.strategy.tipoNivel.ITipoNivel;
import com.example.model.strategy.tipoNivel.Principiante;
import com.example.notification.service.InMemoryUserDirectory;
import com.example.notification.service.NotificationService;
import com.example.notification.service.UserDirectory;
import com.example.notification.strategy.EmailNotificationStrategy;
import com.example.notification.strategy.NotificationStrategy;
import com.example.notification.strategy.PushNotificationStrategy;
import com.example.notification.adapter.JavaMailEmailClientAdapter;
import com.example.notification.adapter.FirebasePushClientAdapter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola que ofrece un menú interactivo para operar con el
 * sistema de gestión de encuentros deportivos.  Permite registrar
 * jugadores, iniciar sesión, crear partidos, buscarlos mediante diversas
 * estrategias y administrar su ciclo de vida.  Esta vista utiliza
 * {@link GestorEmparejamiento} para gestionar los partidos y
 * {@link NotificationService} para notificar a los usuarios cuando
 * corresponde.
 */
public class MenuView {
    private final UserDirectory userDirectory;
    private final NotificationService notificationService;
    private final GestorEmparejamiento gestor;
    private final Scanner scanner;

    /**
     * Jugador autenticado en la sesión actual.  Se actualiza al iniciar
     * sesión y se establece en {@code null} al cerrar.
     */
    private Jugador usuarioActual;

    public MenuView() {
        this.userDirectory = new InMemoryUserDirectory();
        NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());
        this.notificationService = new NotificationService(email, push, userDirectory);
        this.gestor = GestorEmparejamiento.getInstance();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia el ciclo de ejecución del menú.  El método no retorna hasta
     * que el usuario seleccione la opción de salir.
     */
    public void run() {
        boolean salir = false;
        while (!salir) {
            mostrarOpciones();
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> registrarUsuario();
                case "2" -> iniciarSesion();
                case "3" -> crearPartido();
                case "4" -> buscarPartidos();
                case "5" -> unirseAPartido();
                case "6" -> confirmarPartido();
                case "7" -> iniciarPartido();
                case "8" -> finalizarPartido();
                case "9" -> cancelarPartido();
                case "0" -> salir = true;
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }
            System.out.println();
        }
        System.out.println("Saliendo de la aplicación. ¡Hasta luego!");
    }

    private void mostrarOpciones() {
        System.out.println("\n=== Menú Principal ===");
        System.out.println("1. Registrar nuevo usuario");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Crear partido");
        System.out.println("4. Buscar partidos");
        System.out.println("5. Unirse a un partido");
        System.out.println("6. Confirmar partido");
        System.out.println("7. Iniciar partido");
        System.out.println("8. Finalizar partido");
        System.out.println("9. Cancelar partido");
        System.out.println("0. Salir");
    }

    /**
     * Solicita datos al usuario para registrar un nuevo jugador y lo
     * almacena en el directorio de usuarios.  Se incluyen las opciones de
     * deporte favorito, zona y nivel de habilidad.
     */
    private void registrarUsuario() {
        System.out.println("\n--- Registro de usuario ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Correo electrónico: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine().trim();
        System.out.print("Deporte favorito (ej. Futbol, Basquet): ");
        String nombreDeporte = scanner.nextLine().trim();
        Deporte deporte = new Deporte(nombreDeporte);
        System.out.print("Zona (ej. Norte, Sur, Centro): ");
        String nombreZona = scanner.nextLine().trim();
        Zona zona = new Zona(nombreZona);
        ITipoNivel nivel = leerNivel();
        Jugador jugador = new Jugador(nombre, email, password, 0, deporte, zona);
        jugador.setNivel(nivel);
        // Para simplificar no solicitamos edad real ni token push
        ((InMemoryUserDirectory) userDirectory).add(jugador);
        System.out.println("Usuario registrado con éxito.");
    }

    /**
     * Solicita al usuario sus credenciales y, si se encuentran en el
     * directorio, establece al jugador como usuario actual.  El login se
     * realiza buscando por correo electrónico y comparando contraseñas.
     */
    private void iniciarSesion() {
        System.out.println("\n--- Iniciar sesión ---");
        System.out.print("Correo electrónico: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine().trim();
        Jugador j = null;
        if (userDirectory instanceof InMemoryUserDirectory) {
            j = ((InMemoryUserDirectory) userDirectory).findByEmail(email);
        }
        if (j != null && password.equals(j.getPassword())) {
            usuarioActual = j;
            System.out.println("Bienvenido, " + usuarioActual.getNombre() + "!");
        } else {
            System.out.println("Credenciales inválidas. Por favor registre un usuario o intente nuevamente.");
        }
    }

    /**
     * Permite al usuario autenticado crear un nuevo partido.  Se solicitan
     * los datos mínimos y se configura el estado inicial.  El partido se
     * agrega al gestor y se suscribe a las notificaciones para que los
     * eventos de cambio de estado se envíen a los participantes.
     */
    private void crearPartido() {
        if (!verificarSesion()) return;
        System.out.println("\n--- Crear partido ---");
        System.out.print("Deporte (ej. Futbol, Basquet): ");
        String depName = scanner.nextLine().trim();
        Deporte deporte = new Deporte(depName);
        int jugadoresRequeridos = leerEntero("Cantidad de jugadores requeridos: ");
        Partido partido = gestor.crearPartido(usuarioActual, deporte, jugadoresRequeridos);
        System.out.print("Zona del encuentro: ");
        String zoneName = scanner.nextLine().trim();
        partido.setUbicacion(new Zona(zoneName));
        System.out.println("Configuración de niveles. Ingrese un valor entre 1 (Principiante) y 3 (Avanzado) o 0 para omitir.");
        System.out.print("Nivel mínimo permitido: ");
        int minVal = leerEntero();
        if (minVal > 0) partido.setMinNivel(crearNivelDesdeValor(minVal));
        System.out.print("Nivel máximo permitido: ");
        int maxVal = leerEntero();
        if (maxVal > 0) partido.setMaxNivel(crearNivelDesdeValor(maxVal));
        // Suscribir a notificaciones para que los participantes reciban avisos de cambios
        partido.addObserver(notificationService);
        System.out.println("Partido creado correctamente.");
    }

    /**
     * Muestra los partidos disponibles según una estrategia elegida por el
     * usuario (zona, nivel, historial o sin filtro).  El usuario debe
     * estar autenticado para poder realizar la búsqueda.
     */
    private void buscarPartidos() {
        if (!verificarSesion()) return;
        System.out.println("\n--- Buscar partidos ---");
        System.out.println("Seleccione la estrategia de emparejamiento:");
        System.out.println("1. Por zona");
        System.out.println("2. Por nivel");
        System.out.println("3. Por historial");
        System.out.println("4. Sin filtro");
        System.out.print("Opción: ");
        String op = scanner.nextLine().trim();
        switch (op) {
            case "1" -> gestor.setEstrategia(new EmparejamientoZonaImpl());
            case "2" -> gestor.setEstrategia(new EmparejamientoNivelImpl());
            case "3" -> gestor.setEstrategia(new EmparejamientoHistorialImpl());
            default -> gestor.setEstrategia(null);
        }
        List<Partido> encontrados = gestor.buscarPartidosPara(usuarioActual);
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron partidos para los criterios seleccionados.");
            return;
        }
        System.out.println("Partidos encontrados:");
        int idx = 1;
        for (Partido p : encontrados) {
            System.out.println(idx++ + ". " + descripcionPartido(p));
        }
    }

    /**
     * Permite al usuario actual unirse a uno de los partidos listados por
     * el gestor.  Si el partido alcanzara el cupo de jugadores tras la
     * operación, cambiará automáticamente su estado y notificará a los
     * participantes.
     */
    private void unirseAPartido() {
        if (!verificarSesion()) return;
        List<Partido> lista = gestor.getPartidos();
        if (lista.isEmpty()) {
            System.out.println("No hay partidos disponibles.");
            return;
        }
        System.out.println("\n--- Unirse a un partido ---");
        mostrarListadoPartidos(lista);
        int opcion = leerEntero("Seleccione el número de partido al que desea unirse (0 para cancelar): ");
        if (opcion <= 0 || opcion > lista.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = lista.get(opcion - 1);
        elegido.agregarJugador(usuarioActual);
    }

    /**
     * Permite al organizador confirmar un partido.  La confirmación sólo
     * puede hacerse si el usuario actual es el organizador y el partido
     * ya se encuentra armado (es decir, alcanzó el cupo de jugadores).
     */
    private void confirmarPartido() {
        if (!verificarSesion()) return;
        List<Partido> lista = gestor.getPartidos();
        if (lista.isEmpty()) {
            System.out.println("No hay partidos creados.");
            return;
        }
        System.out.println("\n--- Confirmar partido ---");
        mostrarListadoPartidos(lista);
        int opcion = leerEntero("Seleccione el partido a confirmar (0 para cancelar): ");
        if (opcion <= 0 || opcion > lista.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = lista.get(opcion - 1);
        if (!usuarioActual.equals(elegido.getOrganizador())) {
            System.out.println("Solo el organizador puede confirmar el partido.");
            return;
        }
        elegido.confirmar();
        System.out.println("Partido confirmado correctamente.");
    }

    /**
     * Permite iniciar un partido.  El usuario debe ser el organizador y el
     * partido debe estar confirmado previamente.
     */
    private void iniciarPartido() {
        if (!verificarSesion()) return;
        List<Partido> lista = gestor.getPartidos();
        if (lista.isEmpty()) {
            System.out.println("No hay partidos creados.");
            return;
        }
        System.out.println("\n--- Iniciar partido ---");
        mostrarListadoPartidos(lista);
        int opcion = leerEntero("Seleccione el partido a iniciar (0 para cancelar): ");
        if (opcion <= 0 || opcion > lista.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = lista.get(opcion - 1);
        if (!usuarioActual.equals(elegido.getOrganizador())) {
            System.out.println("Solo el organizador puede iniciar el partido.");
            return;
        }
        elegido.iniciar();
        System.out.println("Partido iniciado correctamente.");
    }

    /**
     * Permite finalizar un partido.  El usuario debe ser el organizador y
     * el partido debe estar en juego.
     */
    private void finalizarPartido() {
        if (!verificarSesion()) return;
        List<Partido> lista = gestor.getPartidos();
        if (lista.isEmpty()) {
            System.out.println("No hay partidos creados.");
            return;
        }
        System.out.println("\n--- Finalizar partido ---");
        mostrarListadoPartidos(lista);
        int opcion = leerEntero("Seleccione el partido a finalizar (0 para cancelar): ");
        if (opcion <= 0 || opcion > lista.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = lista.get(opcion - 1);
        if (!usuarioActual.equals(elegido.getOrganizador())) {
            System.out.println("Solo el organizador puede finalizar el partido.");
            return;
        }
        elegido.finalizar();
        System.out.println("Partido finalizado correctamente.");
    }

    /**
     * Permite cancelar un partido antes de su inicio.  El usuario debe ser
     * el organizador para ejecutar la cancelación.
     */
    private void cancelarPartido() {
        if (!verificarSesion()) return;
        List<Partido> lista = gestor.getPartidos();
        if (lista.isEmpty()) {
            System.out.println("No hay partidos creados.");
            return;
        }
        System.out.println("\n--- Cancelar partido ---");
        mostrarListadoPartidos(lista);
        int opcion = leerEntero("Seleccione el partido a cancelar (0 para cancelar): ");
        if (opcion <= 0 || opcion > lista.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = lista.get(opcion - 1);
        if (!usuarioActual.equals(elegido.getOrganizador())) {
            System.out.println("Solo el organizador puede cancelar el partido.");
            return;
        }
        elegido.cancelar();
        System.out.println("Partido cancelado correctamente.");
    }

    // Utilidades
    private boolean verificarSesion() {
        if (usuarioActual == null) {
            System.out.println("Debe iniciar sesión para realizar esta acción.");
            return false;
        }
        return true;
    }

    private void mostrarListadoPartidos(List<Partido> lista) {
        int i = 1;
        for (Partido p : lista) {
            System.out.println(i++ + ". " + descripcionPartido(p));
        }
    }

    private String descripcionPartido(Partido p) {
        StringBuilder sb = new StringBuilder();
        sb.append("Organizador: ").append(p.getOrganizador() != null ? p.getOrganizador().getNombre() : "<sin organizador>");
        sb.append(", Deporte: ").append(p.getDeporte() != null ? p.getDeporte().getNombre() : "<sin deporte>");
        sb.append(", Zona: ").append(p.getUbicacion() != null ? p.getUbicacion().getNombre() : "<sin zona>");
        sb.append(", Jugadores: ").append(p.getJugadores() != null ? p.getJugadores().size() : 0).append("/").append(p.getJugadoresRequeridos());
        sb.append(", Nivel mínimo: ").append(p.getMinNivel() != null ? p.getMinNivel().getNombre() : "Cualquiera");
        sb.append(", Nivel máximo: ").append(p.getMaxNivel() != null ? p.getMaxNivel().getNombre() : "Cualquiera");
        sb.append(", Estado: ").append(p.getEstado() != null ? p.getEstado().getNombreEstado() : "<sin estado>");
        return sb.toString();
    }

    private ITipoNivel leerNivel() {
        while (true) {
            System.out.print("Nivel (1=Principiante, 2=Intermedio, 3=Avanzado): ");
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= 1 && val <= 3) {
                    return crearNivelDesdeValor(val);
                }
            } catch (NumberFormatException e) {
                // ignore
            }
            System.out.println("Entrada inválida. Debe ser 1, 2 o 3.");
        }
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return leerEntero();
    }

    private int leerEntero() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Por favor ingrese un número: ");
            }
        }
    }

    private ITipoNivel crearNivelDesdeValor(int val) {
        return switch (val) {
            case 1 -> new Principiante();
            case 2 -> new Intermedio();
            case 3 -> new Avanzado();
            default -> null;
        };
    }
}