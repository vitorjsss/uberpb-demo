package com.uberpb.model.test;

import com.uberpb.enums.StatusPagamento;
import com.uberpb.enums.MetodoPagamento;
import com.uberpb.model.pagamento.Pagamento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagamentoUnitTest {

    @Test
    void deveCriarPagamentoComStatusPendente() {
        Pagamento pagamento = new Pagamento(10, 5, 75.50, MetodoPagamento.PIX, 123);

        assertEquals(10, pagamento.getCorridaId());
        assertEquals(5, pagamento.getPassageiroId());
        assertEquals(75.50, pagamento.getValor());
        assertEquals(MetodoPagamento.PIX, pagamento.getTipoPagamento());
        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
        assertNotNull(pagamento.getCriadoEm());
    }

    @Test
    void deveAtualizarStatusParaSucesso() {
        Pagamento pagamento = new Pagamento(1, 1, 100.0, MetodoPagamento.CARTAO, 456);
        pagamento.setStatus(StatusPagamento.SUCESSO);

        assertEquals(StatusPagamento.SUCESSO, pagamento.getStatus());
    }

    @Test
    void deveGerarToStringComCamposValidos() {
        Pagamento pagamento = new Pagamento(2, 3, 200.0, MetodoPagamento.PAYPAL, 999);
        String texto = pagamento.toString();

        assertTrue(texto.contains("Pagamento ID"));
        assertTrue(texto.contains("PAYPAL"));
    }
}
