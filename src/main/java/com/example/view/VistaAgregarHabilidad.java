package com.example.view;

import com.example.model.entity.Habilidad;
import com.example.model.strategy.tipoNivel.ITipoNivel;

import java.util.Scanner;

public class VistaAgregarHabilidad extends View {
    private Habilidad habilidad;
    public VistaAgregarHabilidad(Scanner scanner) {
        super(scanner);
    }

    public void mostrarAgregarHabilidad() {
        habilidad = new Habilidad();
        // todo: listar deportes
        System.out.print("Ingrese su deporte favortio: ");
        String deporte = scanner.next();

        seleccionarNivel();
    }

    private void seleccionarNivel() {
        while (true) {
            System.out.print("Nivel (1=Principiante, 2=Intermedio, 3=Avanzado): ");
            String input = scanner.next().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= 1 && val <= 3) {
                    habilidad.seleccionarNivel(val);
                    break;
                }
            } catch (NumberFormatException e) {
                // ignore
            }
            System.out.println("Entrada inválida. Debe ser 1, 2 o 3.");
        }
    }

    public Habilidad getHabilidad() {
        return habilidad;
    }
}
