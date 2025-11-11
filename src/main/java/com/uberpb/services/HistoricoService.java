package com.uberpb.services;

import com.uberpb.model.*;
import com.uberpb.model.pagamento.Pagamento;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.repository.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável por integrar o histórico de corridas
 * com dados completos de corridas, pagamentos, passageiros, motoristas e veículos
 */
public class HistoricoService {

    private final DatabaseManager databaseManager;

    public HistoricoService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Gera o histórico completo de corridas para um passageiro
     * @param passageiroId ID do passageiro
     * @return Lista de itens do histórico com dados completos
     */
    public List<HistoricoItem> gerarHistoricoPassageiro(int passageiroId) {
        List<HistoricoItem> historico = new ArrayList<>();
        
        // Buscar todas as corridas do passageiro (finalizadas ou canceladas)
        List<Corrida> corridas = databaseManager.findAllCorridas().stream()
                .filter(corrida -> corrida.getPassageiroId() == passageiroId)
                .filter(corrida -> corrida.getStatus() == CorridaStatus.FINALIZADA || 
                                 corrida.getStatus() == CorridaStatus.CANCELADA)
                .collect(Collectors.toList());

        // Para cada corrida, montar o item do histórico
        for (Corrida corrida : corridas) {
            HistoricoItem item = montarItemHistorico(corrida);
            if (item != null) {
                historico.add(item);
            }
        }

        // Ordenar por data (mais recente primeiro)
        historico.sort((a, b) -> {
            if (a.getDataHoraSolicitacao() != null && b.getDataHoraSolicitacao() != null) {
                return b.getDataHoraSolicitacao().compareTo(a.getDataHoraSolicitacao());
            }
            return 0;
        });

        // T18.4: Persistir histórico em cache
        databaseManager.salvarHistoricoCache(historico);

        return historico;
    }

    /**
     * Gera o histórico completo de corridas para um motorista
     * @param motoristaId ID do motorista
     * @return Lista de itens do histórico com dados completos
     */
    public List<HistoricoItem> gerarHistoricoMotorista(int motoristaId) {
        List<HistoricoItem> historico = new ArrayList<>();
        
        // Buscar todas as corridas do motorista (finalizadas ou canceladas)
        List<Corrida> corridas = databaseManager.findAllCorridas().stream()
                .filter(corrida -> corrida.getMotoristaId() == motoristaId)
                .filter(corrida -> corrida.getStatus() == CorridaStatus.FINALIZADA || 
                                 corrida.getStatus() == CorridaStatus.CANCELADA)
                .collect(Collectors.toList());

        // Para cada corrida, montar o item do histórico
        for (Corrida corrida : corridas) {
            HistoricoItem item = montarItemHistorico(corrida);
            if (item != null) {
                historico.add(item);
            }
        }

        // Ordenar por data (mais recente primeiro)
        historico.sort((a, b) -> {
            if (a.getDataHoraSolicitacao() != null && b.getDataHoraSolicitacao() != null) {
                return b.getDataHoraSolicitacao().compareTo(a.getDataHoraSolicitacao());
            }
            return 0;
        });

        // T18.4: Persistir histórico em cache
        databaseManager.salvarHistoricoCache(historico);

        return historico;
    }

