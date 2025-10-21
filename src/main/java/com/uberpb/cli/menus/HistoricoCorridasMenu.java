package com.uberpb.cli.menus;

import com.uberpb.model.HistoricoItem;
import com.uberpb.services.HistoricoService;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.session.SessionManager;

import java.util.List;

/**
 * Menu para exibir histórico completo de corridas
 * Integra dados de corridas, pagamentos, usuários e veículos
 */
public class HistoricoCorridasMenu {
    private final HistoricoService historicoService;

    public HistoricoCorridasMenu() {
        this.historicoService = new HistoricoService(new DatabaseManager());
    }

    public void mostrarHistorico() {
        System.out.println("\n=== Histórico Completo de Corridas ===\n");
        
        // Obter usuário atual da sessão
        com.uberpb.model.User currentUser = SessionManager.getCurrentUser();
        
        if (currentUser == null) {
            System.out.println("Nenhuma sessão ativa encontrada.");
            return;
        }
        
        // Determinar se é passageiro ou motorista e exibir histórico apropriado
        int userId = currentUser.getId();
        
        // Tentar primeiro como passageiro
        List<HistoricoItem> historico = historicoService.gerarHistoricoPassageiro(userId);
        
        // Se não encontrar como passageiro, tentar como motorista
        if (historico.isEmpty()) {
            historico = historicoService.gerarHistoricoMotorista(userId);
        }
        
        if (historico.isEmpty()) {
            System.out.println("Nenhuma corrida encontrada no histórico.");
            return;
        }

        // Exibir histórico completo
        for (HistoricoItem item : historico) {
            System.out.println(item.formatarParaExibicao());
            System.out.println(); // Linha em branco entre itens
        }
        
        // Estatísticas resumidas
        double valorTotal = historico.stream()
            .mapToDouble(HistoricoItem::getValorFinal)
            .sum();
        long corridasAvaliadas = historico.stream()
            .mapToLong(item -> item.isCorridaAvaliada() ? 1 : 0)
            .sum();
        
        System.out.println("========================================");
        System.out.println("Total de corridas: " + historico.size());
        System.out.println("Valor total: R$ " + String.format("%.2f", valorTotal));
        System.out.println("Corridas avaliadas: " + corridasAvaliadas + "/" + historico.size());
        System.out.println("========================================");
    }
}