package com.uberpb.sevices;

import com.uberpb.model.Pagamento;
import com.uberpb.model.StatusPagamento;
import com.uberpb.repository.json.PagamentoRepositoryJSON;

import java.time.LocalDateTime;
import java.util.UUID;

public class PagamentoService {

    private PagamentoRepositoryJSON pagamentoRepo = new PagamentoRepositoryJSON();

    public Pagamento processarPagamento(int corridaId, int passageiroId, double valor) {
        Pagamento pagamento = new Pagamento();
        pagamento.setCorridaId(corridaId);
        pagamento.setPassageiroId(passageiroId);
        pagamento.setValor(valor);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setChaveIdempotencia(UUID.randomUUID().toString());
        pagamento.setCriadoEm(LocalDateTime.now());

        pagamentoRepo.salvar(pagamento);

        // Simulação: aprova automaticamente
        pagamento.setStatus(StatusPagamento.SUCESSO);
        pagamento.setAtualizadoEm(LocalDateTime.now());
        pagamentoRepo.atualizar(pagamento);

        return pagamento;
    }
}
