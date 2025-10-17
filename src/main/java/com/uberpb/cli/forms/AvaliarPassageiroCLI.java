package com.uberpb.cli.forms;

import com.uberpb.model.Avaliacao;
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
        
        // Verificar se já foi avaliado
        if (db.corridaJaAvaliadaPor(corrida.getId(), corrida.getMotoristaId(), "MOTORISTA")) {
            System.out.println("Você já avaliou este passageiro para esta corrida.");
            return;
        }
        
        // Solicitar nota
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
        
        // Solicitar comentário (opcional)
        System.out.print("Deixe um comentário (opcional, pressione Enter para pular): ");
        String comentario = sc.nextLine().trim();
        if (comentario.isEmpty()) {
            comentario = null;
        }
        
        // Criar e salvar avaliação no banco de dados
        Avaliacao avaliacao = new Avaliacao(
            corrida.getId(),
            corrida.getMotoristaId(),
            corrida.getPassageiroId(),
            "MOTORISTA",
            "PASSAGEIRO",
            nota,
            comentario
        );
        
        try {
            db.saveAvaliacao(avaliacao);
            
            // Atualizar a avaliação média do passageiro (mantém sistema existente)
            List<Float> avaliacoes = passageiro.getAvaliacoes();
            avaliacoes.add(nota);
            passageiro.setAvaliacoes(avaliacoes);
            db.updatePassageiro(passageiro);
            
            // Marcar corrida como avaliada pelo motorista
            corrida.setAvaliada_motorista(true);
            
            System.out.println("Obrigado por avaliar o passageiro!");
            if (comentario != null && !comentario.isEmpty()) {
                System.out.println("Seu comentário foi registrado com sucesso.");
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao salvar avaliação: " + e.getMessage());
        }
    }
}