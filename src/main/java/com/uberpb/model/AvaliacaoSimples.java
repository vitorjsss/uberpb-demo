package com.uberpb.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Versão simplificada da classe Avaliacao para teste sem dependências Jackson
 */
public class AvaliacaoSimples {

    private int id;
    private int corridaId;
    private int avaliadorId;
    private int avaliadoId;
    private String tipoAvaliador;
    private String tipoAvaliado;
    private float nota;
    private String comentario;
    private String criadoEm;
    private String atualizadoEm;

    public AvaliacaoSimples() {
        this.criadoEm = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.atualizadoEm = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public AvaliacaoSimples(int corridaId, int avaliadorId, int avaliadoId, 
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

    // Getters e Setters básicos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCorridaId() { return corridaId; }
    public void setCorridaId(int corridaId) { this.corridaId = corridaId; }
    
    public int getAvaliadorId() { return avaliadorId; }
    public void setAvaliadorId(int avaliadorId) { this.avaliadorId = avaliadorId; }
    
    public int getAvaliadoId() { return avaliadoId; }
    public void setAvaliadoId(int avaliadoId) { this.avaliadoId = avaliadoId; }
    
    public String getTipoAvaliador() { return tipoAvaliador; }
    public void setTipoAvaliador(String tipoAvaliador) { this.tipoAvaliador = tipoAvaliador; }
    
    public String getTipoAvaliado() { return tipoAvaliado; }
    public void setTipoAvaliado(String tipoAvaliado) { this.tipoAvaliado = tipoAvaliado; }
    
    public float getNota() { return nota; }
    public void setNota(float nota) { 
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5");
        }
        this.nota = nota; 
    }
    
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    
    public String getCriadoEm() { return criadoEm; }
    public void setCriadoEm(String criadoEm) { this.criadoEm = criadoEm; }
    
    public String getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(String atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    public String toJson() {
        return String.format(
            "{\n" +
            "  \"id\": %d,\n" +
            "  \"corridaId\": %d,\n" +
            "  \"avaliadorId\": %d,\n" +
            "  \"avaliadoId\": %d,\n" +
            "  \"tipoAvaliador\": \"%s\",\n" +
            "  \"tipoAvaliado\": \"%s\",\n" +
            "  \"nota\": %.1f,\n" +
            "  \"comentario\": \"%s\",\n" +
            "  \"criadoEm\": \"%s\",\n" +
            "  \"atualizadoEm\": \"%s\"\n" +
            "}",
            id, corridaId, avaliadorId, avaliadoId, 
            tipoAvaliador, tipoAvaliado, nota, 
            comentario != null ? comentario : "", 
            criadoEm, atualizadoEm
        );
    }

    @Override
    public String toString() {
        return "AvaliacaoSimples{" +
                "id=" + id +
                ", corridaId=" + corridaId +
                ", avaliadorId=" + avaliadorId +
                ", avaliadoId=" + avaliadoId +
                ", tipoAvaliador='" + tipoAvaliador + '\'' +
                ", tipoAvaliado='" + tipoAvaliado + '\'' +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", criadoEm='" + criadoEm + '\'' +
                '}';
    }
}