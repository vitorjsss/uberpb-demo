package com.uberpb.services;

import com.uberpb.model.Corrida;
import com.uberpb.model.Passageiro;
import com.uberpb.model.Motorista;
import com.uberpb.model.Veiculo;
import com.uberpb.model.ReciboInfo;
import com.uberpb.model.pagamento.Pagamento;
import com.uberpb.repository.json.CorridaRepositoryJSON;
import com.uberpb.repository.json.PassageiroRepositoryJSON;
import com.uberpb.repository.json.MotoristaRepositoryJSON;
import com.uberpb.repository.json.VeiculoRepositoryJSON;
import com.uberpb.repository.json.PagamentoRepositoryJSON;

import java.util.Optional;

/**
 * Service responsável por gerar recibos de corridas
 * Agrega informações de corrida, pagamento, passageiro, motorista e veículo
 */
public class ReciboService {
    
    private final CorridaRepositoryJSON corridaRepository;
    private final PassageiroRepositoryJSON passageiroRepository;
    private final MotoristaRepositoryJSON motoristaRepository;
    private final VeiculoRepositoryJSON veiculoRepository;
    private final PagamentoRepositoryJSON pagamentoRepository;
    
    public ReciboService() {
        this.corridaRepository = new CorridaRepositoryJSON();
        this.passageiroRepository = new PassageiroRepositoryJSON();
        this.motoristaRepository = new MotoristaRepositoryJSON();
        this.veiculoRepository = new VeiculoRepositoryJSON();
        this.pagamentoRepository = new PagamentoRepositoryJSON();
    }
    
