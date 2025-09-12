package com.uberpb.helpers;

public class ValidadoresVeiculo {

    public enum TipoVeiculo {
        CARRO, MOTO
    }

    // --- Auxiliar para checar se uma string não é nula ou vazia ---
    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // --- Validadores ---
    public static boolean validarModelo(String modelo) {
        return isNotBlank(modelo);
    }

    public static boolean validarMarca(String marca) {
        return isNotBlank(marca);
    }

    public static boolean validarCor(String cor) {
        return isNotBlank(cor);
    }

    public static boolean validarPlaca(String placa) {
        if (!isNotBlank(placa))
            return false;
        // Regex para placas brasileiras (antigo + Mercosul)
        return placa.matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");
    }

    public static boolean validarTipo(String tipo) {
        if (!isNotBlank(tipo))
            return false;
        try {
            TipoVeiculo.valueOf(tipo.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean validarAno(int ano) {
        int anoAtual = java.time.LocalDate.now().getYear();
        return ano >= 1900 && ano <= anoAtual;
    }

    public static boolean validarCapacidadePortaMalas(double capacidade) {
        return capacidade >= 0 && capacidade <= 1000;
    }

    public static boolean validarNumeroPassageiros(int num) {
        return num > 0 && num <= 10;
    }
}
