package com.uberpb.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Interface Observer para mudanças nas categorias
interface CategoriaObserver {
    void onCategoriaPrecoChanged(Categoria categoria, double novoMultiplicador, double multiplicadorAnterior);

    void onCategoriaCapacidadeChanged(Categoria categoria, int novaCapacidade, int capacidadeAnterior);

    void onCategoriaStatusChanged(Categoria categoria, String evento);
}

// Interface Subject para categorias
interface CategoriaSubject {
    void addObserver(CategoriaObserver observer);

    void removeObserver(CategoriaObserver observer);

    void notifyPrecoChange(double novoMultiplicador, double multiplicadorAnterior);

    void notifyCapacidadeChange(int novaCapacidade, int capacidadeAnterior);

    void notifyStatusChange(String evento);
}

public enum Categoria implements CategoriaSubject {
    UBER_X("UberX", "Categoria econômica", 1.0, 4),
    COMFORT("Comfort", "Categoria confort com veículos mais novos", 1.2, 4),
    BLACK("Black", "Categoria premium com veículos de luxo", 1.5, 4),
    BAG("Bag", "Categoria com espaço extra para bagagem", 1.1, 4),
    XL("XL", "Categoria para grupos maiores", 1.3, 6);

    private final String nome;
    private final String descricao;
    private double multiplicadorPreco; // Não final para permitir mudanças
    private int capacidadeMaximaPassageiros; // Não final para permitir mudanças

    // Lista de observers para cada instância do enum
    private final List<CategoriaObserver> observers;

    // Construtor do enum
    Categoria(String nome, String descricao, double multiplicadorPreco, int capacidadeMaximaPassageiros) {
        this.nome = nome;
        this.descricao = descricao;
        this.multiplicadorPreco = multiplicadorPreco;
        this.capacidadeMaximaPassageiros = capacidadeMaximaPassageiros;
        this.observers = new CopyOnWriteArrayList<>(); // Thread-safe para enums
    }

    // Implementação do Observer Pattern
    @Override
    public void addObserver(CategoriaObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(CategoriaObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyPrecoChange(double novoMultiplicador, double multiplicadorAnterior) {
        for (CategoriaObserver observer : observers) {
            observer.onCategoriaPrecoChanged(this, novoMultiplicador, multiplicadorAnterior);
        }
    }

    @Override
    public void notifyCapacidadeChange(int novaCapacidade, int capacidadeAnterior) {
        for (CategoriaObserver observer : observers) {
            observer.onCategoriaCapacidadeChanged(this, novaCapacidade, capacidadeAnterior);
        }
    }

    @Override
    public void notifyStatusChange(String evento) {
        for (CategoriaObserver observer : observers) {
            observer.onCategoriaStatusChanged(this, evento);
        }
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getMultiplicadorPreco() {
        return multiplicadorPreco;
    }

    public int getCapacidadeMaximaPassageiros() {
        return capacidadeMaximaPassageiros;
    }

    // Setters com notificação (Observer Pattern)
    public void setMultiplicadorPreco(double novoMultiplicador) {
        if (this.multiplicadorPreco != novoMultiplicador) {
            double multiplicadorAnterior = this.multiplicadorPreco;
            this.multiplicadorPreco = novoMultiplicador;
            notifyPrecoChange(novoMultiplicador, multiplicadorAnterior);
            notifyStatusChange("Multiplicador de preço alterado");
        }
    }

    public void setCapacidadeMaximaPassageiros(int novaCapacidade) {
        if (this.capacidadeMaximaPassageiros != novaCapacidade) {
            int capacidadeAnterior = this.capacidadeMaximaPassageiros;
            this.capacidadeMaximaPassageiros = novaCapacidade;
            notifyCapacidadeChange(novaCapacidade, capacidadeAnterior);
            notifyStatusChange("Capacidade máxima alterada");
        }
    }

    // Métodos utilitários
    public boolean isCategoriaPremium() {
        return this == BLACK || this == COMFORT;
    }

    public boolean isCategoriaParagrupos() {
        return this == XL;
    }

    public boolean isCategoriaBagagem() {
        return this == BAG;
    }

    // Método para calcular preço baseado na categoria
    public double calcularPreco(double precoBase) {
        double precoCalculado = precoBase * multiplicadorPreco;
        notifyStatusChange("Preço calculado: R$ " + String.format("%.2f", precoCalculado));
        return precoCalculado;
    }

    // Método para buscar categoria por nome
    public static Categoria buscarPorNome(String nome) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.getNome().equalsIgnoreCase(nome)) {
                categoria.notifyStatusChange("Categoria encontrada por busca");
                return categoria;
            }
        }
        return null;
    }

    // Método para verificar se veículo atende à categoria
    public boolean veiculoAtende(int numeroPassageiros, int anoVeiculo) {
        // Verifica capacidade de passageiros
        if (numeroPassageiros < capacidadeMaximaPassageiros) {
            notifyStatusChange("Veículo rejeitado: capacidade insuficiente");
            return false;
        }

        // Comfort e Black exigem veículos mais novos
        if ((this == COMFORT || this == BLACK)) {
            int anoAtual = java.time.LocalDate.now().getYear();
            boolean atende = (anoAtual - anoVeiculo) <= 5;
            if (!atende) {
                notifyStatusChange("Veículo rejeitado: muito antigo para categoria premium");
            } else {
                notifyStatusChange("Veículo aprovado para categoria premium");
            }
            return atende;
        }

        notifyStatusChange("Veículo aprovado para categoria");
        return true;
    }

    // Métodos de negócio com notificações
    public void aplicarPromocao(double desconto) {
        double novoMultiplicador = multiplicadorPreco * (1 - desconto);
        setMultiplicadorPreco(novoMultiplicador);
        notifyStatusChange("Promoção aplicada: " + (desconto * 100) + "% de desconto");
    }

    public void aumentarCapacidade(int passageirosAdicionais) {
        setCapacidadeMaximaPassageiros(capacidadeMaximaPassageiros + passageirosAdicionais);
        notifyStatusChange("Capacidade aumentada em " + passageirosAdicionais + " passageiros");
    }

    public void restaurarValoresOriginais() {
        // Valores originais baseados na definição inicial
        switch (this) {
            case UBER_X:
                setMultiplicadorPreco(1.0);
                setCapacidadeMaximaPassageiros(4);
                break;
            case COMFORT:
                setMultiplicadorPreco(1.2);
                setCapacidadeMaximaPassageiros(4);
                break;
            case BLACK:
                setMultiplicadorPreco(1.5);
                setCapacidadeMaximaPassageiros(4);
                break;
            case BAG:
                setMultiplicadorPreco(1.1);
                setCapacidadeMaximaPassageiros(4);
                break;
            case XL:
                setMultiplicadorPreco(1.3);
                setCapacidadeMaximaPassageiros(6);
                break;
        }
        notifyStatusChange("Valores restaurados para configuração original");
    }

    // toString personalizado
    @Override
    public String toString() {
        return String.format("%s (%.1fx) - %s [Cap: %d passageiros]",
                nome, multiplicadorPreco, descricao, capacidadeMaximaPassageiros);
    }

    // Método para listar todas as categorias
    public static void listarCategorias() {
        System.out.println("Categorias disponíveis:");
        for (Categoria categoria : Categoria.values()) {
            System.out.println("- " + categoria);
            categoria.notifyStatusChange("Categoria listada");
        }
    }

    // Método para obter número de observers (útil para debug)
    public int getNumeroObservers() {
        return observers.size();
    }
}