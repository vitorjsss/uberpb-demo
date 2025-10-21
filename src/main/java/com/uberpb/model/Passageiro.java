package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;

public class Passageiro extends User {

    private double avaliacaoMedia;
    private List<Float> avaliacoes;
    private List<String> historicoCorridas;
    private String localizacaoAtual;
    private boolean emCorrida;
    private int idade;
    private int totalAvaliacoes; // Added to match JSON

    // Construtor padrão necessário para o Jackson
    public Passageiro() {
        super();
        this.avaliacaoMedia = 0.0;
        this.avaliacoes = new ArrayList<>();
        this.historicoCorridas = new ArrayList<>();
        this.localizacaoAtual = "Nao definida";
        this.emCorrida = false;
        this.totalAvaliacoes = 0;
    }

    // Construtor existente
    public Passageiro(int id, String localizacaoAtual, boolean emCorrida) {
        super();
        this.setId(id);
        this.avaliacoes = new ArrayList<>();
        this.avaliacaoMedia = 0.0;
        this.historicoCorridas = new ArrayList<>();
        this.localizacaoAtual = localizacaoAtual;
        this.emCorrida = emCorrida;
        this.totalAvaliacoes = 0;
    }

    // ===== Getters e Setters =====
    public double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public List<String> getHistoricoCorridas() {
        return historicoCorridas;
    }

    public void setHistoricoCorridas(List<String> historicoCorridas) {
        this.historicoCorridas = historicoCorridas;
    }

    public String getLocalizacaoAtual() {
        return localizacaoAtual;
    }

    public void setLocalizacaoAtual(String localizacaoAtual) {
        this.localizacaoAtual = localizacaoAtual;
    }

    public boolean isEmCorrida() {
        return emCorrida;
    }

    public void setEmCorrida(boolean emCorrida) {
        this.emCorrida = emCorrida;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public List<Float> getAvaliacoes() {
        if (avaliacoes == null) {
            avaliacoes = new ArrayList<>();
        }
        return avaliacoes;
    }

    public void setAvaliacoes(List<Float> avaliacoes) {
        this.avaliacoes = avaliacoes;
        if (this.avaliacoes == null) {
            this.avaliacoes = new ArrayList<>();
        }
        this.totalAvaliacoes = this.avaliacoes.size();
        recalcularAvaliacaoMedia();
    }

    private void recalcularAvaliacaoMedia() {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            this.avaliacaoMedia = 0.0;
            return;
        }
        double soma = avaliacoes.stream().mapToDouble(nota -> nota).sum();
        this.avaliacaoMedia = soma / avaliacoes.size();
    }

    public int getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(int totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    @Override
    public String toString() {
        return "Passageiro{" +
                "id=" + getId() +
                ", idade=" + idade +
                ", avaliacaoMedia=" + avaliacaoMedia +
                ", historicoCorridas=" + historicoCorridas +
                ", localizacaoAtual='" + localizacaoAtual + '\'' +
                ", emCorrida=" + emCorrida +
                '}';
    }
}
