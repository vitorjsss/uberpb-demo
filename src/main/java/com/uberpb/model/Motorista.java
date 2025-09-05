package com.uberpb.model;

public class Motorista extends User {

    private boolean ativo;
    private String cnh;
    private String validadeCnh;
    private double avaliacaoMedia;
    private int totalAvaliacoes;
    private boolean disponivel;
    private String localizacaoAtual;

    // Construtor: mantém os atributos atuais, chama super() para dados do Usuario
    public Motorista(int id, boolean ativo, String cnh, String validadeCnh, double avaliacaoMedia,
                     int totalAvaliacoes, boolean disponivel, String localizacaoAtual) {
        super(); // chama construtor padrão de Usuario
        this.setId(id); // id herdado de Usuario
        this.ativo = ativo;
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        this.avaliacaoMedia = avaliacaoMedia;
        this.totalAvaliacoes = totalAvaliacoes;
        this.disponivel = disponivel;
        this.localizacaoAtual = localizacaoAtual;
    }

    // Getters e Setters

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getCnh() { return cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }

    public String getValidadeCnh() { return validadeCnh; }
    public void setValidadeCnh(String validadeCnh) { this.validadeCnh = validadeCnh; }

    public double getAvaliacaoMedia() { return avaliacaoMedia; }
    public void setAvaliacaoMedia(double avaliacaoMedia) { this.avaliacaoMedia = avaliacaoMedia; }

    public int getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(int totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public String getLocalizacaoAtual() { return localizacaoAtual; }
    public void setLocalizacaoAtual(String localizacaoAtual) { this.localizacaoAtual = localizacaoAtual; }

    @Override
    public String toString() {
        return "Motorista{" +
                "id=" + getId() +
                ", ativo=" + ativo +
                ", cnh='" + cnh + '\'' +
                ", validadeCnh='" + validadeCnh + '\'' +
                ", avaliacaoMedia=" + avaliacaoMedia +
                ", totalAvaliacoes=" + totalAvaliacoes +
                ", disponivel=" + disponivel +
                ", localizacaoAtual='" + localizacaoAtual + '\'' +
                '}';
    }
}
