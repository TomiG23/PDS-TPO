package com.example.view;

import com.example.dto.AccesoDTO;
import com.example.model.entity.Jugador;
import com.example.model.entity.Sesion;

import java.util.Scanner;

public class IngresarUsuarioView extends View {
    Sesion sesion = Sesion.getInstance();

    public IngresarUsuarioView(Scanner scanner) {
        super(scanner);
    }

    public void mostrarIngreso() {
        AccesoDTO usuario = new AccesoDTO();
        System.out.println("\n--- Iniciar sesión ---");
        System.out.print("Correo electrónico: ");
        String mail = scanner.next().trim();
        usuario.setMail(mail);
        System.out.print("Contraseña: ");
        String password = scanner.next().trim();
        usuario.setPassword(password);
        Jugador jugador = sesion.ingresar(usuario);
        if (jugador == null) {
            System.out.println("Credenciales inválidas. Por favor registre un usuario o intente nuevamente.");
        } else {
            System.out.println("Bienvenido, " + jugador.getNombre() + "!");
        }
    }
}
