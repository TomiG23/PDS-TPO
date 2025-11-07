package com.example.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class View {
    protected Scanner  scanner;

    public View(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void mostrarOpcipnes(List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            String partido = opciones.get(i);
            System.out.print("[" + i + "] " + partido + "   ");
        }
        System.out.print("\n");
    }

    protected String seleccionarOpcion() {
        System.out.print("Seleccione una opcion: ");
        String opcion = this.scanner.next().trim();
        return opcion;
    }

//    protected String seleccionarOpcion(ArrayList<String> opciones) {
//        System.out.print("Seleccione el numero de partido: ");
//        int opcion = this.scanner.nextInt();
//        if (opcion < 0 || opcion > opciones.size()) {
//            System.out.println(); // todo: manejo de error
//        }
//        return opciones.get(opcion);
//    }

    protected int leerOpciones(String mensaje, int min, int max) {
        System.out.print(mensaje);
        while (true) {
            String input = scanner.next().trim();
            try {
                int res = Integer.parseInt(input);
                if (res >= min && res <= max) {
                    return res;
                }
            } catch (NumberFormatException e) {
                System.out.print("Por favor ingrese un número: ");
            }
        }
    }

    protected String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return scanner.next().trim();
    }

    protected int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return leerEntero();
    }

    protected int leerEntero() {
        while (true) {
            String input = scanner.next().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Por favor ingrese un número: ");
            }
        }
    }
}
