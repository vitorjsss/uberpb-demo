package com.uberpb.sevices;

import com.uberpb.model.Categoria;

public class EstimativaService {
    public double estimarPreco(double distanciaKm, String categoria) {
        double precoBase = 5.0;
        double precoPorKm = 2.0;
        switch (categoria.toUpperCase()) {
            case "UBER_X":
                precoPorKm = 2.0;
                break;
            case "COMFORT":
                precoPorKm = 3.0;
                break;
            case "BLACK":
                precoPorKm = 4.0;
                break;
            case "XL":
                precoPorKm = 3.5;
                break;
            case "BAG":
                precoPorKm = 2.5;
                break;
            default:
                throw new IllegalArgumentException("Categoria desconhecida: " + categoria);
        }
        return precoBase + (precoPorKm * distanciaKm);
    }

    public int estimarTempoMinutos(double distanciaKm) {
        // Exemplo de stub: tempo fixo por km
        int tempoPorKm = 3;
        return (int) Math.ceil(distanciaKm * tempoPorKm);
    }
}