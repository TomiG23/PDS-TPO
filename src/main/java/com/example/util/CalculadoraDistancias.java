package com.example.util;

import com.example.model.entity.Ubicacion;

public class CalculadoraDistancias {
    private static final double RADIO_TIERRA_KM = 6371.0;
    
    public static final double RADIO_MAXIMO_KM = 3.0;
    
    private CalculadoraDistancias() {
    }
    
    /**
     * Calcula la distancia en kilómetros entre dos ubicaciones usando la fórmula de Haversine
     * @param u1 Primera ubicación
     * @param u2 Segunda ubicación
     * @return Distancia en kilómetros entre las dos ubicaciones
     */
    public static double calcularDistancia(Ubicacion u1, Ubicacion u2) {
        if (u1 == null || u2 == null) {
            return Double.POSITIVE_INFINITY;
        }
        
        double lat1 = Math.toRadians(u1.getLatitud());
        double lon1 = Math.toRadians(u1.getLongitud());
        double lat2 = Math.toRadians(u2.getLatitud());
        double lon2 = Math.toRadians(u2.getLongitud());
        
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.pow(Math.sin(dLon / 2), 2);
                   
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return RADIO_TIERRA_KM * c;
    }
    
    /**
     * Verifica si dos ubicaciones están dentro del radio máximo permitido
     * @param u1 Primera ubicación
     * @param u2 Segunda ubicación
     * @return true si la distancia entre las ubicaciones es menor o igual al radio máximo
     */
    public static boolean estanCercanas(Ubicacion u1, Ubicacion u2) {
        if (u1 == null || u2 == null) {
            return false;
        }
        return calcularDistancia(u1, u2) <= RADIO_MAXIMO_KM;
    }
}
