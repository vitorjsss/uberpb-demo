package com.uberpb.sevices;

import com.uberpb.model.Categoria;
import com.uberpb.services.LocalizacaoService;

public class EstimativaService {

    private static final double PRECO_BASE = 5.0;
    private static final double PRECO_BASE_POR_KM = 2.0;
    private final LocalizacaoService localizacaoService;

    public EstimativaService() {
        this.localizacaoService = new LocalizacaoService();
    }

    // Método principal para estimar preço - faz tudo em uma função
    public double estimarPreco(String nomeOrigem, String nomeDestino, String categoriaNome) {
        // 1. Calcular distância entre localizações
        int distancia = localizacaoService.calcularDistancia(nomeOrigem, nomeDestino);
        double distanciaKm = distancia / 10.0; // Convertendo unidades para km (10 unidades = 1 km)

        // 2. Buscar categoria
        Categoria categoria = Categoria.buscarPorNome(categoriaNome);
        if (categoria == null) {
            // Fallback para categorias não encontradas
            categoria = Categoria.valueOf(categoriaNome.toUpperCase());
        }

        // 3. Calcular preço base
        double precoBase = PRECO_BASE + (PRECO_BASE_POR_KM * distanciaKm);

        // 4. Aplicar multiplicador da categoria e retornar
        return categoria.calcularPreco(precoBase);
    }

    // Método para estimar tempo
    public int estimarTempoMinutos(String nomeOrigem, String nomeDestino) {
        int distancia = localizacaoService.calcularDistancia(nomeOrigem, nomeDestino);
        double distanciaKm = distancia / 10.0; // Convertendo unidades para km
        int tempoPorKm = 3; // tempo fixo por km
        return (int) Math.ceil(distanciaKm * tempoPorKm);
    }

    // Métodos de conveniência para acessar localizações
    public void exibirLocalizacoes() {
        localizacaoService.exibirLocalizacoes();
    }

    public boolean isLocalizacaoValida(String nome) {
        return localizacaoService.isLocalizacaoValida(nome);
    }

    public String getNomeLocalizacao(String nome) {
        return localizacaoService.getNome(nome);
    }
}