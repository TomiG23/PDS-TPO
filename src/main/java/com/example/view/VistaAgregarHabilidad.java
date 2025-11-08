package com.example.view;

import com.example.model.entity.Habilidad;
import com.example.model.entity.Deporte;
import com.example.model.strategy.tipoDeporte.tipoNivel.Basket;
import com.example.model.strategy.tipoDeporte.tipoNivel.Futbol;
import com.example.model.strategy.tipoDeporte.tipoNivel.ITipoDeporte;
import com.example.model.strategy.tipoDeporte.tipoNivel.Voley;
import com.example.model.strategy.tipoNivel.ITipoNivel;

import java.util.List;
import java.util.Scanner;

public class VistaAgregarHabilidad extends View {
    private Habilidad habilidad;
    public VistaAgregarHabilidad(Scanner scanner) {
        super(scanner);
    }

    public ITipoDeporte leerDeportes() {
        mostrarOpcipnes(List.of(new Basket(), new Futbol(), new Voley()).stream().map(ITipoDeporte::verTipo).toList());
        int opcion = leerOpciones("Seleccione el deporte: ",0, 2);

        switch (opcion) {
            case 0 -> {
                return new Basket();
            }
            case 1 -> {
                return new Futbol();
            }
            case 2 -> {
                return new Voley();
            }
            default -> System.out.println("EL deporte ingresado no es valido");
        }
        return null;
    }

    public void mostrarAgregarHabilidad() {
        habilidad = new Habilidad();
        ITipoDeporte ITipoDeporte = leerDeportes();

        habilidad.setDeporte(new Deporte(ITipoDeporte));

        seleccionarNivel();
    }

    private void seleccionarNivel() {
        while (true) {
            System.out.print("Nivel (1=Principiante, 2=Intermedio, 3=Avanzado): ");
            String input = scanner.next().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= 1 && val <= 3) {
                    ITipoNivel nivel = habilidad.seleccionarNivel(val);
                    habilidad.setNivel(nivel);
                    break;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Entrada inválida. Debe ser 1, 2 o 3.");
        }
    }

    public Habilidad getHabilidad() {
        return habilidad;
    }
}
