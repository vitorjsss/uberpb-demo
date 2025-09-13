package com.uberpb.helpers;

/**
 * Classe utilitária com métodos de validação para cadastro de veículos.
 */
public class ValidadoresVeiculo {

    // --- Tipos de veículo suportados ---
    public enum TipoVeiculo {
        CARRO, MOTO
    }

    // --- Auxiliar: verifica se uma string não é nula nem vazia ---
    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // --- Validações principais ---

    /**
     * Valida o modelo do veículo.
     * @param modelo Nome do modelo.
     * @return true se não for nulo ou vazio.
     */
    public static boolean validarModelo(String modelo) {
        return isNotBlank(modelo);
    }

    /**
     * Valida a marca do veículo.
     * @param marca Nome da marca.
     * @return true se não for nulo ou vazio.
     */
    public static boolean validarMarca(String marca) {
        return isNotBlank(marca);
    }

    /**
     * Valida a cor do veículo.
     * @param cor Cor informada.
     * @return true se não for nulo ou vazio.
     */
    public static boolean validarCor(String cor) {
        return isNotBlank(cor);
    }

    /**
     * Valida a placa do veículo.
     * Suporta placas brasileiras antigas e Mercosul.
     * @param placa Placa informada.
     * @return true se estiver no formato correto.
     */
    public static boolean validarPlaca(String placa) {
        if (!isNotBlank(placa))
            return false;
        return placa.matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");
    }

    /**
     * Valida o tipo de veículo (CARRO ou MOTO).
     * @param tipo Tipo informado.
     * @return true se for um tipo válido.
     */
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

    /**
     * Valida o ano do veículo.
     * @param ano Ano informado.
     * @return true se estiver entre 1900 e o ano atual.
     */
    public static boolean validarAno(int ano) {
        int anoAtual = java.time.LocalDate.now().getYear();
        return ano >= 1900 && ano <= anoAtual;
    }

    /**
     * Valida a capacidade do porta-malas (em litros).
     * @param capacidade Capacidade informada.
     * @return true se estiver entre 0 e 1000 litros.
     */
    public static boolean validarCapacidadePortaMalas(double capacidade) {
        return capacidade >= 0 && capacidade <= 1000;
    }

    /**
     * Valida o número de passageiros do veículo.
     * @param num Número de passageiros.
     * @return true se estiver entre 1 e 10.
     */
    public static boolean validarNumeroPassageiros(int num) {
        return num > 0 && num <= 10;
    }
}