package com.uberpb.repository;

import com.uberpb.model.Avaliacao;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository para gerenciar operações CRUD das avaliações
 * Segue o padrão estabelecido por outras entidades do projeto
 */
public class AvaliacaoRepository extends BaseRepository<Avaliacao> {

    public AvaliacaoRepository() {
        super("avaliacoes");
    }

    // ===== Operações CRUD básicas =====

    /**
     * Salva uma nova avaliação
     */
    public Avaliacao save(Avaliacao avaliacao) {
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação não pode ser nula");
        }

        if (!avaliacao.isValid()) {
            throw new IllegalArgumentException("Dados da avaliação são inválidos");
        }

        List<Avaliacao> avaliacoes = loadAll();
        
        if (avaliacao.getId() == 0) {
            // Nova avaliação - gerar ID
            avaliacao.setId(generateNextId());
        }
        
        avaliacoes.add(avaliacao);
        saveAll(avaliacoes);
        
        return avaliacao;
    }

    /**
     * Busca uma avaliação por ID
     */
    public Optional<Avaliacao> findById(int id) {
        return loadAll().stream()
                .filter(avaliacao -> avaliacao.getId() == id)
                .findFirst();
    }

    /**
     * Lista todas as avaliações
     */
    public List<Avaliacao> findAll() {
        return loadAll();
    }

    /**
     * Busca avaliações por corrida
     */
    public List<Avaliacao> findByCorridaId(int corridaId) {
        return loadAll().stream()
                .filter(avaliacao -> avaliacao.getCorridaId() == corridaId)
                .collect(Collectors.toList());
    }

    /**
     * Busca avaliações feitas por um usuário específico
     */
    public List<Avaliacao> findByAvaliadorId(int avaliadorId) {
        return loadAll().stream()
                .filter(avaliacao -> avaliacao.getAvaliadorId() == avaliadorId)
                .collect(Collectors.toList());
    }

    /**
     * Busca avaliações recebidas por um usuário específico
     */
    public List<Avaliacao> findByAvaliadoId(int avaliadoId) {
        return loadAll().stream()
                .filter(avaliacao -> avaliacao.getAvaliadoId() == avaliadoId)
                .collect(Collectors.toList());
    }

    /**
     * Busca avaliações de motoristas (feitas por passageiros)
     */
    public List<Avaliacao> findAvaliacoesDeMotoristas() {
        return loadAll().stream()
                .filter(avaliacao -> "MOTORISTA".equals(avaliacao.getTipoAvaliado()))
                .collect(Collectors.toList());
    }

    /**
     * Busca avaliações de passageiros (feitas por motoristas)
     */
    public List<Avaliacao> findAvaliacoesDePassageiros() {
        return loadAll().stream()
                .filter(avaliacao -> "PASSAGEIRO".equals(avaliacao.getTipoAvaliado()))
                .collect(Collectors.toList());
    }

    /**
     * Verifica se uma corrida já foi avaliada por um tipo específico
     */
    public boolean corridaJaAvaliadaPor(int corridaId, int avaliadorId, String tipoAvaliador) {
        return loadAll().stream()
                .anyMatch(avaliacao -> 
                    avaliacao.getCorridaId() == corridaId &&
                    avaliacao.getAvaliadorId() == avaliadorId &&
                    tipoAvaliador.equals(avaliacao.getTipoAvaliador()));
    }

    /**
     * Calcula a média de avaliações de um usuário
     */
    public double calcularMediaAvaliacoes(int avaliadoId) {
        List<Avaliacao> avaliacoes = findByAvaliadoId(avaliadoId);
        
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        
        double soma = avaliacoes.stream()
                .mapToDouble(Avaliacao::getNota)
                .sum();
        
        return soma / avaliacoes.size();
    }

    /**
     * Conta o total de avaliações de um usuário
     */
    public int contarAvaliacoes(int avaliadoId) {
        return findByAvaliadoId(avaliadoId).size();
    }

    /**
     * Remove uma avaliação por ID
     */
    public boolean deleteById(int id) {
        List<Avaliacao> avaliacoes = loadAll();
        boolean removed = avaliacoes.removeIf(avaliacao -> avaliacao.getId() == id);
        
        if (removed) {
            saveAll(avaliacoes);
        }
        
        return removed;
    }

    /**
     * Atualiza uma avaliação existente
     */
    public Optional<Avaliacao> update(Avaliacao avaliacaoAtualizada) {
        if (avaliacaoAtualizada == null || !avaliacaoAtualizada.isValid()) {
            return Optional.empty();
        }

        List<Avaliacao> avaliacoes = loadAll();
        
        for (int i = 0; i < avaliacoes.size(); i++) {
            if (avaliacoes.get(i).getId() == avaliacaoAtualizada.getId()) {
                avaliacoes.set(i, avaliacaoAtualizada);
                saveAll(avaliacoes);
                return Optional.of(avaliacaoAtualizada);
            }
        }
        
        return Optional.empty();
    }

    // ===== Métodos abstratos do BaseRepository =====

    @Override
    protected int getId(Avaliacao avaliacao) {
        return avaliacao.getId();
    }

    @Override
    protected void setId(Avaliacao avaliacao, int id) {
        avaliacao.setId(id);
    }
}