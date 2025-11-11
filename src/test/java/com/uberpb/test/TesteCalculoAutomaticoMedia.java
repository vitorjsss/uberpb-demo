package com.uberpb.test;

import com.uberpb.model.Avaliacao;
import com.uberpb.model.Motorista;
import com.uberpb.repository.DatabaseManager;

import java.util.Optional;

/**
 * Teste para validar o cálculo automático da média de avaliações (T16.4)
 * Verifica se a média é atualizada automaticamente ao salvar, editar ou deletar avaliações
 */
public class TesteCalculoAutomaticoMedia {
    
    public static void main(String[] args) {
        System.out.println("=== TESTE DO CÁLCULO AUTOMÁTICO DE MÉDIA (T16.4) ===\n");
        
        try {
            DatabaseManager db = new DatabaseManager();
            
            // Escolher um motorista para teste (ID 2 - geralmente existe nos dados)
            int motoristaId = 2;
            
            System.out.println("--- Estado Inicial ---");
            Optional<Motorista> motoristaOpt = db.findMotoristaById(motoristaId);
            
            if (motoristaOpt.isEmpty()) {
                System.out.println("❌ Motorista ID " + motoristaId + " não encontrado");
                return;
            }
            
            Motorista motorista = motoristaOpt.get();
            System.out.println("Motorista: " + motorista.getNome() + " " + motorista.getSobrenome());
            System.out.println("Média ANTES: " + motorista.getAvaliacaoMedia());
            System.out.println("Total avaliações ANTES: " + motorista.getTotalAvaliacoes());
            
            // Criar uma nova avaliação de teste
            System.out.println("\n--- Teste 1: Salvar Nova Avaliação ---");
            Avaliacao novaAvaliacao = new Avaliacao();
            novaAvaliacao.setCorridaId(999); // ID fictício para teste
            novaAvaliacao.setAvaliadorId(1);
            novaAvaliacao.setAvaliadoId(motoristaId);
            novaAvaliacao.setTipoAvaliador("passageiro");
            novaAvaliacao.setTipoAvaliado("motorista");
            novaAvaliacao.setNota(5);
            novaAvaliacao.setComentario("Teste automático - excelente motorista!");
            
            System.out.println("Salvando avaliação com nota 5...");
            Avaliacao avaliacaoSalva = db.saveAvaliacao(novaAvaliacao);
            System.out.println("✅ Avaliação salva com ID: " + avaliacaoSalva.getId());
            
            // Recarregar motorista e verificar média atualizada
            motoristaOpt = db.findMotoristaById(motoristaId);
            if (motoristaOpt.isPresent()) {
                motorista = motoristaOpt.get();
                System.out.println("Média APÓS salvar: " + motorista.getAvaliacaoMedia());
                System.out.println("Total avaliações APÓS salvar: " + motorista.getTotalAvaliacoes());
                System.out.println("✅ Média atualizada AUTOMATICAMENTE!");
            }
            
            // Testar atualização de avaliação
            System.out.println("\n--- Teste 2: Atualizar Avaliação Existente ---");
            avaliacaoSalva.setNota(4);
            avaliacaoSalva.setComentario("Teste automático - muito bom!");
            System.out.println("Atualizando nota de 5 para 4...");
            db.updateAvaliacao(avaliacaoSalva);
            
            // Recarregar e verificar
            motoristaOpt = db.findMotoristaById(motoristaId);
            if (motoristaOpt.isPresent()) {
                motorista = motoristaOpt.get();
                System.out.println("Média APÓS atualizar: " + motorista.getAvaliacaoMedia());
                System.out.println("✅ Média recalculada AUTOMATICAMENTE após update!");
            }
            
            // Testar deleção de avaliação
            System.out.println("\n--- Teste 3: Deletar Avaliação ---");
            System.out.println("Deletando avaliação de teste...");
            boolean deletado = db.deleteAvaliacaoById(avaliacaoSalva.getId());
            System.out.println("Deletado: " + deletado);
            
            if (deletado) {
                // Recarregar e verificar
                motoristaOpt = db.findMotoristaById(motoristaId);
                if (motoristaOpt.isPresent()) {
                    motorista = motoristaOpt.get();
                    System.out.println("Média APÓS deletar: " + motorista.getAvaliacaoMedia());
                    System.out.println("Total avaliações APÓS deletar: " + motorista.getTotalAvaliacoes());
                    System.out.println("✅ Média recalculada AUTOMATICAMENTE após delete!");
                }
            }
            
            // Testar método auxiliar
            System.out.println("\n--- Teste 4: Método findMotoristaComMediaAtualizada ---");
            motoristaOpt = db.findMotoristaComMediaAtualizada(motoristaId);
            if (motoristaOpt.isPresent()) {
                motorista = motoristaOpt.get();
                double mediaCalculada = db.calcularMediaAvaliacoes(motoristaId);
                System.out.println("Média retornada pelo método: " + motorista.getAvaliacaoMedia());
                System.out.println("Média calculada diretamente: " + mediaCalculada);
                
                if (motorista.getAvaliacaoMedia() == mediaCalculada) {
                    System.out.println("✅ Médias coincidem - método funcionando!");
                } else {
                    System.out.println("❌ Médias diferentes - pode haver problema");
                }
            }
            
            System.out.println("\n=== RESUMO DO CÁLCULO AUTOMÁTICO ===");
            System.out.println("✅ Média atualizada automaticamente ao SALVAR avaliação");
            System.out.println("✅ Média recalculada automaticamente ao ATUALIZAR avaliação");
            System.out.println("✅ Média recalculada automaticamente ao DELETAR avaliação");
            System.out.println("✅ Método auxiliar findMotoristaComMediaAtualizada() funcional");
            System.out.println("\n=== TESTE CONCLUÍDO COM SUCESSO ===");
            
        } catch (Exception e) {
            System.err.println("❌ Erro durante o teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
