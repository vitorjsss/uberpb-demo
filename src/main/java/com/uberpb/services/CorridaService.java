package com.uberpb.services;

import com.uberpb.model.Corrida;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.enums.Categoria;
import com.uberpb.model.Motorista;
import com.uberpb.model.Veiculo;
import com.uberpb.model.pagamento.Pagamento;
import com.uberpb.repository.CorridaRepository;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.repository.json.CorridaRepositoryJSON;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CorridaService {

    private final CorridaRepository repository;
    private final EstimativaService estimativaService;
    private final DatabaseManager databaseManager;
    private final LocalizacaoService localizacaoService;

    public CorridaService() {
        this.repository = new CorridaRepositoryJSON();
        this.estimativaService = new EstimativaService();
        this.databaseManager = new DatabaseManager();
        this.localizacaoService = new LocalizacaoService();
    }

    public CorridaService(CorridaRepository repository) {
        this.repository = repository;
        this.estimativaService = new EstimativaService();
        this.databaseManager = new DatabaseManager();
        this.localizacaoService = new LocalizacaoService();
    }

    public CorridaService(CorridaRepository repository, EstimativaService estimativaService) {
        this.repository = repository;
        this.estimativaService = estimativaService;
        this.databaseManager = new DatabaseManager();
        this.localizacaoService = new LocalizacaoService();
    }

    public Corrida criarCorrida(String origem, String destino, Categoria categoria,
            int passageiroId, int motoristaId, int veiculoId, double distancia) {

        Optional<Motorista> motoristaProximo = encontrarMotoristaMaisProximo(origem, categoria);

        if (motoristaProximo.isEmpty()) {
            System.out.println("Nenhum motorista disponível encontrado para categoria " + categoria.getNome());
            return null;
        }

        List<Veiculo> veiculosDisponiveis = databaseManager.findVeiculosByCategoria(categoria.getNome());
        if (veiculosDisponiveis.isEmpty()) {
            System.out.println("Nenhum veículo disponível encontrado para categoria " + categoria.getNome());
            return null;
        }

        int motoristaEscolhidoId = motoristaProximo.get().getId();
        int veiculoEscolhidoId = veiculosDisponiveis.get(0).getId();

        System.out.println("Corrida solicitada procurando motorista...");

        Corrida corrida = new Corrida(0, origem, destino, categoria,
                passageiroId, motoristaEscolhidoId, veiculoEscolhidoId, distancia);

        double precoEstimado = estimativaService.estimarPreco(origem, destino, categoria.getNome());
        corrida.setPrecoEstimado(precoEstimado);

        return repository.save(corrida);
    }

    public Optional<Motorista> encontrarMotoristaMaisProximo(String origem, Categoria categoria) {
        return encontrarMotoristaMaisProximo(origem, categoria, -1);
    }

    public Optional<Motorista> encontrarMotoristaMaisProximo(String origem, Categoria categoria,
            int motoristaExcluido) {
        List<Motorista> motoristasDisponiveis = databaseManager.findAllMotoristas()
                .stream()
                .filter(m -> m.isDisponivel() && m.isAtivo())
                .filter(m -> !temCorridaAtiva(m.getId()))
                .filter(m -> categoria.getNome().equalsIgnoreCase(m.getCategoria()))
                .filter(m -> m.getId() != motoristaExcluido)
                .toList();

        if (motoristasDisponiveis.isEmpty()) {
            return Optional.empty();
        }

        Motorista motoristaProximo = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (Motorista motorista : motoristasDisponiveis) {
            String localizacaoMotorista = motorista.getLocalizacaoAtual();

            if (localizacaoService.isLocalizacaoValida(localizacaoMotorista)) {
                int distancia = localizacaoService.calcularDistancia(origem, localizacaoMotorista);

                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    motoristaProximo = motorista;
                }
            }
        }

        return Optional.ofNullable(motoristaProximo);
    }

    public boolean reatribuirCorrida(int corridaId, int motoristaQueRecusou) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);

        if (corridaOpt.isEmpty()) {
            return false;
        }

        Corrida corrida = corridaOpt.get();

        Optional<Motorista> novoMotorista = encontrarMotoristaMaisProximo(
                corrida.getOrigem(),
                corrida.getCategoria(),
                motoristaQueRecusou);

        if (novoMotorista.isPresent()) {
            corrida.setMotoristaId(novoMotorista.get().getId());
            repository.update(corrida);

            System.out.println("🔄 Corrida reatribuída para: " +
                    novoMotorista.get().getNome() + " " + novoMotorista.get().getSobrenome());
            System.out.println("📍 Localização do novo motorista: " + novoMotorista.get().getLocalizacaoAtual());

            return true;
        } else {
            corrida.cancelarCorrida();
            repository.update(corrida);

            Optional<com.uberpb.model.Passageiro> passageiroOpt = databaseManager
                    .findPassageiroById(corrida.getPassageiroId());
            if (passageiroOpt.isPresent()) {
                com.uberpb.model.Passageiro passageiro = passageiroOpt.get();
                passageiro.setEmCorrida(false);
                databaseManager.updatePassageiro(passageiro);
            }

            System.out.println("❌ Nenhum motorista disponível. Corrida cancelada automaticamente.");
            return false;
        }
    }

    public String obterDetalhesReatribuicao(int corridaId, int motoristaQueRecusou) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);

        if (corridaOpt.isEmpty()) {
            return "Corrida não encontrada.";
        }

        Corrida corrida = corridaOpt.get();

        Optional<Motorista> novoMotorista = encontrarMotoristaMaisProximo(
                corrida.getOrigem(),
                corrida.getCategoria(),
                motoristaQueRecusou);

        if (novoMotorista.isPresent()) {
            return String.format("Novo motorista encontrado: %s %s (%s)",
                    novoMotorista.get().getNome(),
                    novoMotorista.get().getSobrenome(),
                    novoMotorista.get().getLocalizacaoAtual());
        } else {
            return "Nenhum motorista disponível na categoria " + corrida.getCategoria().getNome();
        }
    }

    public List<Motorista> listarMotoristasPorProximidade(String origem, Categoria categoria, int motoristaExcluido) {
        return databaseManager.findAllMotoristas()
                .stream()
                .filter(m -> m.isDisponivel() && m.isAtivo())
                .filter(m -> !temCorridaAtiva(m.getId()))
                .filter(m -> categoria.getNome().equalsIgnoreCase(m.getCategoria()))
                .filter(m -> m.getId() != motoristaExcluido)
                .filter(m -> localizacaoService.isLocalizacaoValida(m.getLocalizacaoAtual()))
                .sorted((m1, m2) -> {
                    int dist1 = localizacaoService.calcularDistancia(origem, m1.getLocalizacaoAtual());
                    int dist2 = localizacaoService.calcularDistancia(origem, m2.getLocalizacaoAtual());
                    return Integer.compare(dist1, dist2);
                })
                .toList();
    }

    private boolean temCorridaAtiva(int motoristaId) {
        List<Corrida> corridasMotorista = repository.findByMotoristaId(motoristaId);

        return corridasMotorista.stream()
                .anyMatch(corrida -> corrida.getStatus() != CorridaStatus.FINALIZADA
                        && corrida.getStatus() != CorridaStatus.CANCELADA);
    }

    public void iniciarCorrida(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();

            corrida.iniciarCorrida();
            repository.update(corrida);

            Optional<com.uberpb.model.Passageiro> passageiroOpt = databaseManager
                    .findPassageiroById(corrida.getPassageiroId());
            if (passageiroOpt.isPresent()) {
                com.uberpb.model.Passageiro passageiro = passageiroOpt.get();
                passageiro.setEmCorrida(true);
                databaseManager.updatePassageiro(passageiro);
            }

            Optional<Motorista> motoristaOpt = databaseManager.findMotoristaById(corrida.getMotoristaId());
            if (motoristaOpt.isPresent()) {
                Motorista motorista = motoristaOpt.get();
                motorista.setDisponivel(false);
                databaseManager.updateMotorista(motorista);
            }

            System.out.println("🚀 Corrida iniciada!");
            System.out.println("🚗 Motorista agora está ocupado");
            System.out.println("👤 Passageiro em corrida");
        }
    }

    public void finalizarCorrida(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();

            corrida.setStatus(CorridaStatus.AVALIACAO);
            repository.update(corrida);

            Optional<com.uberpb.model.Passageiro> passageiroOpt = databaseManager
                    .findPassageiroById(corrida.getPassageiroId());
            if (passageiroOpt.isPresent()) {
                com.uberpb.model.Passageiro passageiro = passageiroOpt.get();
                passageiro.setEmCorrida(false);
                databaseManager.updatePassageiro(passageiro);
            }

            Optional<Motorista> motoristaOpt = databaseManager.findMotoristaById(corrida.getMotoristaId());
            if (motoristaOpt.isPresent()) {
                Motorista motorista = motoristaOpt.get();
                motorista.setDisponivel(true);
                databaseManager.updateMotorista(motorista);
            }

            PagamentoService pagamentoService = new PagamentoService();
            Pagamento pagamento = pagamentoService.processarPagamento(
                    corrida.getId(),
                    corrida.getPassageiroId(),
                    corrida.getPrecoEstimado());

            System.out.println("Pagamento realizado: " + pagamento.getStatus());
        }
    }

    public void cancelarCorrida(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();

            corrida.cancelarCorrida();
            repository.update(corrida);

            Optional<com.uberpb.model.Passageiro> passageiroOpt = databaseManager
                    .findPassageiroById(corrida.getPassageiroId());
            if (passageiroOpt.isPresent()) {
                com.uberpb.model.Passageiro passageiro = passageiroOpt.get();
                passageiro.setEmCorrida(false);
                databaseManager.updatePassageiro(passageiro);
            }

            Optional<Motorista> motoristaOpt = databaseManager.findMotoristaById(corrida.getMotoristaId());
            if (motoristaOpt.isPresent()) {
                Motorista motorista = motoristaOpt.get();
                motorista.setDisponivel(true);
                databaseManager.updateMotorista(motorista);
            }

            System.out.println("❌ Corrida cancelada!");
            System.out.println("🚗 Motorista disponível para novas corridas");
            System.out.println("👤 Passageiro liberado para solicitar novas corridas");
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

    public void atualizarTempoRestante(int corridaId) {
        Optional<Corrida> corridaOpt = repository.findById(corridaId);
        if (corridaOpt.isPresent()) {
            Corrida corrida = corridaOpt.get();
            if (corrida.getStatus() == CorridaStatus.EM_ANDAMENTO) {
                int tempoRestanteAtual = corrida.calcularTempoRestanteReal();
                corrida.setTempoRestante(tempoRestanteAtual);
                repository.update(corrida);
            }
        }
    }

    public void finalizarCorridasExpiradas() {
        List<Corrida> corridasEmAndamento = repository.findByStatus(CorridaStatus.EM_ANDAMENTO);

        for (Corrida corrida : corridasEmAndamento) {
            if (corrida.calcularTempoRestanteReal() <= 0 && corrida.getDataHoraAceito() != null) {
                corrida.finalizarCorrida();
                repository.update(corrida);

                Optional<com.uberpb.model.Passageiro> passageiroOpt = databaseManager
                        .findPassageiroById(corrida.getPassageiroId());
                if (passageiroOpt.isPresent()) {
                    com.uberpb.model.Passageiro passageiro = passageiroOpt.get();
                    passageiro.setEmCorrida(false);
                    databaseManager.updatePassageiro(passageiro);
                }

                Optional<Motorista> motoristaOpt = databaseManager.findMotoristaById(corrida.getMotoristaId());
                if (motoristaOpt.isPresent()) {
                    Motorista motorista = motoristaOpt.get();
                    motorista.setDisponivel(true);
                    databaseManager.updateMotorista(motorista);
                }
            }
        }
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

    public void updateCorrida(Corrida corrida) {
        repository.update(corrida);
    }

    public Optional<Corrida> obterCorridaAtivaMotorista(int motoristaId) {
        List<Corrida> corridasMotorista = repository.findByMotoristaId(motoristaId);

        return corridasMotorista.stream()
                .filter(corrida -> corrida.getStatus() != CorridaStatus.FINALIZADA
                        && corrida.getStatus() != CorridaStatus.CANCELADA)
                .findFirst();
    }

    // ✅ Novo método adicionado conforme solicitado
    public List<Corrida> getHistoricoCorridas(String userId) {
        int id = Integer.parseInt(userId);
        List<Corrida> todasCorridas = repository.findAll();
        return todasCorridas.stream()
                .filter(c -> c.getPassageiroId() == id || c.getMotoristaId() == id)
                .sorted((c1, c2) -> c2.getDataHoraSolicitacao().compareTo(c1.getDataHoraSolicitacao()))
                .collect(Collectors.toList());
    }
}
