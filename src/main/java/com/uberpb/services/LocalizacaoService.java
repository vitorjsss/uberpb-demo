package com.uberpb.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LocalizacaoService {
    private Map<String, Map<String, Object>> localizacoes;
    private final ObjectMapper objectMapper;

    public LocalizacaoService() {
        this.objectMapper = new ObjectMapper();
        carregarLocalizacoes();
    }

    private void carregarLocalizacoes() {
        try {
            String content = Files.readString(Paths.get("database/localizacoes.json"));
            this.localizacoes = objectMapper.readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
        } catch (IOException e) {
            System.err.println("Erro ao carregar localizações: " + e.getMessage());
            this.localizacoes = new HashMap<>();
        }
    }

    public String getNome(String nome) {
        return localizacoes.containsKey(nome) ? nome : "Localização não encontrada";
    }

    public int getCoordenada(String nome) {
        Map<String, Object> loc = localizacoes.get(nome);
        return loc != null ? (Integer) loc.get("coordenada") : 0;
    }

    public int calcularDistancia(String nomeOrigem, String nomeDestino) {
        int coordOrigem = getCoordenada(nomeOrigem);
        int coordDestino = getCoordenada(nomeDestino);
        return Math.abs(coordDestino - coordOrigem);
    }

    public boolean isLocalizacaoValida(String nome) {
        return localizacoes.containsKey(nome);
    }

    public void exibirLocalizacoes() {
        System.out.println("\n=== Recomendações ===");
        for (Map.Entry<String, Map<String, Object>> entry : localizacoes.entrySet()) {
            String nome = entry.getKey();
            System.out.println(nome);
        }
        System.out.println();
    }

    public String getLocalizacaoAleatoria() {
        if (localizacoes.isEmpty()) {
            return null;
        }

        // Converter as chaves (nomes das localizações) para array
        String[] nomes = localizacoes.keySet().toArray(new String[0]);

        // Gerar índice aleatório
        int indiceAleatorio = (int) (Math.random() * nomes.length);

        return nomes[indiceAleatorio];
    }
}