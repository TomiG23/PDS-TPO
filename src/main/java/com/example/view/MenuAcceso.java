package com.example.view;

import java.util.List;
import java.util.Scanner;

public class MenuAcceso extends View {
    private boolean salir = false;

    public MenuAcceso(Scanner scanner) {
        super(scanner);
    }

    public void mostrarMenu() {
        mostrarOpcipnes(List.of("Salir", "Registrarse", "Ingresar"));
        String opcion = seleccionarOpcion();
        switch (opcion) {
            case "1" -> registrarUsuario();
            case "2" -> iniciarSesion();
            case "0" -> salir();
            default -> System.out.println("Opción no válida. Intente nuevamente.");
        }
    }

    private void registrarUsuario() {
        RegistrarUsuario registrarUsuario = new RegistrarUsuario(scanner);
        registrarUsuario.mostarRegistro();
    }

    private void iniciarSesion() {
        IngresarUsuarioView ingresarUsuarioView = new IngresarUsuarioView(scanner);
        ingresarUsuarioView.mostrarIngreso();
    }

    private void salir() {
        salir = true;
    }

    public boolean getSalir() {
        return salir;
    }
}

