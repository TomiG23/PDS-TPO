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
        System.out.print("Nombre: ");
        String nombre = scanner.next().trim();

        System.out.print("Correo electrónico: ");
        String email = scanner.next().trim();
        boolean estaMailOcupado = sesion.findByEmail(email) instanceof Jugador;
        while (estaMailOcupado) {
            System.out.print("Error: El mail ya se encuentra registrado\nIngrese otro email: ");
            email = scanner.next().trim();
            estaMailOcupado = sesion.findByEmail(email) instanceof Jugador;
        }

        System.out.print("Contraseña (3 caracteres minimo): ");
        String password = scanner.next().trim();
        boolean esPassValida = password.length() >= 3;
        while (!esPassValida) {
            System.out.print("Error: La contraseña debe ser de al menos 3 caracteres\nIngrese nuevamente: ");
            password = scanner.next().trim();
            esPassValida = password.length() >= 3;
        }


        System.out.print("Desea agregar se deporte favorito?(s/n): ");
        String tieneFavortio = scanner.next();
        Jugador jugador = new Jugador(nombre, email, password);
        if (tieneFavortio.toLowerCase().equals("s")) {
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
