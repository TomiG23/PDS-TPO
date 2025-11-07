package com.example.view;

import com.example.model.entity.*;
// import com.example.model.strategy.emparejamiento.EmparejamientoHistorialImpl;
// import com.example.model.strategy.emparejamiento.EmparejamientoNivelImpl;
import com.example.model.strategy.emparejamiento.EmparejamientoHistorialImpl;
import com.example.model.strategy.emparejamiento.EmparejamientoNivelImpl;
import com.example.model.strategy.emparejamiento.EmparejamientoZonaNivelCustomImpl;
import com.example.model.strategy.emparejamiento.GestorEmparejamiento;
// import com.example.model.strategy.tipoNivel.Avanzado;
import com.example.model.strategy.tipoDeporte.tipoNivel.ITipoDeporte;
import com.example.model.strategy.tipoNivel.Avanzado;
import com.example.model.strategy.tipoNivel.Intermedio;
import com.example.model.strategy.tipoNivel.ITipoNivel;
// import com.example.model.strategy.tipoNivel.Principiante;
import com.example.model.strategy.tipoNivel.Principiante;
import com.example.notification.service.NotificationService;
// import com.example.notification.strategy.EmailNotificationStrategy;
import com.example.notification.strategy.NotificationStrategy;
// import com.example.notification.strategy.PushNotificationStrategy;
// import com.example.notification.adapter.JavaMailEmailClientAdapter;
// import com.example.notification.adapter.FirebasePushClientAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
public class MenuView extends View {
    private final Sesion sesion;
    private final NotificationService notificationService;
    private final GestorEmparejamiento gestor;

    /**
     * Jugador autenticado en la sesión actual.  Se actualiza al iniciar
     * sesión y se establece en {@code null} al cerrar.
     */
    private Jugador usuarioActual;

    public MenuView() {
        super(new Scanner(System.in));
        sesion = Sesion.getInstance();
        // NotificationStrategy email = new EmailNotificationStrategy(new JavaMailEmailClientAdapter());
        // NotificationStrategy push = new PushNotificationStrategy(new FirebasePushClientAdapter());
        // this.notificationService = new NotificationService(email, push);
        this.notificationService = new NotificationService(null, null);
        this.gestor = GestorEmparejamiento.getInstance();
    }

    /**
     * Constructor que acepta un NotificationService ya configurado.
     * Útil cuando se quiere compartir la misma instancia entre la demo y el menú.
     */
    public MenuView(NotificationService notificationService) {
        super(new Scanner(System.in));
        sesion = Sesion.getInstance();
        this.notificationService = notificationService;
        this.gestor = GestorEmparejamiento.getInstance();
    }

