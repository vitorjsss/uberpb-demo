package com.uberpb.test;

import com.uberpb.model.Avaliacao;
import com.uberpb.repository.DatabaseManager;

public class TesteAvaliacao {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTE DO SISTEMA DE AVALIAÇÕES ===");
            
            // Criar instância do DatabaseManager
            DatabaseManager db = new DatabaseManager();
            
            // Criar uma avaliação de teste
            Avaliacao avaliacao = new Avaliacao(
                1, // corridaId
                1, // avaliadorId (passageiro)
                2, // avaliadoId (motorista)
                "PASSAGEIRO", // tipoAvaliador
                "MOTORISTA", // tipoAvaliado
                4.5f, // nota
                "Motorista muito educado e dirigiu com segurança!" // comentario
            );
            
            System.out.println("Avaliação criada: " + avaliacao);
            
            // Salvar avaliação
            db.saveAvaliacao(avaliacao);
            System.out.println("Avaliação salva com sucesso!");
            
            // Buscar todas as avaliações
            System.out.println("Número de avaliações no banco: " + db.findAllAvaliacoes().size());
            
            // Buscar avaliações por corrida
            var avaliacoesCorrid1 = db.findAvaliacoesByCorridaId(1);
            System.out.println("Avaliações da corrida 1: " + avaliacoesCorrid1.size());
            
            // Estatísticas
            System.out.println(db.getDatabaseStats());
            
            System.out.println("=== TESTE CONCLUÍDO COM SUCESSO! ===");
            
        } catch (Exception e) {
            System.err.println("Erro durante o teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}