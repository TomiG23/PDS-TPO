package com.example.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuAcceso extends View {

    public MenuAcceso(Scanner scanner) {
        super(scanner);
    }

    public void mostrarMenu(Scanner scanner) {
        boolean isLogging = true;
        while(isLogging){
            ArrayList<String> opciones = new ArrayList<>(List.of("Salir", "Ingresar", "Registrarse"));
            mostrarOpcipnes(opciones);
            int opcion = seleccionarOpcion("seleccione opcion> ", opciones);

            switch (opcion) {
                case 0:
                    isLogging = false;
                    break;
                case 1:
                    IngresarUsuarioView ingresarUsuario = new IngresarUsuarioView(scanner);
                    ingresarUsuario.mostrarIngreso();
                    isLogging = false;
                    break;
                case 2:
                    RegistrarUsuario registrarUsuario = new RegistrarUsuario();
                    registrarUsuario.mostarRegistro(scanner);
                    break;
            }
        }
    }
}
