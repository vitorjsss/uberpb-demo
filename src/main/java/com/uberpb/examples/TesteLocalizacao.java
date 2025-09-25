package com.uberpb.examples;

import com.uberpb.sevices.EstimativaService;

public class TesteLocalizacao {
    public static void main(String[] args) {
        EstimativaService estimativa = new EstimativaService();

        // Exibir todas as localizações
        estimativa.exibirLocalizacoes();

        // Teste de corrida do Aeroporto para Hospital
        System.out.println("=== Corrida: Aeroporto → Hospital ===");
        System.out.println("Origem: " + estimativa.getNomeLocalizacao("Aeroporto"));
        System.out.println("Destino: " + estimativa.getNomeLocalizacao("Hospital"));

        double precoEconomico = estimativa.estimarPreco("Aeroporto", "Hospital", "UBER_X");
        double precoComfort = estimativa.estimarPreco("Aeroporto", "Hospital", "COMFORT");
        double precoBlack = estimativa.estimarPreco("Aeroporto", "Hospital", "BLACK");
        int tempo = estimativa.estimarTempoMinutos("Aeroporto", "Hospital");

        System.out.printf("UberX: R$ %.2f%n", precoEconomico);
        System.out.printf("Comfort: R$ %.2f%n", precoComfort);
        System.out.printf("Black: R$ %.2f%n", precoBlack);
        System.out.printf("Tempo estimado: %d minutos%n", tempo);

        System.out.println();

        // Teste de corrida do Centro para Praia
        System.out.println("=== Corrida: Centro → Praia ===");
        double precoEconomico2 = estimativa.estimarPreco("Centro", "Praia", "UBER_X");
        double precoComfort2 = estimativa.estimarPreco("Centro", "Praia", "COMFORT");
        double precoBlack2 = estimativa.estimarPreco("Centro", "Praia", "BLACK");
        int tempo2 = estimativa.estimarTempoMinutos("Centro", "Praia");

        System.out.printf("UberX: R$ %.2f%n", precoEconomico2);
        System.out.printf("Comfort: R$ %.2f%n", precoComfort2);
        System.out.printf("Black: R$ %.2f%n", precoBlack2);
        System.out.printf("Tempo estimado: %d minutos%n", tempo2);
    }
}