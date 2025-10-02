package com.uberpb.model.pagamento;

import com.uberpb.enums.StatusPagamento;
import com.uberpb.enums.MetodoPagamento;

import java.time.LocalDateTime;

public class Pagamento {
    private int id;
    private int corridaId;
    private int passageiroId;
    private double valor;
    private StatusPagamento status;
    private MetodoPagamento tipoPagamento;
    private int metodoPagamentoId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Pagamento() {
    }

    public Pagamento(int corridaId, int passageiroId, double valor,
            MetodoPagamento tipoPagamento, int metodoPagamentoId) {
        this.corridaId = corridaId;
        this.passageiroId = passageiroId;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.metodoPagamentoId = metodoPagamentoId;
        this.status = StatusPagamento.PENDENTE;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
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

    public MetodoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(MetodoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public int getMetodoPagamentoId() {
        return metodoPagamentoId;
    }

    public void setMetodoPagamentoId(int metodoPagamentoId) {
        this.metodoPagamentoId = metodoPagamentoId;
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

    @Override
    public String toString() {
        return String.format(
                "Pagamento ID: %d\nCorrida: %d\nValor: R$ %.2f\nMétodo: %s (ID: %d)\nStatus: %s\nCriado em: %s",
                id, corridaId, valor, tipoPagamento, metodoPagamentoId, status,
                criadoEm != null ? criadoEm.toString() : "N/A");
    }
}
