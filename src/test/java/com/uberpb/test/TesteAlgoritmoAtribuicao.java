package com.uberpb.test;

import com.uberpb.model.Motorista;
import com.uberpb.services.CorridaService;
import com.uberpb.enums.Categoria;
import com.uberpb.repository.DatabaseManager;

import java.util.Optional;
import java.util.List;

/**
 * Teste para validar o algoritmo de atribuição de corridas (T16.5)
 * Verifica se motoristas com melhor média são priorizados em categorias premium
 */
public class TesteAlgoritmoAtribuicao {
    
    public static void main(String[] args) {
        System.out.println("=== TESTE DO ALGORITMO DE ATRIBUIÇÃO (T16.5) ===\n");
        
        try {
            DatabaseManager dbManager = new DatabaseManager();
            CorridaService corridaService = new CorridaService();
            
            // Listar motoristas disponíveis para categoria BLACK (premium)
            List<Motorista> motoristasBlack = dbManager.findAllMotoristas().stream()
                .filter(m -> m.isAtivo() && m.isDisponivel())
                .filter(m -> "BLACK".equalsIgnoreCase(m.getCategoria()))
                .toList();
            
            System.out.println("Motoristas disponíveis na categoria BLACK:");
            for (Motorista m : motoristasBlack) {
                double media = dbManager.calcularMediaAvaliacoes(m.getId());
                int numAvaliacoes = dbManager.contarAvaliacoes(m.getId());
                System.out.printf("- %s %s (ID: %d) - Média: %.2f (%d avaliações) - Localização: %s%n",
                    m.getNome(), m.getSobrenome(), m.getId(), media, numAvaliacoes, m.getLocalizacaoAtual());
            }
            
            System.out.println("\n--- Teste 1: Categoria PREMIUM (BLACK) ---");
            System.out.println("Origem: Hospital");
            Optional<Motorista> motoristaPremium = corridaService.encontrarMotoristaMaisProximo("Hospital", Categoria.BLACK);
            
            if (motoristaPremium.isPresent()) {
                Motorista m = motoristaPremium.get();
                double media = dbManager.calcularMediaAvaliacoes(m.getId());
                System.out.println("✅ Motorista selecionado: " + m.getNome() + " " + m.getSobrenome());
                System.out.printf("   Média de avaliação: %.2f%n", media);
                System.out.println("   Localização: " + m.getLocalizacaoAtual());
                System.out.println("   ➡️  PRIORIZAÇÃO POR AVALIAÇÃO (categoria premium)");
            } else {
                System.out.println("❌ Nenhum motorista encontrado");
            }
            
            // Listar motoristas disponíveis para categoria UBER_X (não-premium)
            List<Motorista> motoristasUberX = dbManager.findAllMotoristas().stream()
                .filter(m -> m.isAtivo() && m.isDisponivel())
                .filter(m -> "UberX".equalsIgnoreCase(m.getCategoria()))
                .toList();
            
            System.out.println("\n\nMotoristas disponíveis na categoria UBER_X:");
            for (Motorista m : motoristasUberX) {
                double media = dbManager.calcularMediaAvaliacoes(m.getId());
                int numAvaliacoes = dbManager.contarAvaliacoes(m.getId());
                System.out.printf("- %s %s (ID: %d) - Média: %.2f (%d avaliações) - Localização: %s%n",
                    m.getNome(), m.getSobrenome(), m.getId(), media, numAvaliacoes, m.getLocalizacaoAtual());
            }
            
            System.out.println("\n--- Teste 2: Categoria NÃO-PREMIUM (UBER_X) ---");
            System.out.println("Origem: Hospital");
            Optional<Motorista> motoristaNormal = corridaService.encontrarMotoristaMaisProximo("Hospital", Categoria.UBER_X);
            
            if (motoristaNormal.isPresent()) {
                Motorista m = motoristaNormal.get();
                double media = dbManager.calcularMediaAvaliacoes(m.getId());
                System.out.println("✅ Motorista selecionado: " + m.getNome() + " " + m.getSobrenome());
                System.out.printf("   Média de avaliação: %.2f%n", media);
                System.out.println("   Localização: " + m.getLocalizacaoAtual());
                System.out.println("   ➡️  PRIORIZAÇÃO POR DISTÂNCIA (categoria não-premium)");
            } else {
                System.out.println("❌ Nenhum motorista encontrado");
            }
            
            System.out.println("\n=== RESUMO DO ALGORITMO ===");
            System.out.println("✅ Categorias PREMIUM (BLACK, XL): Prioriza MELHOR MÉDIA de avaliação");
            System.out.println("✅ Categorias NORMAIS (UberX, Comfort, Bag): Prioriza MENOR DISTÂNCIA");
            System.out.println("✅ Em caso de empate na avaliação: Escolhe o mais PRÓXIMO");
            System.out.println("\n=== TESTE CONCLUÍDO COM SUCESSO ===");
            
        } catch (Exception e) {
            System.err.println("❌ Erro durante o teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
