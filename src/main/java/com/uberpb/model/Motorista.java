package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;

public class Motorista extends User {

    private boolean ativo;
    private String cnh;
    private String validadeCnh;
    private List<Float> avaliacoes;
    private double avaliacaoMedia;
    private int totalAvaliacoes;
    private boolean disponivel;
    private String localizacaoAtual;
    private String categoria;

    public Motorista() {
        super();
        this.ativo = false;
        this.avaliacoes = new ArrayList<>();
        this.avaliacaoMedia = 0.0;
        this.totalAvaliacoes = 0;
        this.disponivel = false;
        this.localizacaoAtual = "Não definida";
        this.categoria = "Não definida";
    }

    public Motorista(int id, boolean ativo, String cnh, String validadeCnh, double avaliacaoMedia, int totalAvaliacoes,
            boolean disponivel, String categoria, String localizacaoAtual) {
        super();
        this.setId(id);
        this.ativo = ativo;
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        this.avaliacoes = new ArrayList<>();
        this.avaliacaoMedia = avaliacaoMedia;
        this.totalAvaliacoes = totalAvaliacoes;
        this.disponivel = disponivel;
        this.localizacaoAtual = (localizacaoAtual != null) ? localizacaoAtual : "Não definida";
        this.categoria = (categoria != null) ? categoria : "Não definida";
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getValidadeCnh() {
        return validadeCnh;
    }

    public void setValidadeCnh(String validadeCnh) {
        this.validadeCnh = validadeCnh;
    }

    public double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public int getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(int totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getLocalizacaoAtual() {
        return localizacaoAtual;
    }

    public void setLocalizacaoAtual(String localizacaoAtual) {
        this.localizacaoAtual = localizacaoAtual;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<Float> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Float> avaliacoes) {
        this.avaliacoes = avaliacoes;
        recalcularAvaliacaoMedia();
    }

    // ===== Novo método principal de avaliação =====
    public void adicionarAvaliacao(float nota) {
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve ser entre 1 e 5.");
        }
        avaliacoes.add(nota);
        recalcularAvaliacaoMedia();
    }

    private void recalcularAvaliacaoMedia() {
        if (avaliacoes.isEmpty()) {
            this.avaliacaoMedia = 0.0;
            this.totalAvaliacoes = 0;
        } else {
            double soma = 0;
            for (float n : avaliacoes)
                soma += n;
            this.totalAvaliacoes = avaliacoes.size();
            this.avaliacaoMedia = soma / this.totalAvaliacoes;
        }
    }

    // Método auxiliar anterior (mantido)
    public void atualizarAvaliacao(double novaNota) {
        double total = this.avaliacaoMedia * this.totalAvaliacoes;
        this.totalAvaliacoes++;
        this.avaliacaoMedia = (total + novaNota) / this.totalAvaliacoes;
        this.avaliacoes.add((float) novaNota);
    }

    @Override
    public String toString() {
        return "Motorista{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", avaliacaoMedia=" + avaliacaoMedia +
                ", totalAvaliacoes=" + totalAvaliacoes +
                ", categoria='" + categoria + '\'' +
                ", localizacaoAtual='" + localizacaoAtual + '\'' +
                ", disponivel=" + disponivel +
                '}';
    }
}
