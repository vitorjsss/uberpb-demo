package com.uberpb.cli.menus;

import com.uberpb.model.Corrida;
import com.uberpb.services.CorridaService;
import com.uberpb.session.SessionManager;

import java.util.List;

public class HistoricoCorridasMenu {
    private final CorridaService corridaService;
    private final SessionManager sessionManager;

    public HistoricoCorridasMenu() {
        this.corridaService = new CorridaService();
        this.sessionManager = SessionManager.getInstance();
    }

    public void mostrarHistorico() {
        System.out.println("\n=== Histórico de Corridas ===\n");
        
        String userId = sessionManager.getCurrentSession().getUserId();
        List<Corrida> corridas = corridaService.getHistoricoCorridas(userId);
        
        if (corridas.isEmpty()) {
            System.out.println("Nenhuma corrida encontrada no histórico.");
            return;
        }

        for (Corrida corrida : corridas) {
            System.out.println("ID da Corrida: " + corrida.getId());
            System.out.println("Data: " + corrida.getDataHora());
            System.out.println("Origem: " + corrida.getOrigem());
            System.out.println("Destino: " + corrida.getDestino());
            System.out.println("Status: " + corrida.getStatus());
            System.out.println("Valor: R$ " + String.format("%.2f", corrida.getValor()));
            System.out.println("------------------------");
        }
    }
}