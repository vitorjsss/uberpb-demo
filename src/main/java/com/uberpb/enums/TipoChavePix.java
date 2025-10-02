package com.uberpb.enums;

/**
 * Tipos de chave PIX disponíveis.
 */
public enum TipoChavePix {
    CPF("CPF"),
    CNPJ("CNPJ"),
    EMAIL("Email"),
    TELEFONE("Telefone"),
    CHAVE_ALEATORIA("Chave Aleatória");

    private final String descricao;

    TipoChavePix(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}