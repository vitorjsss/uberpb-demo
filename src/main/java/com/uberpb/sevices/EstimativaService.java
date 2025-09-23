package com.uberpb.sevices;

import com.uberpb.model.Categoria;

public class EstimativaService {

    private static final double PRECO_BASE = 5.0;
    private static final double PRECO_BASE_POR_KM = 2.0;

    public double estimarPreco(double distanciaKm, String categoriaNome) {
        Categoria categoria = Categoria.buscarPorNome(categoriaNome);
        if (categoria == null) {
            // Fallback para categorias não encontradas
            categoria = Categoria.valueOf(categoriaNome.toUpperCase());
        }
        return estimarPreco(distanciaKm, categoria);
    }

    public double estimarPreco(double distanciaKm, Categoria categoria) {
        double precoBase = PRECO_BASE + (PRECO_BASE_POR_KM * distanciaKm);
        return categoria.calcularPreco(precoBase);
    }

    public int estimarTempoMinutos(double distanciaKm) {
        // Exemplo de stub: tempo fixo por km
        int tempoPorKm = 3;
        return (int) Math.ceil(distanciaKm * tempoPorKm);
    }
}