    /**
     * Monta um item completo do histórico a partir de uma corrida
     * Busca dados relacionados de pagamento, passageiro, motorista e veículo
     */
    private HistoricoItem montarItemHistorico(Corrida corrida) {
        try {
            // Criar item básico com dados da corrida
            HistoricoItem item = new HistoricoItem(
                corrida.getId(),
                corrida.getOrigem(),
                corrida.getDestino(),
                corrida.getCategoria(),
                corrida.getStatus(),
                corrida.getDistancia(),
                corrida.getDataHoraSolicitacao(),
                corrida.getDataHoraFim(),
                corrida.getDataHoraAceito(),
                corrida.getPrecoEstimado() // Valor inicial, será sobrescrito pelo pagamento
            );

            // Buscar dados do pagamento
            Optional<Pagamento> pagamento = buscarPagamentoPorCorrida(corrida.getId());
            if (pagamento.isPresent()) {
                item.setValorFinal(pagamento.get().getValor());
                item.setMetodoPagamento(pagamento.get().getTipoPagamento().toString());
                item.setStatusPagamento(pagamento.get().getStatus().toString());
            } else {
                // Se não há pagamento, usar preço estimado
                item.setValorFinal(corrida.getPrecoEstimado());
                item.setMetodoPagamento("N/A");
                item.setStatusPagamento("Pendente");
            }

            // Buscar dados do passageiro
            Optional<Passageiro> passageiro = databaseManager.findPassageiroById(corrida.getPassageiroId());
            if (passageiro.isPresent()) {
                item.setPassageiroId(corrida.getPassageiroId());
                item.setNomePassageiro(passageiro.get().getNome());
            }

            // Buscar dados do motorista
            Optional<Motorista> motorista = databaseManager.findMotoristaById(corrida.getMotoristaId());
            if (motorista.isPresent()) {
                item.setMotoristaId(corrida.getMotoristaId());
                item.setNomeMotorista(motorista.get().getNome());
            }

            // Buscar dados do veículo
            Optional<Veiculo> veiculo = databaseManager.findVeiculoById(corrida.getVeiculoId());
            if (veiculo.isPresent()) {
                item.setVeiculoId(corrida.getVeiculoId());
                item.setModeloVeiculo(veiculo.get().getModelo() + " " + veiculo.get().getMarca());
                item.setPlacaVeiculo(veiculo.get().getPlaca());
            }

            // Verificar se a corrida foi avaliada
            item.setCorridaAvaliada(corrida.isAvaliada_passageiro() && corrida.isAvaliada_motorista());

            return item;

        } catch (Exception e) {
            System.err.println("Erro ao montar item do histórico para corrida " + corrida.getId() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca o pagamento relacionado a uma corrida
     */
    private Optional<Pagamento> buscarPagamentoPorCorrida(int corridaId) {
        try {
            // Usar o DatabaseManager para buscar pagamentos
            List<Pagamento> pagamentos = databaseManager.findAllPagamentos();
            return pagamentos.stream()
                    .filter(pagamento -> pagamento.getCorridaId() == corridaId)
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Erro ao buscar pagamento para corrida " + corridaId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Gera um resumo estatístico do histórico de um usuário
     */
    public EstatisticasHistorico gerarEstatisticas(List<HistoricoItem> historico) {
        EstatisticasHistorico stats = new EstatisticasHistorico();
        
        if (historico.isEmpty()) {
            return stats;
        }

        int totalCorridas = historico.size();
        int corridasFinalizadas = (int) historico.stream()
                .filter(item -> item.getStatus() == CorridaStatus.FINALIZADA)
                .count();
        int corridasCanceladas = (int) historico.stream()
                .filter(item -> item.getStatus() == CorridaStatus.CANCELADA)
                .count();

        double valorTotal = historico.stream()
                .filter(item -> item.getStatus() == CorridaStatus.FINALIZADA)
                .mapToDouble(HistoricoItem::getValorFinal)
                .sum();

        double distanciaTotal = historico.stream()
                .filter(item -> item.getStatus() == CorridaStatus.FINALIZADA)
                .mapToDouble(HistoricoItem::getDistancia)
                .sum();

        stats.setTotalCorridas(totalCorridas);
        stats.setCorridasFinalizadas(corridasFinalizadas);
        stats.setCorridasCanceladas(corridasCanceladas);
        stats.setValorTotalGasto(valorTotal);
        stats.setDistanciaTotalPercorrida(distanciaTotal);
        stats.setValorMedio(corridasFinalizadas > 0 ? valorTotal / corridasFinalizadas : 0.0);

        return stats;
    }

    /**
     * Classe auxiliar para estatísticas do histórico
     */
    public static class EstatisticasHistorico {
        private int totalCorridas;
        private int corridasFinalizadas;
        private int corridasCanceladas;
        private double valorTotalGasto;
        private double distanciaTotalPercorrida;
        private double valorMedio;

        // Getters e Setters
        public int getTotalCorridas() { return totalCorridas; }
        public void setTotalCorridas(int totalCorridas) { this.totalCorridas = totalCorridas; }

        public int getCorridasFinalizadas() { return corridasFinalizadas; }
        public void setCorridasFinalizadas(int corridasFinalizadas) { this.corridasFinalizadas = corridasFinalizadas; }

        public int getCorridasCanceladas() { return corridasCanceladas; }
        public void setCorridasCanceladas(int corridasCanceladas) { this.corridasCanceladas = corridasCanceladas; }

        public double getValorTotalGasto() { return valorTotalGasto; }
        public void setValorTotalGasto(double valorTotalGasto) { this.valorTotalGasto = valorTotalGasto; }

        public double getDistanciaTotalPercorrida() { return distanciaTotalPercorrida; }
        public void setDistanciaTotalPercorrida(double distanciaTotalPercorrida) { this.distanciaTotalPercorrida = distanciaTotalPercorrida; }

        public double getValorMedio() { return valorMedio; }
        public void setValorMedio(double valorMedio) { this.valorMedio = valorMedio; }

        @Override
        public String toString() {
            return String.format(
                "Estatísticas do Histórico:\n" +
                "- Total de corridas: %d\n" +
                "- Corridas finalizadas: %d\n" +
                "- Corridas canceladas: %d\n" +
                "- Valor total gasto: R$ %.2f\n" +
                "- Distância total percorrida: %.1f km\n" +
                "- Valor médio por corrida: R$ %.2f",
                totalCorridas, corridasFinalizadas, corridasCanceladas,
                valorTotalGasto, distanciaTotalPercorrida, valorMedio
            );
        }
    }
}