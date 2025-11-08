package com.example.view;

import java.util.Scanner;

public class MenuCrearPartido extends View {
    public MenuCrearPartido(Scanner scanner) {
        super(scanner);
    }

    public void mostrarCreacion() {
        System.out.print("\nIngresar deporte: ");
        String deporte = scanner.next();
        System.out.print("Cantidad de jugadores requeridos: ");
        String jugadoresRequeridos = scanner.next();
        System.out.print("Duracion del encuentro (minutos): ");
        String duracion = scanner.next();
        System.out.print("Ingrese la fecha del encuentro: ");
        String fecha = scanner.next();
        System.out.print("Ingrese la zona del encuentro: ");
        String zona = scanner.next();
        System.out.println("partido creado");
    }
}
