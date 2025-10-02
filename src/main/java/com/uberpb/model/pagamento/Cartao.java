package com.uberpb.model.pagamento;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe simples para cartões (crédito e débito).
 */
public class Cartao {
    private int id;
    private int usuarioId;
    private String numeroCartao;
    private String pin;
    private LocalDate validade;
    private String nomeDono;
    private String cpfDono;
    private String tipo; // "CREDITO" ou "DEBITO"
    private boolean ativo;

    // Construtor vazio
    public Cartao() {
        this.ativo = true;
    }

    // Construtor completo
    public Cartao(int usuarioId, String numeroCartao, String pin, LocalDate validade,
            String nomeDono, String cpfDono, String tipo) {
        this();
        this.usuarioId = usuarioId;
        this.numeroCartao = numeroCartao;
        this.pin = pin;
        this.validade = validade;
        this.nomeDono = nomeDono;
        this.cpfDono = cpfDono;
        this.tipo = tipo.toUpperCase();
    }

    // Validação do cartão
    @JsonIgnore
    public boolean isValido() {
        return numeroCartao != null && !numeroCartao.trim().isEmpty() &&
                pin != null && pin.length() == 4 && pin.matches("\\d{4}") &&
                validade != null && validade.isAfter(LocalDate.now()) &&
                nomeDono != null && !nomeDono.trim().isEmpty() &&
                cpfDono != null && validarCPF(cpfDono) &&
                (tipo.equals("CREDITO") || tipo.equals("DEBITO")) &&
                ativo;
    }

    // Validação simples de CPF (apenas formato)
    private boolean validarCPF(String cpf) {
        return cpf != null && (cpf.matches("\\d{11}") || cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
    }

    // Máscara do cartão para exibição segura
    @JsonIgnore
    public String getNumeroMascarado() {
        if (numeroCartao != null && numeroCartao.length() >= 4) {
            return "**** **** **** " + numeroCartao.substring(numeroCartao.length() - 4);
        }
        return "Cartão inválido";
    }

    // Máscara do CPF
    @JsonIgnore
    public String getCpfMascarado() {
        if (cpfDono != null && cpfDono.length() >= 4) {
            return "***.***.***-" + cpfDono.substring(cpfDono.length() - 2);
        }
        return "CPF inválido";
    }

    // Verificar se cartão está vencido
    @JsonIgnore
    public boolean isVencido() {
        return validade != null && validade.isBefore(LocalDate.now());
    }

    // Formatar validade como MM/YYYY
    @JsonIgnore
    public String getValidadeFormatada() {
        if (validade == null) {
            return "N/A";
        }
        return String.format("%02d/%04d", validade.getMonthValue(), validade.getYear());
    }

    // toString para exibição
    @Override
    public String toString() {
        return String.format("Cartão %s - %s\nTitular: %s\nCPF: %s\nValidade: %s",
                tipo,
                getNumeroMascarado(),
                nomeDono,
                getCpfMascarado(),
                getValidadeFormatada());
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getCpfDono() {
        return cpfDono;
    }

    public void setCpfDono(String cpfDono) {
        this.cpfDono = cpfDono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo != null ? tipo.toUpperCase() : null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}