package com.example.view;

import java.util.Scanner;

public class VistaAgregarHabilidad extends View {
    public VistaAgregarHabilidad(Scanner scanner) {
        super(scanner);
    }

    public void mostrarAgregarHabilidad() {
        System.out.print("\n");
        // todo: listar deportes
        System.out.print("Ingrese su deporte favortio: ");
        String deporte = scanner.next();

        // todo: listar niveles de habilidad
        System.out.print("Ingrese su nivel de habilidad: ");
        String nivel = scanner.next();
    }
}
