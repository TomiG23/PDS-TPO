package com.example.view;

import java.util.Scanner;

public class RegistrarUsuario {
    public void mostarRegistro(Scanner scanner) {
        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.next();
        System.out.print("Ingrese su direccion de correo: ");
        String correo = scanner.next();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.next();
        System.out.print("Desea agregar se deporte favorito?(s/n): ");
        String tieneFavortio = scanner.next();
        if (tieneFavortio.toLowerCase().equals("s")) {
            VistaAgregarHabilidad vistaAgregarHabilidad = new VistaAgregarHabilidad(scanner);
            vistaAgregarHabilidad.mostrarAgregarHabilidad();
        }
        // todo: llamar a registro
    }
}
