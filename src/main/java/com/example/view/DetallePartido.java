package com.example.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetallePartido extends View {
    String partido;
    public DetallePartido(Scanner scanner) {
        super(scanner);
        this.partido = partido;
    }

    public void mostrarDetalle(String partido) {
        this.partido = partido;
        System.out.println("Partido: " + partido);
        ArrayList<String> opciones = new ArrayList<>(List.of("Aceptar", "Volver"));
        mostrarOpcipnes(opciones);
        String opcion = seleccionarOpcion();
        switch (opcion) {
            case "0":
                System.out.println("Partido aceptado");
                break;
            case "1":
                break;
            default:
                System.out.println("opcion no valida");
        }
    }
}
