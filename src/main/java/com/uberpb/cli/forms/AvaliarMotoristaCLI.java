package com.uberpb.cli.forms;

import com.uberpb.model.Avaliacao;
import com.uberpb.model.Corrida;
import com.uberpb.model.Motorista;
import com.uberpb.repository.DatabaseManager;
import java.util.Scanner;

public class AvaliarMotoristaCLI {
    private final Scanner sc;
    private final Corrida corrida;
    private final DatabaseManager db;

    public AvaliarMotoristaCLI(Scanner sc, Corrida corrida, DatabaseManager db) {
        this.sc = sc;
        this.corrida = corrida;
        this.db = db;
    }

    public void exibirMenu() {
        System.out.println("\n=== Avaliação do Motorista ===");
        Motorista motorista = db.findMotoristaById(corrida.getMotoristaId()).orElse(null);
        if (motorista == null) {
            System.out.println("Motorista não encontrado.");
            return;
        }
        
        System.out.println("Motorista: " + motorista.getNome());
        
        // Verificar se já foi avaliado
        if (db.corridaJaAvaliadaPor(corrida.getId(), corrida.getPassageiroId(), "PASSAGEIRO")) {
            System.out.println("Você já avaliou este motorista para esta corrida.");
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
            corrida.getPassageiroId(),
            corrida.getMotoristaId(),
            "PASSAGEIRO",
            "MOTORISTA",
            nota,
            comentario
        );
        
        try {
            db.saveAvaliacao(avaliacao);
            
            // Atualizar a avaliação média do motorista (mantém sistema existente)
            motorista.adicionarAvaliacao(nota);
            db.updateMotorista(motorista);
            
            // Marcar corrida como avaliada pelo passageiro
            corrida.setAvaliada_passageiro(true);
            
            System.out.println("Obrigado por avaliar o motorista!");
            if (comentario != null && !comentario.isEmpty()) {
                System.out.println("Seu comentário foi registrado com sucesso.");
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao salvar avaliação: " + e.getMessage());
        }
    }
}
