package com.uberpb.model;

public class Motorista extends User {

    private boolean ativo;
    private String cnh;
    private String validadeCnh;
    private double avaliacaoMedia;
    private int totalAvaliacoes;
    private boolean disponivel;
    private String localizacaoAtual;
    private String categoria;

    // Construtor padrão necessário para o Jackson
    public Motorista() {
        super();
        this.ativo = false;
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
        this.avaliacaoMedia = avaliacaoMedia;
        this.totalAvaliacoes = totalAvaliacoes;
        this.disponivel = disponivel;
        this.localizacaoAtual = (localizacaoAtual != null) ? localizacaoAtual : "Não definida";
        this.categoria = (categoria != null) ? categoria : "Não definida";
    }

    // ===== Getters e Setters =====
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

    // ===== Métodos auxiliares =====
    public void atualizarAvaliacao(double novaNota) {
        double total = this.avaliacaoMedia * this.totalAvaliacoes;
        this.totalAvaliacoes++;
        this.avaliacaoMedia = (total + novaNota) / this.totalAvaliacoes;
    }
}