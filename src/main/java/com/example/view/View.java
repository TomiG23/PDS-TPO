package com.example.view;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class View {
    protected Scanner  scanner;

    public View(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void mostrarOpcipnes(ArrayList<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            String partido = opciones.get(i);
            System.out.print("[" + i + "] " + partido + "\t");
        }
        System.out.print("\n");
    }

    protected int seleccionarOpcion(String mensaje, ArrayList<String> opciones) {
        System.out.print(mensaje);
        int opcion = this.scanner.nextInt();
        if (opcion < 0 || opcion > opciones.size()) {
            System.out.println(); // todo: manejo de error
        }
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
}
