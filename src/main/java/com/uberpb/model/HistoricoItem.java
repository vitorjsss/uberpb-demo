package com.uberpb.model;

import com.uberpb.enums.Categoria;
import com.uberpb.enums.CorridaStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um item completo do histórico de corridas
 * Integra dados da corrida, pagamento e informações dos participantes
 */
public class HistoricoItem {

    // Dados da corrida
    private int corridaId;
    private String origem;
    private String destino;
    private Categoria categoria;
    private CorridaStatus status;
    private double distancia;
    private LocalDateTime dataHoraSolicitacao;
    private LocalDateTime dataHoraFim;
    private LocalDateTime dataHoraAceito;
    
    // Dados do pagamento
    private double valorFinal;
    private String metodoPagamento;
    private String statusPagamento;
    
    // Dados dos participantes (dependendo da perspectiva)
    private int passageiroId;
    private String nomePassageiro;
    private int motoristaId;
    private String nomeMotorista;
    
    // Dados do veículo
    private int veiculoId;
    private String modeloVeiculo;
    private String placaVeiculo;
    
    // Metadados
    private String duracao; // Calculada entre dataHoraAceito e dataHoraFim
    private boolean corridaAvaliada;

    // ===== Construtores =====
    public HistoricoItem() {
    }

    public HistoricoItem(int corridaId, String origem, String destino, Categoria categoria, 
                        CorridaStatus status, double distancia, LocalDateTime dataHoraSolicitacao,
                        LocalDateTime dataHoraFim, LocalDateTime dataHoraAceito, double valorFinal) {
        this.corridaId = corridaId;
        this.origem = origem;
        this.destino = destino;
        this.categoria = categoria;
        this.status = status;
        this.distancia = distancia;
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.dataHoraFim = dataHoraFim;
        this.dataHoraAceito = dataHoraAceito;
        this.valorFinal = valorFinal;
        this.duracao = calcularDuracao();
    }

    // ===== Métodos de Negócio =====
    
    /**
     * Calcula a duração da corrida em formato legível
     */
    private String calcularDuracao() {
        if (dataHoraAceito != null && dataHoraFim != null) {
            java.time.Duration duration = java.time.Duration.between(dataHoraAceito, dataHoraFim);
            long minutos = duration.toMinutes();
            if (minutos < 60) {
                return minutos + "min";
            } else {
                long horas = minutos / 60;
                long minutosRestantes = minutos % 60;
                return horas + "h " + minutosRestantes + "min";
            }
        }
        return "N/A";
    }

    /**
     * Retorna a data da corrida formatada
     */
    public String getDataFormatada() {
        if (dataHoraSolicitacao != null) {
            return dataHoraSolicitacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "N/A";
    }

    /**
     * Retorna informações do trajeto
     */
    public String getTrajeto() {
        return origem + " → " + destino;
    }

    /**
     * Retorna o status da corrida formatado
     */
    public String getStatusFormatado() {
        if (status != null) {
            switch (status) {
                case FINALIZADA: return "Concluída";
                case CANCELADA: return "Cancelada";
                case EM_ANDAMENTO: return "Em andamento";
                case PENDENTE: return "Pendente";
                case AVALIACAO: return "Aguardando avaliação";
                default: return status.toString();
            }
        }
        return "Desconhecido";
    }

    // ===== Getters e Setters =====

    public int getCorridaId() {
        return corridaId;
    }

    public void setCorridaId(int corridaId) {
        this.corridaId = corridaId;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public CorridaStatus getStatus() {
        return status;
    }

    public void setStatus(CorridaStatus status) {
        this.status = status;
        this.duracao = calcularDuracao(); // Recalcular duração
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public LocalDateTime getDataHoraSolicitacao() {
        return dataHoraSolicitacao;
    }

    public void setDataHoraSolicitacao(LocalDateTime dataHoraSolicitacao) {
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.duracao = calcularDuracao();
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
        this.duracao = calcularDuracao();
    }

    public LocalDateTime getDataHoraAceito() {
        return dataHoraAceito;
    }

    public void setDataHoraAceito(LocalDateTime dataHoraAceito) {
        this.dataHoraAceito = dataHoraAceito;
        this.duracao = calcularDuracao();
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public int getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(int passageiroId) {
        this.passageiroId = passageiroId;
    }

    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

    public int getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(int motoristaId) {
        this.motoristaId = motoristaId;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public int getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(int veiculoId) {
        this.veiculoId = veiculoId;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public boolean isCorridaAvaliada() {
        return corridaAvaliada;
    }

    public void setCorridaAvaliada(boolean corridaAvaliada) {
        this.corridaAvaliada = corridaAvaliada;
    }

    /**
     * Formata o item do histórico para exibição amigável
     * @return String formatada com os dados principais do histórico
     */
    public String formatarParaExibicao() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CORRIDA #").append(corridaId).append(" ===\n");
        sb.append("Trajeto: ").append(getTrajeto()).append("\n");
        sb.append("Data: ").append(getDataFormatada()).append("\n");
        sb.append("Status: ").append(getStatusFormatado()).append("\n");
        sb.append("Categoria: ").append(categoria != null ? categoria.getNome() : "N/A").append("\n");
        sb.append("Valor Final: R$ ").append(String.format("%.2f", valorFinal)).append("\n");
        sb.append("Duração: ").append(duracao != null ? duracao : "N/A").append("\n");
        
        if (nomeMotorista != null && !nomeMotorista.isEmpty()) {
            sb.append("Motorista: ").append(nomeMotorista).append("\n");
        }
        if (nomePassageiro != null && !nomePassageiro.isEmpty()) {
            sb.append("Passageiro: ").append(nomePassageiro).append("\n");
        }
        if (modeloVeiculo != null && !modeloVeiculo.isEmpty()) {
            sb.append("Veículo: ").append(modeloVeiculo);
            if (placaVeiculo != null && !placaVeiculo.isEmpty()) {
                sb.append(" (").append(placaVeiculo).append(")");
            }
            sb.append("\n");
        }
        
        sb.append("Avaliada: ").append(corridaAvaliada ? "Sim" : "Não").append("\n");
        sb.append("=============================");
        
        return sb.toString();
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "HistoricoItem{" +
                "corridaId=" + corridaId +
                ", trajeto='" + getTrajeto() + '\'' +
                ", categoria=" + categoria +
                ", status=" + status +
                ", valorFinal=" + valorFinal +
                ", data='" + getDataFormatada() + '\'' +
                ", duracao='" + duracao + '\'' +
                '}';
    }
}