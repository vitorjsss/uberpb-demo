package com.uberpb.model;

import com.uberpb.enums.StatusPagamento;

import java.time.LocalDateTime;

public class Pagamento {
    private int id;
    private int corridaId;
    private int passageiroId;
    private double valor;
    private StatusPagamento status;
    private String chaveIdempotencia;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Pagamento() {
    }

    // Getters e Setters
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
    }

    public int getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(int passageiroId) {
        this.passageiroId = passageiroId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public String getChaveIdempotencia() {
        return chaveIdempotencia;
    }

    public void setChaveIdempotencia(String chaveIdempotencia) {
        this.chaveIdempotencia = chaveIdempotencia;
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
}
