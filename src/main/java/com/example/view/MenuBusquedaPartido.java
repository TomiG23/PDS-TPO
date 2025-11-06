package com.example.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuBusquedaPartido extends View {
    String partidoSeleccionado;
    public MenuBusquedaPartido(Scanner scanner) {
        super(scanner);
    }

    private String seleccionarPartido(ArrayList<String> partidos) {
        System.out.print("Seleccione el numero de partido: ");
        int opcion = this.scanner.nextInt();
        if (opcion < 0 || opcion > partidos.size()) {
            System.out.println(); // todo: manejo de error
        }
        return partidos.get(opcion);
    }

    private void mostrarListaPartidos(ArrayList<String> partidos) {
        for (int i = 0; i < partidos.size(); i++) {
            String partido = partidos.get(i);
            System.out.print("[" + i + "] " + partido + "\t");
        }
        System.out.print("\n");
    }

    public void mostrarMenu() {
        ArrayList<String> opciones = new ArrayList<>(List.of("Volver", "busqueda por zona", "Busqueda por nivel partido", "Busqueda por historial"));
        mostrarOpcipnes(opciones);
        int opcion = seleccionarOpcion("seleccione opcion> ", opciones);

        DetallePartido detallePartido = new DetallePartido(scanner);

        switch (opcion) {
            case 0:
                break;
            case 1:
                ArrayList<String> partidosZona = new ArrayList<String>();
                partidosZona.add("pinamar");
                partidosZona.add("buenos aires");
                partidosZona.add("rosario");

                mostrarListaPartidos(partidosZona);
                partidoSeleccionado = seleccionarPartido(partidosZona);
                detallePartido.mostrarDetalle(partidoSeleccionado);
                break;
            case 2:
                ArrayList<String> partidosNivel = new ArrayList<String>();
                partidosNivel.add("basico");
                partidosNivel.add("intermedio");
                partidosNivel.add("avanzado");

                mostrarListaPartidos(partidosNivel);
                partidoSeleccionado = seleccionarPartido(partidosNivel);
                detallePartido.mostrarDetalle(partidoSeleccionado);
                break;
            case 3:
                System.out.println("Seleccione el numero de partido");
                ArrayList<String> partidosHistorial = new ArrayList<String>();
                partidosHistorial.add("basico");
                partidosHistorial.add("intermedio");
                partidosHistorial.add("avanzado");

                mostrarListaPartidos(partidosHistorial);
                partidoSeleccionado = seleccionarPartido(partidosHistorial);
                detallePartido.mostrarDetalle(partidoSeleccionado);
                break;
        }
    }
}
