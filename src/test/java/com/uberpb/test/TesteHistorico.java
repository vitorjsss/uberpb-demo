package com.uberpb.test;

import com.uberpb.services.HistoricoService;
import com.uberpb.model.HistoricoItem;
import java.util.List;

/**
 * Classe simples para testar o HistoricoService
 */
public class TesteHistorico {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTE DO HISTÓRICO ===");
            
            // Instanciar o DatabaseManager e o HistoricoService
            com.uberpb.repository.DatabaseManager databaseManager = new com.uberpb.repository.DatabaseManager();
            HistoricoService historicoService = new HistoricoService(databaseManager);
            
            // Teste com passageiro ID 1 (se existir)
            System.out.println("Gerando histórico para passageiro ID 1...");
            List<HistoricoItem> historicoPassageiro = historicoService.gerarHistoricoPassageiro(1);
            
            System.out.println("Total de itens no histórico: " + historicoPassageiro.size());
            
            if (!historicoPassageiro.isEmpty()) {
                System.out.println("\nPrimeiro item do histórico:");
                HistoricoItem primeiro = historicoPassageiro.get(0);
                System.out.println(primeiro.formatarParaExibicao());
            }
            
            // Teste com motorista ID 1 (se existir)
            System.out.println("\n\nGerando histórico para motorista ID 1...");
            List<HistoricoItem> historicoMotorista = historicoService.gerarHistoricoMotorista(1);
            
            System.out.println("Total de itens no histórico: " + historicoMotorista.size());
            
            if (!historicoMotorista.isEmpty()) {
                System.out.println("\nPrimeiro item do histórico:");
                HistoricoItem primeiro = historicoMotorista.get(0);
                System.out.println(primeiro.formatarParaExibicao());
            }
            
            System.out.println("\n=== TESTE CONCLUÍDO COM SUCESSO ===");
            
        } catch (Exception e) {
            System.err.println("Erro durante o teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}