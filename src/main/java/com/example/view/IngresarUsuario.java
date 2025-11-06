package com.example.view;

import java.util.Scanner;

public class IngresarUsuario extends View {

    public IngresarUsuario(Scanner scanner) {
        super(scanner);
    }

    public void mostrarIngreso() {
        System.out.print("Ingrese su usuario: ");
        String usuario = scanner.next();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.next();
        // llamar a controller de login
    }
}