    /**
     * Gera o recibo completo para uma corrida específica
     * 
     * @param corridaId ID da corrida para gerar o recibo
     * @return ReciboInfo com todas as informações ou null se não encontrar dados suficientes
     */
    public ReciboInfo gerarRecibo(int corridaId) {
        try {
            // Buscar informações da corrida
            Optional<Corrida> corridaOpt = corridaRepository.findById(corridaId);
            if (corridaOpt.isEmpty()) {
                System.err.println("Corrida não encontrada com ID: " + corridaId);
                return null;
            }
            
            Corrida corrida = corridaOpt.get();
            
            // Buscar informações do passageiro
            Optional<Passageiro> passageiroOpt = passageiroRepository.findById(corrida.getPassageiroId());
            if (passageiroOpt.isEmpty()) {
                System.err.println("Passageiro não encontrado com ID: " + corrida.getPassageiroId());
                return null;
            }
            
            Passageiro passageiro = passageiroOpt.get();
            
            // Buscar informações do motorista
            Optional<Motorista> motoristaOpt = motoristaRepository.findById(corrida.getMotoristaId());
            if (motoristaOpt.isEmpty()) {
                System.err.println("Motorista não encontrado com ID: " + corrida.getMotoristaId());
                return null;
            }
            
            Motorista motorista = motoristaOpt.get();
            
            // Buscar informações do veículo
            Optional<Veiculo> veiculoOpt = veiculoRepository.findById(corrida.getVeiculoId());
            if (veiculoOpt.isEmpty()) {
                System.err.println("Veículo não encontrado com ID: " + corrida.getVeiculoId());
                return null;
            }
            
            Veiculo veiculo = veiculoOpt.get();
            
            // Buscar informações do pagamento
            Optional<Pagamento> pagamentoOpt = buscarPagamentoPorCorrida(corridaId);
            
            // Criar o ReciboInfo com todas as informações
            ReciboInfo recibo = new ReciboInfo();
            
            // Informações da corrida
            recibo.setCorridaId(corrida.getId());
            recibo.setOrigem(corrida.getOrigem());
            recibo.setDestino(corrida.getDestino());
            recibo.setCategoria(corrida.getCategoria() != null ? corrida.getCategoria().toString() : "Não definida");
            recibo.setDistancia(corrida.getDistancia());
            recibo.setDataHoraSolicitacao(corrida.getDataHoraSolicitacao());
            recibo.setDataHoraFim(corrida.getDataHoraFim());
            recibo.setPrecoFinal(corrida.getPrecoEstimado());
            
            // Informações do passageiro
            recibo.setNomePassageiro(passageiro.getNome() != null ? passageiro.getNome() : "Não informado");
            recibo.setEmailPassageiro(passageiro.getEmail() != null ? passageiro.getEmail() : "Não informado");
            
            // Informações do motorista
            recibo.setNomeMotorista(motorista.getNome() != null ? motorista.getNome() : "Não informado");
            recibo.setCnhMotorista(motorista.getCnh() != null ? motorista.getCnh() : "Não informado");
            recibo.setAvaliacaoMotorista(motorista.getAvaliacaoMedia());
            
            // Informações do veículo
            recibo.setModeloVeiculo(veiculo.getModelo() != null ? veiculo.getModelo() : "Não informado");
            recibo.setMarcaVeiculo(veiculo.getMarca() != null ? veiculo.getMarca() : "Não informado");
            recibo.setPlacaVeiculo(veiculo.getPlaca() != null ? veiculo.getPlaca() : "Não informado");
            recibo.setCorVeiculo(veiculo.getCor() != null ? veiculo.getCor() : "Não informado");
            
            // Informações do pagamento
            if (pagamentoOpt.isPresent()) {
                Pagamento pagamento = pagamentoOpt.get();
                recibo.setMetodoPagamento(pagamento.getTipoPagamento() != null ? 
                    pagamento.getTipoPagamento().toString() : "Não informado");
                recibo.setStatusPagamento(pagamento.getStatus() != null ? 
                    pagamento.getStatus().toString() : "Pendente");
                recibo.setDataPagamento(pagamento.getAtualizadoEm());
                
                // Atualizar preço final com o valor do pagamento se disponível
                if (pagamento.getValor() > 0) {
                    recibo.setPrecoFinal(pagamento.getValor());
                }
            } else {
                recibo.setMetodoPagamento("Não processado");
                recibo.setStatusPagamento("Pendente");
                recibo.setDataPagamento(null);
            }
            
            return recibo;
            
        } catch (Exception e) {
            System.err.println("Erro ao gerar recibo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca o pagamento relacionado a uma corrida específica
     * 
     * @param corridaId ID da corrida
     * @return Optional contendo o pagamento se encontrado
     */
    private Optional<Pagamento> buscarPagamentoPorCorrida(int corridaId) {
        try {
            return pagamentoRepository.listarTodos().stream()
                .filter(pagamento -> pagamento.getCorridaId() == corridaId)
                .findFirst();
        } catch (Exception e) {
            System.err.println("Erro ao buscar pagamento para corrida " + corridaId + ": " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Gera e exibe o recibo formatado no console
     * 
     * @param corridaId ID da corrida
     * @return true se o recibo foi gerado com sucesso, false caso contrário
     */
    public boolean exibirRecibo(int corridaId) {
        ReciboInfo recibo = gerarRecibo(corridaId);
        
        if (recibo != null) {
            System.out.println(recibo.gerarReciboFormatado());
            return true;
        } else {
            System.out.println("Não foi possível gerar o recibo para a corrida ID: " + corridaId);
            return false;
        }
    }
    
    /**
     * Gera o recibo e salva em arquivo de texto
     * 
     * @param corridaId ID da corrida
     * @param nomeArquivo nome do arquivo para salvar (opcional)
     * @return true se salvou com sucesso, false caso contrário
     */
    public boolean salvarReciboEmArquivo(int corridaId, String nomeArquivo) {
        ReciboInfo recibo = gerarRecibo(corridaId);
        
        if (recibo == null) {
            return false;
        }
        
        try {
            String nomeArquivoFinal = nomeArquivo != null ? nomeArquivo : 
                "recibo_corrida_" + corridaId + ".txt";
            
            java.nio.file.Path path = java.nio.file.Paths.get(nomeArquivoFinal);
            java.nio.file.Files.write(path, recibo.gerarReciboFormatado().getBytes());
            
            System.out.println("Recibo salvo em: " + path.toAbsolutePath());
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar recibo em arquivo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lista todas as corridas finalizadas do passageiro para gerar recibos
     * 
     * @param passageiroId ID do passageiro
     * @return lista de corridas finalizadas
     */
    public java.util.List<Corrida> listarCorridasFinalizadasDoPassageiro(int passageiroId) {
        try {
            return corridaRepository.findAll().stream()
                .filter(corrida -> corrida.getPassageiroId() == passageiroId)
                .filter(corrida -> corrida.getStatus() == com.uberpb.enums.CorridaStatus.FINALIZADA)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erro ao buscar corridas finalizadas: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * Lista todas as corridas finalizadas do motorista para gerar recibos
     * 
     * @param motoristaId ID do motorista
     * @return lista de corridas finalizadas
     */
    public java.util.List<Corrida> listarCorridasFinalizadasDoMotorista(int motoristaId) {
        try {
            return corridaRepository.findAll().stream()
                .filter(corrida -> corrida.getMotoristaId() == motoristaId)
                .filter(corrida -> corrida.getStatus() == com.uberpb.enums.CorridaStatus.FINALIZADA)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erro ao buscar corridas finalizadas: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}