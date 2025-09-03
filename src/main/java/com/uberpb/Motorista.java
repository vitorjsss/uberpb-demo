package com.uberpb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um Motorista no sistema UberPB
 * T1.3 - Implementar classe Motorista com atributos
 */
public class Motorista {
    // Atributos básicos do usuário
    private static int contadorId = 1;
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCadastro;
    private boolean ativo;
    
    // Atributos específicos do motorista
    private String cnh;
    private String validadeCnh;
    private String placaVeiculo;
    private String modeloVeiculo;
    private String corVeiculo;
    private int anoVeiculo;
    private List<String> categorias;
    private double avaliacaoMedia;
    private int totalAvaliacoes;
    private boolean disponivel;
    private String localizacaoAtual;
    
    /**
     * Construtor da classe Motorista
     */
    public Motorista(String nome, String email, String telefone, String cnh, 
                     String validadeCnh, String placaVeiculo, String modeloVeiculo, 
                     String corVeiculo, int anoVeiculo) {
        // Inicializa atributos básicos
        this.id = contadorId++;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
        
        // Inicializa atributos específicos do motorista
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        this.placaVeiculo = placaVeiculo;
        this.modeloVeiculo = modeloVeiculo;
        this.corVeiculo = corVeiculo;
        this.anoVeiculo = anoVeiculo;
        this.categorias = new ArrayList<>();
        this.avaliacaoMedia = 0.0;
        this.totalAvaliacoes = 0;
        this.disponivel = false;
        this.localizacaoAtual = "";
    }
    
    // ===== GETTERS =====
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public boolean isAtivo() { return ativo; }
    
    public String getCnh() { return cnh; }
    public String getValidadeCnh() { return validadeCnh; }
    public String getPlacaVeiculo() { return placaVeiculo; }
    public String getModeloVeiculo() { return modeloVeiculo; }
    public String getCorVeiculo() { return corVeiculo; }
    public int getAnoVeiculo() { return anoVeiculo; }
    public List<String> getCategorias() { return categorias; }
    public double getAvaliacaoMedia() { return avaliacaoMedia; }
    public int getTotalAvaliacoes() { return totalAvaliacoes; }
    public boolean isDisponivel() { return disponivel; }
    public String getLocalizacaoAtual() { return localizacaoAtual; }
    
    // ===== SETTERS =====
    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public void setCnh(String cnh) { this.cnh = cnh; }
    public void setValidadeCnh(String validadeCnh) { this.validadeCnh = validadeCnh; }
    public void setPlacaVeiculo(String placaVeiculo) { this.placaVeiculo = placaVeiculo; }
    public void setModeloVeiculo(String modeloVeiculo) { this.modeloVeiculo = modeloVeiculo; }
    public void setCorVeiculo(String corVeiculo) { this.corVeiculo = corVeiculo; }
    public void setAnoVeiculo(int anoVeiculo) { this.anoVeiculo = anoVeiculo; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }
    public void setAvaliacaoMedia(double avaliacaoMedia) { this.avaliacaoMedia = avaliacaoMedia; }
    public void setTotalAvaliacoes(int totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public void setLocalizacaoAtual(String localizacaoAtual) { this.localizacaoAtual = localizacaoAtual; }
    
    // ===== MÉTODOS ESPECÍFICOS =====
    
    /**
     * Adiciona categoria ao motorista (evita duplicatas)
     */
    public void adicionarCategoria(String categoria) {
        if (!this.categorias.contains(categoria)) {
            this.categorias.add(categoria);
        }
    }
    
    /**
     * Remove categoria do motorista
     */
    public void removerCategoria(String categoria) {
        this.categorias.remove(categoria);
    }
    
    /**
     * Adiciona uma nova avaliação e recalcula a média
     */
    public void adicionarAvaliacao(double novaAvaliacao) {
        double somaTotal = (this.avaliacaoMedia * this.totalAvaliacoes) + novaAvaliacao;
        this.totalAvaliacoes++;
        this.avaliacaoMedia = somaTotal / this.totalAvaliacoes;
    }
    
    /**
     * Verifica se o motorista pode atuar em uma categoria específica
     */
    public boolean podeAtuarCategoria(String categoria) {
        return this.categorias.contains(categoria);
    }
    
    @Override
    public String toString() {
        return "Motorista{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", email='" + email + '\'' +
               ", cnh='" + cnh + '\'' +
               ", placaVeiculo='" + placaVeiculo + '\'' +
               ", modeloVeiculo='" + modeloVeiculo + '\'' +
               ", categorias=" + categorias +
               ", avaliacaoMedia=" + avaliacaoMedia +
               ", disponivel=" + disponivel +
               '}';
    }
}