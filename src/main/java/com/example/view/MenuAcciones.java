package com.example.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuAcciones extends View {
    public MenuAcciones(Scanner scanner) {
        super(scanner);
    }

    public void mostrarMenu() {
        boolean isActive = true;
        while(isActive) {
            ArrayList<String> opciones = new ArrayList<>(List.of("Salir", "Crear partido", "Buscar partido", "Agragar habilidad"));
            mostrarOpcipnes(opciones);
            String opcion = seleccionarOpcion();

            switch (opcion) {
                case "0":
                    isActive = false;
                    break;
                case "1":
                    // todo cambiar a configurar partido si hay un partido creado por el usuario
                    MenuCrearPartido menuCrearPartido = new MenuCrearPartido(scanner);
                    menuCrearPartido.mostrarCreacion();
                    break;
                case "2":
                    MenuBusquedaPartido menuBusquedaPartido = new MenuBusquedaPartido(scanner);
                    menuBusquedaPartido.mostrarMenu();
                    break;
                case "3":
                    VistaAgregarHabilidad vistaAgregarHabilidad = new VistaAgregarHabilidad(scanner);
                    vistaAgregarHabilidad.mostrarAgregarHabilidad();
                    break;
                default:
                    System.out.println("opcion no valida");
            }
        }
        System.out.println("Adios :c");
    }
}
