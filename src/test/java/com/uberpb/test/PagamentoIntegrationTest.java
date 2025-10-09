package com.uberpb.test;

import com.uberpb.enums.MetodoPagamento;
import com.uberpb.enums.StatusPagamento;
import com.uberpb.model.pagamento.Pagamento;
import com.uberpb.services.PagamentoService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagamentoIntegrationTest {

    @Test
    void deveProcessarPagamentoPixComSucesso() {
        PagamentoService service = new PagamentoService();

        Pagamento pagamento = service.processarPagamento(
                1001, 501, 89.90, MetodoPagamento.PIX, 0);

        assertNotNull(pagamento);
        assertEquals(StatusPagamento.SUCESSO, pagamento.getStatus());
        assertEquals(MetodoPagamento.PIX, pagamento.getTipoPagamento());
        assertTrue(pagamento.getValor() > 0);
        assertNotNull(pagamento.getAtualizadoEm());
    }

    @Test
    void deveProcessarPagamentoCartao() {
        PagamentoService service = new PagamentoService();

        Pagamento pagamento = service.processarPagamento(
                1002, 777, 250.00, MetodoPagamento.CARTAO_CREDITO, 1234);

        assertEquals(MetodoPagamento.CARTAO_CREDITO, pagamento.getTipoPagamento());
        assertEquals(StatusPagamento.SUCESSO, pagamento.getStatus());
    }
}
