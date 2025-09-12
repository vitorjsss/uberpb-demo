package com.uberpb.model;

public class Veiculo {
    private int id;
    private String modelo;
    private String marca;
    private int ano; // Mudado para int para facilitar cálculos
    private String cor;
    private String placa;
    private String categoria; // Volta a ser String

    // Atributos adicionais obrigatórios da task T2.1
    private double capacidadePortaMalas; // em litros
    private int numeroPassageiros;

    // Construtor completo
    public Veiculo(int id, String modelo, String marca, int ano, String cor, String placa,
            String categoria, double capacidadePortaMalas, int numeroPassageiros) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.cor = cor;
        this.placa = placa;
        this.categoria = categoria;
        this.capacidadePortaMalas = capacidadePortaMalas;
        this.numeroPassageiros = numeroPassageiros;
    }

    // Construtor padrão
    public Veiculo() {
    }

    // Getters e Setters existentes
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Getters e Setters para novos atributos obrigatórios
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

    // Métodos utilitários
    public int getIdadeVeiculo() {
        return java.time.LocalDate.now().getYear() - this.ano;
    }

    public boolean isVeiculoNovo() {
        return getIdadeVeiculo() <= 1;
    }

    // toString para facilitar debug e exibição
    @Override
    public String toString() {
        return String.format("Veiculo{id=%d, modelo='%s', marca='%s', ano=%d, cor='%s', " +
                "placa='%s', categoria='%s', capacidadePortaMalas=%.1fL, " +
                "numeroPassageiros=%d}",
                id, modelo, marca, ano, cor, placa, categoria != null ? categoria : "",
                capacidadePortaMalas, numeroPassageiros);
    }

    // equals e hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Veiculo veiculo = (Veiculo) obj;
        return id == veiculo.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}