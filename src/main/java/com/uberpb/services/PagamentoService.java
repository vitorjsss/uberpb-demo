package com.uberpb.services;

import com.uberpb.enums.StatusPagamento;
import com.uberpb.enums.MetodoPagamento;
import com.uberpb.model.pagamento.Pagamento;
import com.uberpb.repository.json.PagamentoRepositoryJSON;

import java.time.LocalDateTime;

public class PagamentoService {

    private PagamentoRepositoryJSON pagamentoRepo = new PagamentoRepositoryJSON();

    public Pagamento processarPagamento(int corridaId, int passageiroId, double valor,
            MetodoPagamento tipoPagamento, int metodoPagamentoId) {
        // Usar o construtor completo
        Pagamento pagamento = new Pagamento(corridaId, passageiroId, valor, tipoPagamento, metodoPagamentoId);

        pagamentoRepo.salvar(pagamento);

        // Simulação: aprova automaticamente
        pagamento.setStatus(StatusPagamento.SUCESSO);
        pagamento.setAtualizadoEm(LocalDateTime.now());
        pagamentoRepo.atualizar(pagamento);

        return pagamento;
    }

    // Método de conveniência para manter compatibilidade
    public Pagamento processarPagamento(int corridaId, int passageiroId, double valor) {
        return processarPagamento(corridaId, passageiroId, valor, MetodoPagamento.PIX, 0);
    }
}