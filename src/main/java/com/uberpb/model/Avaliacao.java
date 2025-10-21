package com.uberpb.model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma avaliação no sistema UberPB
 * Armazena a nota, comentário e informações sobre quem avaliou quem
 */
public class Avaliacao {

    private int id;
    private int corridaId; // ID da corrida que está sendo avaliada
    private int avaliadorId; // ID do usuário que está fazendo a avaliação (passageiro ou motorista)
    private int avaliadoId; // ID do usuário que está sendo avaliado (motorista ou passageiro)
    private String tipoAvaliador; // "PASSAGEIRO" ou "MOTORISTA"
    private String tipoAvaliado; // "MOTORISTA" ou "PASSAGEIRO"
    private float nota; // Nota de 1 a 5
    private String comentario; // Comentário opcional
    
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private boolean valid;

    // ===== Construtores =====
        public Avaliacao() {
            this.criadoEm = LocalDateTime.now();
            this.atualizadoEm = LocalDateTime.now();
            this.valid = false;
        }

    public Avaliacao(int corridaId, int avaliadorId, int avaliadoId, 
                    String tipoAvaliador, String tipoAvaliado, 
                    float nota, String comentario) {
        this();
        this.corridaId = corridaId;
        this.avaliadorId = avaliadorId;
        this.avaliadoId = avaliadoId;
        this.tipoAvaliador = tipoAvaliador;
        this.tipoAvaliado = tipoAvaliado;
        this.nota = nota;
        this.comentario = comentario;
    }

    // ===== Getters e Setters =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorridaId() {
        return corridaId;
    }

    public void setCorridaId(int corridaId) {
        this.corridaId = corridaId;
        this.atualizadoEm = LocalDateTime.now();
    }

    public int getAvaliadorId() {
        return avaliadorId;
    }

    public void setAvaliadorId(int avaliadorId) {
        this.avaliadorId = avaliadorId;
        this.atualizadoEm = LocalDateTime.now();
    }

    public int getAvaliadoId() {
        return avaliadoId;
    }

    public void setAvaliadoId(int avaliadoId) {
        this.avaliadoId = avaliadoId;
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getTipoAvaliador() {
        return tipoAvaliador;
    }

    public void setTipoAvaliador(String tipoAvaliador) {
        this.tipoAvaliador = tipoAvaliador;
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getTipoAvaliado() {
        return tipoAvaliado;
    }

    public void setTipoAvaliado(String tipoAvaliado) {
        this.tipoAvaliado = tipoAvaliado;
        this.atualizadoEm = LocalDateTime.now();
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5");
        }
        this.nota = nota;
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
        this.atualizadoEm = LocalDateTime.now();
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    // ===== Métodos de validação =====
    public boolean isValid() {
        this.valid = corridaId > 0 && 
               avaliadorId > 0 && 
               avaliadoId > 0 && 
               nota >= 1 && nota <= 5 &&
               tipoAvaliador != null && !tipoAvaliador.trim().isEmpty() &&
               tipoAvaliado != null && !tipoAvaliado.trim().isEmpty();
        return this.valid;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", corridaId=" + corridaId +
                ", avaliadorId=" + avaliadorId +
                ", avaliadoId=" + avaliadoId +
                ", tipoAvaliador='" + tipoAvaliador + '\'' +
                ", tipoAvaliado='" + tipoAvaliado + '\'' +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }

    // ===== equals e hashCode =====
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Avaliacao avaliacao = (Avaliacao) obj;
        return id == avaliacao.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}