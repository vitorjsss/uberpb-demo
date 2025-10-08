package com.uberpb.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe que representa as informações de um recibo de corrida
 * Agrega dados da corrida, pagamento, passageiro, motorista e veículo
 */
public class ReciboInfo {
    
    // Informações da corrida
    private int corridaId;
    private String origem;
    private String destino;
    private String categoria;
    private double distancia;
    private LocalDateTime dataHoraSolicitacao;
    private LocalDateTime dataHoraFim;
    private double precoFinal;
    
    // Informações do passageiro
    private String nomePassageiro;
    private String emailPassageiro;
    
    // Informações do motorista
    private String nomeMotorista;
    private String cnhMotorista;
    private double avaliacaoMotorista;
    
    // Informações do veículo
    private String modeloVeiculo;
    private String marcaVeiculo;
    private String placaVeiculo;
    private String corVeiculo;
    
    // Informações do pagamento
    private String metodoPagamento;
    private String statusPagamento;
    private LocalDateTime dataPagamento;
    
    // Construtor
    public ReciboInfo() {
    }
    
    // Construtor completo
    public ReciboInfo(int corridaId, String origem, String destino, String categoria,
                     double distancia, LocalDateTime dataHoraSolicitacao, LocalDateTime dataHoraFim,
                     double precoFinal, String nomePassageiro, String emailPassageiro,
                     String nomeMotorista, String cnhMotorista, double avaliacaoMotorista,
                     String modeloVeiculo, String marcaVeiculo, String placaVeiculo, String corVeiculo,
                     String metodoPagamento, String statusPagamento, LocalDateTime dataPagamento) {
        this.corridaId = corridaId;
        this.origem = origem;
        this.destino = destino;
        this.categoria = categoria;
        this.distancia = distancia;
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.dataHoraFim = dataHoraFim;
        this.precoFinal = precoFinal;
        this.nomePassageiro = nomePassageiro;
        this.emailPassageiro = emailPassageiro;
        this.nomeMotorista = nomeMotorista;
        this.cnhMotorista = cnhMotorista;
        this.avaliacaoMotorista = avaliacaoMotorista;
        this.modeloVeiculo = modeloVeiculo;
        this.marcaVeiculo = marcaVeiculo;
        this.placaVeiculo = placaVeiculo;
        this.corVeiculo = corVeiculo;
        this.metodoPagamento = metodoPagamento;
        this.statusPagamento = statusPagamento;
        this.dataPagamento = dataPagamento;
    }
    
    /**
     * Gera um recibo formatado em texto
     */
    public String gerarReciboFormatado() {
        StringBuilder recibo = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        recibo.append("==================================================\n");
        recibo.append("                   RECIBO UBERPB                 \n");
        recibo.append("==================================================\n\n");
        
        // Informações da corrida (sem mostrar o ID)
        recibo.append("📍 DETALHES DA CORRIDA\n");
        recibo.append("Origem: ").append(origem).append("\n");
        recibo.append("Destino: ").append(destino).append("\n");
        recibo.append("Categoria: ").append(categoria).append("\n");
        recibo.append("Distância: ").append(String.format("%.2f km", distancia)).append("\n");
        recibo.append("Data/Hora Solicitação: ").append(dataHoraSolicitacao.format(formatter)).append("\n");
        if (dataHoraFim != null) {
            recibo.append("Data/Hora Finalização: ").append(dataHoraFim.format(formatter)).append("\n");
        }
        recibo.append("\n");
        
        // Informações do passageiro
        recibo.append("👤 PASSAGEIRO\n");
        recibo.append("Nome: ").append(nomePassageiro).append("\n");
        recibo.append("Email: ").append(emailPassageiro).append("\n\n");
        
        // Informações do motorista
        recibo.append("🚗 MOTORISTA\n");
        recibo.append("Nome: ").append(nomeMotorista).append("\n");
        recibo.append("CNH: ").append(cnhMotorista).append("\n");
        recibo.append("Avaliação: ").append(String.format("%.1f/5.0", avaliacaoMotorista)).append("\n\n");
        
        // Informações do veículo
        recibo.append("🚙 VEÍCULO\n");
        recibo.append("Modelo: ").append(modeloVeiculo).append("\n");
        recibo.append("Marca: ").append(marcaVeiculo).append("\n");
        recibo.append("Placa: ").append(placaVeiculo).append("\n");
        recibo.append("Cor: ").append(corVeiculo).append("\n\n");
        
        // Informações do pagamento
        recibo.append("💰 PAGAMENTO\n");
        recibo.append("Método: ").append(metodoPagamento).append("\n");
        recibo.append("Status: ").append(statusPagamento).append("\n");
        if (dataPagamento != null) {
            recibo.append("Data do Pagamento: ").append(dataPagamento.format(formatter)).append("\n");
        }
        recibo.append("\n");
        
        // Valor final
        recibo.append("==================================================\n");
        recibo.append("VALOR TOTAL: R$ ").append(String.format("%.2f", precoFinal)).append("\n");
        recibo.append("==================================================\n");
        
        recibo.append("\nObrigado por usar o UberPB!\n");
        recibo.append("Avalie sua experiência e nos ajude a melhorar.\n");
        
        return recibo.toString();
    }
    
    /**
     * Calcula a duração da corrida em minutos
     */
    public long getDuracaoCorridaMinutos() {
        if (dataHoraSolicitacao != null && dataHoraFim != null) {
            return java.time.Duration.between(dataHoraSolicitacao, dataHoraFim).toMinutes();
        }
        return 0;
    }
    
    /**
     * Retorna o preço por quilômetro da corrida
     */
    public double getPrecoPorKm() {
        if (distancia > 0) {
            return precoFinal / distancia;
        }
        return 0;
    }
    
    // Getters e Setters
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
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
    }
    
    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }
    
    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
    
    public double getPrecoFinal() {
        return precoFinal;
    }
    
    public void setPrecoFinal(double precoFinal) {
        this.precoFinal = precoFinal;
    }
    
    public String getNomePassageiro() {
        return nomePassageiro;
    }
    
    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }
    
    public String getEmailPassageiro() {
        return emailPassageiro;
    }
    
    public void setEmailPassageiro(String emailPassageiro) {
        this.emailPassageiro = emailPassageiro;
    }
    
    public String getNomeMotorista() {
        return nomeMotorista;
    }
    
    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }
    
    public String getCnhMotorista() {
        return cnhMotorista;
    }
    
    public void setCnhMotorista(String cnhMotorista) {
        this.cnhMotorista = cnhMotorista;
    }
    
    public double getAvaliacaoMotorista() {
        return avaliacaoMotorista;
    }
    
    public void setAvaliacaoMotorista(double avaliacaoMotorista) {
        this.avaliacaoMotorista = avaliacaoMotorista;
    }
    
    public String getModeloVeiculo() {
        return modeloVeiculo;
    }
    
    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }
    
    public String getMarcaVeiculo() {
        return marcaVeiculo;
    }
    
    public void setMarcaVeiculo(String marcaVeiculo) {
        this.marcaVeiculo = marcaVeiculo;
    }
    
    public String getPlacaVeiculo() {
        return placaVeiculo;
    }
    
    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }
    
    public String getCorVeiculo() {
        return corVeiculo;
    }
    
    public void setCorVeiculo(String corVeiculo) {
        this.corVeiculo = corVeiculo;
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
    
    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }
    
    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}