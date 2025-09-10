package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.time.LocalDate;

// Interface Observer - quem quer ser notificado implementa isso
interface VeiculoObserver {
    void onVeiculoStatusChanged(Veiculo veiculo, String novoStatus, String statusAnterior);
    void onVeiculoLocalizacaoChanged(Veiculo veiculo, String novaLocalizacao);
    void onVeiculoDisponibilidadeChanged(Veiculo veiculo, boolean disponivel);
}

// Interface Subject - quem será observado
interface VeiculoSubject {
    void addObserver(VeiculoObserver observer);
    void removeObserver(VeiculoObserver observer);
    void notifyStatusChange(String novoStatus, String statusAnterior);
    void notifyLocalizacaoChange(String novaLocalizacao);
    void notifyDisponibilidadeChange(boolean disponivel);
}

public class Veiculo implements VeiculoSubject {
    private int id;
    private String modelo;
    private String marca;
    private int ano;
    private String cor;
    private String placa;
    private String tipo; // Ex: Carro, Moto, Bicicleta
    private String categoria; // Ex: Econômico, Luxo, SUV
    
    // Atributos obrigatórios da task T2.1
    private double capacidadePortaMalas; // em litros
    private int numeroPassageiros;
    
    // Atributos para Observer Pattern
    private List<VeiculoObserver> observers;
    private String status; // Ex: "Disponível", "Ocupado", "Em Manutenção", "Offline"
    private String localizacao; // Ex: coordenadas ou endereço atual
    private boolean disponivel;

    // Construtor completo
    public Veiculo(int id, String modelo, String marca, int ano, String cor, String placa, 
                   String tipo, String categoria, double capacidadePortaMalas, int numeroPassageiros) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.cor = cor;
        this.placa = placa;
        this.tipo = tipo;
        this.categoria = categoria;
        this.capacidadePortaMalas = capacidadePortaMalas;
        this.numeroPassageiros = numeroPassageiros;
        
        // Inicialização do Observer Pattern
        this.observers = new ArrayList<>();
        this.status = "Disponível";
        this.disponivel = true;
        this.localizacao = "Não definida";
    }
    
    // Construtor padrão
    public Veiculo() {
        this.observers = new ArrayList<>();
        this.status = "Disponível";
        this.disponivel = true;
        this.localizacao = "Não definida";
    }

    // Implementação do Observer Pattern
    @Override
    public void addObserver(VeiculoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(VeiculoObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyStatusChange(String novoStatus, String statusAnterior) {
        for (VeiculoObserver observer : observers) {
            observer.onVeiculoStatusChanged(this, novoStatus, statusAnterior);
        }
    }

    @Override
    public void notifyLocalizacaoChange(String novaLocalizacao) {
        for (VeiculoObserver observer : observers) {
            observer.onVeiculoLocalizacaoChanged(this, novaLocalizacao);
        }
    }

    @Override
    public void notifyDisponibilidadeChange(boolean disponivel) {
        for (VeiculoObserver observer : observers) {
            observer.onVeiculoDisponibilidadeChanged(this, disponivel);
        }
    }

    // Getters e Setters originais
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public double getCapacidadePortaMalas() {
        return capacidadePortaMalas;
    }
    
    public void setCapacidadePortaMalas(double capacidadePortaMalas) {
        this.capacidadePortaMalas = capacidadePortaMalas;
    }
    
    public int getNumeroPassageiros() {
        return numeroPassageiros;
    }
    
    public void setNumeroPassageiros(int numeroPassageiros) {
        this.numeroPassageiros = numeroPassageiros;
    }

    // Getters e Setters para Observer Pattern (com notificações)
    public String getStatus() {
        return status;
    }

    public void setStatus(String novoStatus) {
        if (!Objects.equals(this.status, novoStatus)) {
            String statusAnterior = this.status;
            this.status = novoStatus;
            notifyStatusChange(novoStatus, statusAnterior);
        }
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String novaLocalizacao) {
        if (!Objects.equals(this.localizacao, novaLocalizacao)) {
            this.localizacao = novaLocalizacao;
            notifyLocalizacaoChange(novaLocalizacao);
        }
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean novaDisponibilidade) {
        if (this.disponivel != novaDisponibilidade) {
            this.disponivel = novaDisponibilidade;
            notifyDisponibilidadeChange(novaDisponibilidade);
        }
    }

    // Métodos de negócio com notificações automáticas
    public void iniciarCorrida() {
        setStatus("Em Corrida");
        setDisponivel(false);
    }

    public void finalizarCorrida() {
        setStatus("Disponível");
        setDisponivel(true);
    }

    public void entrarEmManutencao() {
        setStatus("Em Manutenção");
        setDisponivel(false);
    }

    public void ficarOffline() {
        setStatus("Offline");
        setDisponivel(false);
    }
    
    // Métodos utilitários
    public int getIdadeVeiculo() {
        return LocalDate.now().getYear() - this.ano;
    }
    
    public boolean isVeiculoNovo() {
        return getIdadeVeiculo() <= 1;
    }
    
    // toString atualizado
    @Override
    public String toString() {
        return String.format("Veiculo{id=%d, modelo='%s', marca='%s', ano=%d, cor='%s', " +
                           "placa='%s', tipo='%s', categoria='%s', capacidadePortaMalas=%.1fL, " +
                           "numeroPassageiros=%d, status='%s', disponivel=%s, localizacao='%s'}", 
                           id, modelo, marca, ano, cor, placa, tipo, categoria, 
                           capacidadePortaMalas, numeroPassageiros, status, disponivel, localizacao);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Veiculo veiculo = (Veiculo) obj;
        return id == veiculo.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}