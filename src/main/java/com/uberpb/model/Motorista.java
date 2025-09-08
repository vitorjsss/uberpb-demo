package com.uberpb.model;

public class Motorista {
    private int id;
    private boolean ativo;

    private String cnh;
    private String validadeCnh;

    private double avaliacaoMedia;
    private int totalAvaliacoes;
    private boolean disponivel;
    private String localizacaoAtual;

    public Motorista(int id, boolean ativo, String cnh, String validadeCnh, double avaliacaoMedia,
            int totalAvaliacoes, boolean disponivel, String localizacaoAtual) {
        this.id = id;
        this.ativo = ativo;
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        this.avaliacaoMedia = avaliacaoMedia;
        this.totalAvaliacoes = totalAvaliacoes;
        this.disponivel = disponivel;
        this.localizacaoAtual = localizacaoAtual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}