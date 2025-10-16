package com.uberpb.cli.forms;

import com.uberpb.model.Corrida;
import com.uberpb.model.Passageiro;
import com.uberpb.repository.DatabaseManager;
import java.util.List;
import java.util.Scanner;

public class AvaliarPassageiroCLI {
    private final Scanner sc;
    private final Corrida corrida;
    private final DatabaseManager db;

    public AvaliarPassageiroCLI(Scanner sc, Corrida corrida, DatabaseManager db) {
        this.sc = sc;
        this.corrida = corrida;
        this.db = db;
    }

    public void exibirMenu() {
        System.out.println("\n=== Avaliação do Passageiro ===");
        Passageiro passageiro = db.findPassageiroById(corrida.getPassageiroId()).orElse(null);
        if (passageiro == null) {
            System.out.println("Passageiro não encontrado.");
            return;
        }
        System.out.println("Passageiro: " + passageiro.getNome());
        System.out.print("Dê uma nota de 1 a 5: ");
        float nota = -1;
        while (nota < 1 || nota > 5) {
            try {
                nota = Float.parseFloat(sc.nextLine());
                if (nota < 1 || nota > 5) {
                    System.out.print("Nota inválida. Digite novamente (1 a 5): ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número de 1 a 5: ");
            }
        }
        List<Float> avaliacoes = passageiro.getAvaliacoes();
        avaliacoes.add(nota);
        passageiro.setAvaliacoes(avaliacoes);
        db.updatePassageiro(passageiro);
        corrida.setAvaliada_motorista(true);
        System.out.println("Obrigado por avaliar o passageiro!");
    }
}