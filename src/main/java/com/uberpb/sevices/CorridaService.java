package com.uberpb.sevices;

import com.uberpb.model.Corrida;
import com.uberpb.model.CorridaStatus;
import com.uberpb.model.Categoria;
import com.uberpb.repository.CorridaRepository;
import com.uberpb.repository.json.CorridaRepositoryJSON;

import java.util.List;
import java.util.Optional;

public class CorridaService {

    private final CorridaRepository repository;
    private final EstimativaService estimativaService;

    public CorridaService() {
        this.repository = new CorridaRepositoryJSON();
        this.estimativaService = new EstimativaService();
    }

    public CorridaService(CorridaRepository repository) {
        this.repository = repository;
        this.estimativaService = new EstimativaService();
    }

    public CorridaService(CorridaRepository repository, EstimativaService estimativaService) {
        this.repository = repository;
        this.estimativaService = estimativaService;
    }

    public Corrida criarCorrida(String origem, String destino, Categoria categoria,
            int passageiroId, int motoristaId, int veiculoId, double distancia) {
        Corrida corrida = new Corrida(0, origem, destino, categoria,
                passageiroId, motoristaId, veiculoId, distancia);

        // Usar EstimativaService para calcular o preço usando nomes
        double precoEstimado = estimativaService.estimarPreco(origem, destino, categoria.getNome());
        corrida.setPrecoEstimado(precoEstimado);

        return repository.save(corrida);
    }

    public void iniciarCorrida(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();
            corrida.iniciarCorrida();
            repository.update(corrida);
        }
    }

    public void finalizarCorrida(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();
            corrida.finalizarCorrida();
            repository.update(corrida);
        }
    }

    public void cancelarCorrida(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();
            corrida.cancelarCorrida();
            repository.update(corrida);
        }
    }

    public List<Corrida> listarCorridas() {
        return repository.findAll();
    }

    public List<Corrida> listarCorridasPorPassageiro(int passageiroId) {
        return repository.findByPassageiroId(passageiroId);
    }

    public List<Corrida> listarCorridasPorMotorista(int motoristaId) {
        return repository.findByMotoristaId(motoristaId);
    }

    public List<Corrida> listarCorridasPorStatus(CorridaStatus status) {
        return repository.findByStatus(status);
    }

    public List<Corrida> listarCorridasPorCategoria(Categoria categoria) {
        return repository.findByCategoria(categoria);
    }

    public Optional<Corrida> getCorridaById(int id) {
        return repository.findById(id);
    }

    public double calcularReceitaMotorista(int motoristaId) {
        return repository.getTotalReceitaByMotorista(motoristaId);
    }

    public long contarCorridasPorStatus(CorridaStatus status) {
        return repository.countByStatus(status);
    }

    public boolean excluirCorrida(int corridaId) {
        return repository.deleteById(corridaId);
    }

    public void recalcularPreco(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();
            double novoPreco = estimativaService.estimarPreco(
                    corrida.getOrigem(),
                    corrida.getDestino(),
                    corrida.getCategoria().getNome());
            corrida.setPrecoEstimado(novoPreco);
            repository.update(corrida);
        }
    }

    public double estimarPrecoParaCorrida(String nomeOrigem, String nomeDestino, String categoriaNome) {
        return estimativaService.estimarPreco(nomeOrigem, nomeDestino, categoriaNome);
    }

    public boolean passageiroTemCorridaAtiva(int passageiroId) {
        List<Corrida> corridasPassageiro = repository.findByPassageiroId(passageiroId);

        return corridasPassageiro.stream()
                .anyMatch(corrida -> corrida.getStatus() != CorridaStatus.FINALIZADA
                        && corrida.getStatus() != CorridaStatus.CANCELADA);
    }

    public Optional<Corrida> obterCorridaAtivaPassageiro(int passageiroId) {
        List<Corrida> corridasPassageiro = repository.findByPassageiroId(passageiroId);

        return corridasPassageiro.stream()
                .filter(corrida -> corrida.getStatus() != CorridaStatus.FINALIZADA
                        && corrida.getStatus() != CorridaStatus.CANCELADA)
                .findFirst();
    }
}
