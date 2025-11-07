package com.example.view;

import com.example.model.entity.Jugador;
import com.example.model.entity.Sesion;

import java.util.Scanner;

public class RegistrarUsuario extends View {
    private Sesion sesion = Sesion.getInstance();

    public RegistrarUsuario(Scanner scanner) {
        super(scanner);
    }

    public void mostarRegistro() {
        System.out.println("\n--- Registro de usuario ---");
        // Validar nombre (no vacío)
        String nombre;
        while (true) {
            System.out.print("Nombre (o presione 0 para salir): ");
            nombre = scanner.nextLine().trim();
            if (nombre.equals("0")) {
                System.out.println("Registro cancelado.");
                return;
            }
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre no puede estar vacío.");
                continue;
            }
            break;
        }

        // Validar email (no vacío y no ocupado)
        String email;
        while (true) {
            System.out.print("Correo electrónico (o presione 0 para salir): ");
            email = scanner.nextLine().trim();
            if (email.equals("0")) {
                System.out.println("Registro cancelado.");
                return;
            }
            if (email.isEmpty()) {
                System.out.println("Error: El correo electrónico no puede estar vacío.");
                continue;
            }
            boolean estaMailOcupado = sesion.findByEmail(email) instanceof Jugador;
            if (estaMailOcupado) {
                System.out.println("Error: El mail ya se encuentra registrado.");
                continue;
            }
            break;
        }

        // Validar contraseña (no vacía y al menos 3 caracteres)
        String password;
        while (true) {
            System.out.print("Contraseña (mínimo 3 caracteres, o presione 0 para salir): ");
            password = scanner.nextLine().trim();
            if (password.equals("0")) {
                System.out.println("Registro cancelado.");
                return;
            }
            if (password.isEmpty()) {
                System.out.println("Error: La contraseña no puede estar vacía.");
                continue;
            }
            if (password.length() < 3) {
                System.out.println("Error: La contraseña debe tener al menos 3 caracteres.");
                continue;
            }
            break;
        }

        // Validar opción de deporte favorito (solo s o n)
        String tieneFavorito;
        while (true) {
            System.out.print("¿Desea agregar su deporte favorito? (s/n, o presione 0 para salir): ");
            tieneFavorito = scanner.nextLine().trim().toLowerCase();
            if (tieneFavorito.equals("0")) {
                System.out.println("Registro cancelado.");
                return;
            }
            if (tieneFavorito.equals("s") || tieneFavorito.equals("n")) {
                break;
            }
            System.out.println("Error: Opción incorrecta. Ingrese 's' para sí o 'n' para no.");
        }

        Jugador jugador = new Jugador(nombre, email, password);
        if (tieneFavorito.equals("s")) {
            VistaAgregarHabilidad vistaAgregarHabilidad = new VistaAgregarHabilidad(scanner);
            vistaAgregarHabilidad.mostrarAgregarHabilidad();
            jugador.setDeporteFavorito(vistaAgregarHabilidad.getHabilidad());
        }
        
        Jugador jugadorRegistrado = sesion.registrar(jugador);
        if (jugadorRegistrado == null) {
            System.out.println("No se pudo registrar el Usuario");
        } else {
            System.out.println("Usuario registrado con éxito.");
        }
    }
}
