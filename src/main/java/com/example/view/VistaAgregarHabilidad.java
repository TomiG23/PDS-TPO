package com.example.view;

import com.example.model.entity.Habilidad;
import com.example.model.entity.Deporte;
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
        String depNombre = leerLinea("Ingrese su deporte favortio: ");
        habilidad.setDeporte(new Deporte(depNombre));

        seleccionarNivel();
    }

    private void seleccionarNivel() {
        while (true) {
            System.out.print("Nivel (1=Principiante, 2=Intermedio, 3=Avanzado): ");
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= 1 && val <= 3) {
                    ITipoNivel nivel = habilidad.seleccionarNivel(val);
                    habilidad.setNivel(nivel);
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