    /**
     * Inicia el ciclo de ejecución del menú.  El método no retorna hasta
     * que el usuario seleccione la opción de salir.
     */
    public void run() {
        MenuAcceso menuAcceso = new MenuAcceso(scanner);
        while (!menuAcceso.getSalir()) {
            boolean estaLoggeado = sesion.getUsuarioActual() != null;

            if (!estaLoggeado) {
                System.out.println("\n======");
                menuAcceso.mostrarMenu();
            } else {
                usuarioActual = sesion.getUsuarioActual();
                System.out.println("\n=== Menú Principal === " + usuarioActual.getNombre());
                mostrarOpcipnes(List.of("Cerrar sesion", "Crear partido", "Buscar partidos", "Agregar habilidad", "Confirmar partido", "Cancelar partido"));
                String opcion = seleccionarOpcion();
                switch (opcion) {
                    case "1" -> crearPartido();
                    case "2" -> buscarPartidos();
                    case "3" -> agregarHabilidad();
                    case "4" -> confirmarPartido();
                    case "5" -> cancelarPartido();
                    case "0" -> cerrarSesion();
                    default -> System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
            System.out.println();
        }
        System.out.println("Saliendo de la aplicación. ¡Hasta luego!");
    }

    private void cerrarSesion() {
        sesion.cerrarSesion();
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
        VistaAgregarHabilidad vistaAgregarHabilidad = new VistaAgregarHabilidad(scanner);
        ITipoDeporte tipoDeporte = vistaAgregarHabilidad.leerDeportes();
        Deporte deporte = new Deporte(tipoDeporte);
        int jugadoresRequeridos = leerOpciones("Cantidad de jugadores requeridos (2 a 15): ", 2, 15);
        Partido partido = gestor.crearPartido(usuarioActual, deporte, jugadoresRequeridos);

        // Solicitar fecha
        LocalDate fecha = leerFecha();

        // Solicitar horario
        LocalTime hora = leerHora();

        // Combinar fecha y hora
        LocalDateTime horario = LocalDateTime.of(fecha, hora);
        partido.setHorario(horario);

        // Solicitar ubicación (dirección/lugar específico)
        String ubicacionStr = leerLinea("Ubicación del encuentro (ej. Estadio Luna Park, Calle 123): ");

        // Solicitar zona
        String zoneName = leerLinea("Zona del encuentro (ej. Norte, Sur, Centro): ");

        Zona zona = new Zona(zoneName);
        zona.setUbicacion(ubicacionStr);
        partido.setUbicacion(zona);

        // Preguntar si desea configurar niveles
        String deseaConfig = leerLinea("¿Desea configurar el nivel? (s/n): ");
        if (deseaConfig.equalsIgnoreCase("s") || deseaConfig.equalsIgnoreCase("si") || deseaConfig.equalsIgnoreCase("sí")) {
            System.out.println("Configuración de niveles. Ingrese un valor entre 1 (Principiante) y 3 (Avanzado) o 0 para omitir.");
            System.out.print("Nivel mínimo permitido: ");
            int minVal = leerEntero();
            if (minVal > 0) partido.setMinNivel(crearNivelDesdeValor(minVal));
            System.out.print("Nivel máximo permitido: ");
            int maxVal = leerEntero();
            if (maxVal > 0) partido.setMaxNivel(crearNivelDesdeValor(maxVal));
        }
        // Suscribir a notificaciones para que los participantes reciban avisos de cambios
        partido.addObserver(notificationService);
        System.out.println("Partido creado correctamente. Tú estás registrado como jugador (1/" + jugadoresRequeridos + ").");
        
        // Disparar evento de creación para enviar notificaciones
        partido.publicarCreacion();
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
        String op = scanner.next().trim();
        switch (op) {
            case "1" -> {
                // Obtener zonas únicas de los partidos disponibles
                List<Partido> todosPartidos = gestor.getPartidos();
                List<String> zonasDisponibles = new ArrayList<>();
                for (Partido p : todosPartidos) {
                    if (p.getUbicacion() != null) {
                        String nombreZona = p.getUbicacion().getNombre();
                        if (nombreZona != null && !zonasDisponibles.contains(nombreZona)) {
                            zonasDisponibles.add(nombreZona);
                        }
                    }
                }

                if (zonasDisponibles.isEmpty()) {
                    System.out.println("No hay zonas disponibles en los partidos existentes.");
                    return;
                }

                // Mostrar zonas disponibles
                System.out.println("\nZonas disponibles:");
                for (int i = 0; i < zonasDisponibles.size(); i++) {
                    System.out.println((i + 1) + ". " + zonasDisponibles.get(i));
                }

                int opcionZona;
                while (true) {
                    opcionZona = leerEntero("Seleccione el número de zona (0 para volver al menú): ");
                    if (opcionZona == 0) {
                        System.out.println("Volviendo al menú principal.");
                        return;
                    }
                    if (opcionZona > 0 && opcionZona <= zonasDisponibles.size()) {
                        break; // Opción válida
                    }
                    System.out.println("Opción inválida. Seleccione una zona de la lista o 0 para volver al menú.");
                }

                String zonaNombre = zonasDisponibles.get(opcionZona - 1);
                Zona zonaBuscada = new Zona(zonaNombre);
                gestor.setEstrategia(new EmparejamientoZonaNivelCustomImpl(zonaBuscada));
            }
            case "2" -> gestor.setEstrategia(new EmparejamientoNivelImpl());
            case "3" -> gestor.setEstrategia(new EmparejamientoHistorialImpl());
            default -> gestor.setEstrategia(null);
        }
        List<Partido> encontrados = gestor.buscarPartidosPara(usuarioActual);

        // Filtrar partidos creados por el usuario actual, partidos a los que ya se unió, y partidos armados
        List<Partido> partidosFiltrados = new ArrayList<>();
        for (Partido p : encontrados) {
            // Excluir si es el organizador
            if (p.getOrganizador() != null && p.getOrganizador().equals(usuarioActual)) {
                continue;
            }
            // Excluir si ya está en la lista de jugadores
            if (p.getJugadores() != null && p.getJugadores().contains(usuarioActual)) {
                continue;
            }
            // Excluir si el partido está en estado "PartidoArmado"
            if (p.getEstado() != null && "PartidoArmado".equals(p.getEstado().getNombreEstado())) {
                continue;
            }
            partidosFiltrados.add(p);
        }

        if (partidosFiltrados.isEmpty()) {
            System.out.println("No se encontraron partidos para los criterios seleccionados.");
            return;
        }
        System.out.println("Partidos encontrados:");
        int idx = 1;
        for (Partido p : partidosFiltrados) {
            System.out.println(idx++ + ". " + descripcionPartido(p));
        }

        // Preguntar si desea unirse a alguno con validación
        System.out.println("\n¿Desea unirse a alguno de estos partidos?");
        int opcionUnirse;
        while (true) {
            opcionUnirse = leerEntero("Seleccione el número de partido (0 para volver al menú principal): ");
            if (opcionUnirse == 0) {
                System.out.println("Volviendo al menú principal.");
                return;
            }
            if (opcionUnirse > 0 && opcionUnirse <= partidosFiltrados.size()) {
                break; // Opción válida
            }
            System.out.println("Opción inválida. Seleccione un partido de la lista o 0 para volver al menú principal.");
        }
        Partido elegido = partidosFiltrados.get(opcionUnirse - 1);

        // Validar que no sea su propio partido
        if (elegido.getOrganizador() != null && elegido.getOrganizador().equals(usuarioActual)) {
            System.out.println("No puedes unirte a tu propio partido. Ya estás registrado como organizador.");
            return;
        }

        // Validar que no esté ya anotado
        if (elegido.getJugadores() != null && elegido.getJugadores().contains(usuarioActual)) {
            System.out.println("Ya estás anotado en este partido.");
            return;
        }

        elegido.agregarJugador(usuarioActual);
        System.out.println("Te has unido al partido organizado por " + elegido.getOrganizador().getNombre());
    }

    /**
     * Permite a un jugador confirmar su asistencia a un partido al que se anotó.
     */
    private void confirmarPartido() {
        if (!verificarSesion()) return;
        List<Partido> todos = gestor.getPartidos();
        List<Partido> inscritos = new ArrayList<>();
        for (Partido p : todos) {
            if (p.getJugadores() != null && p.getJugadores().contains(usuarioActual)
                    && (p.getOrganizador() == null || !usuarioActual.equals(p.getOrganizador()))) {
                inscritos.add(p);
            }
        }
        if (inscritos.isEmpty()) {
            System.out.println("No tenés partidos para confirmar asistencia.");
            return;
        }
        System.out.println("\n--- Confirmar asistencia ---");
        mostrarListadoPartidos(inscritos);
        int opcion = leerEntero("Seleccione el partido para confirmar asistencia (0 para cancelar): ");
        if (opcion <= 0 || opcion > inscritos.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = inscritos.get(opcion - 1);
        elegido.confirmarAsistencia(usuarioActual);
        System.out.println("Asistencia confirmada correctamente.");
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
     * Permite cancelar un partido organizado por el usuario actual.
     */
    private void cancelarPartido() {
        if (!verificarSesion()) return;
        List<Partido> todos = gestor.getPartidos();
        List<Partido> propios = new ArrayList<>();
        for (Partido p : todos) {
            if (p.getOrganizador() != null && p.getOrganizador().equals(usuarioActual)) {
                propios.add(p);
            }
        }
        if (propios.isEmpty()) {
            System.out.println("No tenés partidos organizados para cancelar.");
            return;
        }
        System.out.println("\n--- Cancelar partido ---");
        mostrarListadoPartidos(propios);
        int opcion = leerEntero("Seleccione el partido a cancelar (0 para cancelar): ");
        if (opcion <= 0 || opcion > propios.size()) {
            System.out.println("Operación cancelada.");
            return;
        }
        Partido elegido = propios.get(opcion - 1);
        elegido.cancelar();
        System.out.println("Partido cancelado correctamente.");
    }

    private void agregarHabilidad() {
        VistaAgregarHabilidad vistaAgregarHabilidad = new VistaAgregarHabilidad(scanner);
        vistaAgregarHabilidad.mostrarAgregarHabilidad();
        usuarioActual.agregarDeporte(vistaAgregarHabilidad.getHabilidad());
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
        if (p.getUbicacion() != null && p.getUbicacion().getUbicacion() != null) {
            sb.append(", Ubicación: ").append(p.getUbicacion().getUbicacion());
        }
        if (p.getHorario() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            sb.append(", Fecha/Hora: ").append(p.getHorario().format(formatter));
        }
        sb.append(", Jugadores: ").append(p.getJugadores() != null ? p.getJugadores().size() : 0).append("/").append(p.getJugadoresRequeridos());
        sb.append(", Nivel mínimo: ").append(p.getMinNivel() != null ? p.getMinNivel().getNombre() : "Cualquiera");
        sb.append(", Nivel máximo: ").append(p.getMaxNivel() != null ? p.getMaxNivel().getNombre() : "Cualquiera");
        sb.append(", Estado: ").append(p.getEstado() != null ? p.getEstado().getNombreEstado() : "<sin estado>");
        return sb.toString();
    }

    private ITipoNivel crearNivelDesdeValor(int val) {
        return switch (val) {
            case 1 -> new Principiante();
            case 2 -> new Intermedio();
            case 3 -> new Avanzado();
            default -> null;
        };
    }

    private LocalDate leerFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            System.out.print("Fecha del partido (formato dd/MM/yyyy, ej. 25/12/2024): ");
            String input = scanner.next().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Use dd/MM/yyyy (ej. 25/12/2024)");
            }
        }
    }

    private LocalTime leerHora() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        while (true) {
            System.out.print("Horario del partido (formato HH:mm, ej. 18:30): ");
            String input = scanner.next().trim();
            try {
                return LocalTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de horario inválido. Use HH:mm (ej. 18:30)");
            }
        }
    }
}