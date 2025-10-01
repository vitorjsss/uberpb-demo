package com.uberpb.model;

import com.uberpb.enums.Categoria;
import com.uberpb.enums.CorridaStatus;

import java.time.LocalDateTime;

public class Corrida {

    private int id;
    private String origem;
    private String destino;
    private Categoria categoria;
    private CorridaStatus status;

    // Relacionamentos via ID (mantém padrão de persistência JSON do projeto)
    private int passageiroId;
    private int motoristaId;
    private int veiculoId;

    // Informações adicionais
    private double precoEstimado;
    private double distancia;
    private LocalDateTime dataHoraSolicitacao;
    private LocalDateTime dataHoraFim;
    private LocalDateTime dataHoraAceito;
    private int tempoRestante; // em minutos

    // ===== Construtores =====
    public Corrida() {
    }

    public Corrida(int id, String origem, String destino, Categoria categoria,
            int passageiroId, int motoristaId, int veiculoId, double distancia) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.categoria = categoria;
        this.status = CorridaStatus.PENDENTE;
        this.passageiroId = passageiroId;
        this.motoristaId = motoristaId;
        this.veiculoId = veiculoId;
        this.distancia = distancia;
        this.dataHoraSolicitacao = LocalDateTime.now();
    }

    // ===== Métodos de Negócio =====
    public void iniciarCorrida() {
        if (this.status == CorridaStatus.PENDENTE) {
            this.status = CorridaStatus.EM_ANDAMENTO;
            this.dataHoraAceito = LocalDateTime.now();
            // Calcular tempo restante baseado na distância (estimativa: 3 minutos por km)
            this.tempoRestante = (int) Math.ceil(this.distancia * 3);
        }
    }

    public void finalizarCorrida() {
        if (this.status == CorridaStatus.EM_ANDAMENTO) {
            this.status = CorridaStatus.FINALIZADA;
            this.dataHoraFim = LocalDateTime.now();
            this.tempoRestante = 0; // Corrida finalizada
        }
    }

    public void cancelarCorrida() {
        if (this.status == CorridaStatus.PENDENTE || this.status == CorridaStatus.EM_ANDAMENTO) {
            this.status = CorridaStatus.CANCELADA;
            this.dataHoraFim = LocalDateTime.now();
            this.tempoRestante = 0; // Corrida cancelada
        }
    }

    /**
     * Calcula o tempo restante real baseado no tempo decorrido desde que foi aceita
     * 
     * @return tempo restante em minutos, 0 se já passou do tempo estimado
     */
    public int calcularTempoRestanteReal() {
        if (this.status != CorridaStatus.EM_ANDAMENTO || this.dataHoraAceito == null) {
            return this.tempoRestante;
        }

        long minutosDecorridos = java.time.Duration.between(this.dataHoraAceito, LocalDateTime.now()).toMinutes();
        int tempoRestanteReal = this.tempoRestante - (int) minutosDecorridos;

        return Math.max(0, tempoRestanteReal);
    }

    /**
     * Método auxiliar de cálculo de preço.
     * Normalmente o CorridaService usa EstimativaService, mas pode ser usado aqui
     * também.
     */
    public void calcularPreco(double precoBaseKm) {
        if (this.categoria != null) {
            this.precoEstimado = precoBaseKm * this.distancia * this.categoria.getMultiplicadorPreco();
        } else {
            this.precoEstimado = precoBaseKm * this.distancia;
        }
    }

    // ===== Getters e Setters =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    }

    public int getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(int passageiroId) {
        this.passageiroId = passageiroId;
    }

    public int getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(int motoristaId) {
        this.motoristaId = motoristaId;
    }

    public int getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(int veiculoId) {
        this.veiculoId = veiculoId;
    }

    public double getPrecoEstimado() {
        return precoEstimado;
    }

    public void setPrecoEstimado(double precoEstimado) {
        this.precoEstimado = precoEstimado;
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

    public LocalDateTime getDataHoraAceito() {
        return dataHoraAceito;
    }

    public void setDataHoraAceito(LocalDateTime dataHoraAceito) {
        this.dataHoraAceito = dataHoraAceito;
    }

    public int getTempoRestante() {
        return tempoRestante;
    }

    public void setTempoRestante(int tempoRestante) {
        this.tempoRestante = tempoRestante;
    }

    @Override
    public String toString() {
        return "Corrida{" +
                "id=" + id +
                ", origem='" + origem + '\'' +
                ", destino='" + destino + '\'' +
                ", categoria=" + categoria +
                ", status=" + status +
                ", passageiroId=" + passageiroId +
                ", motoristaId=" + motoristaId +
                ", veiculoId=" + veiculoId +
                ", precoEstimado=" + precoEstimado +
                ", distancia=" + distancia +
                ", dataHoraSolicitacao=" + dataHoraSolicitacao +
                ", dataHoraFim=" + dataHoraFim +
                ", dataHoraAceito=" + dataHoraAceito +
                ", tempoRestante=" + tempoRestante +
                '}';
    }
}
