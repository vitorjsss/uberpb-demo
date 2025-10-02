package com.uberpb.services;

import com.uberpb.model.pagamento.Cartao;
import com.uberpb.model.pagamento.PIX;
import com.uberpb.model.pagamento.PayPal;
import com.uberpb.repository.json.CartaoRepositoryJSON;
import com.uberpb.repository.json.PIXRepositoryJSON;
import com.uberpb.repository.json.PayPalRepositoryJSON;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciar métodos de pagamento dos usuários.
 */
public class MetodoPagamentoService {
    private final CartaoRepositoryJSON cartaoRepo;
    private final PIXRepositoryJSON pixRepo;
    private final PayPalRepositoryJSON paypalRepo;

    public MetodoPagamentoService() {
        this.cartaoRepo = new CartaoRepositoryJSON();
        this.pixRepo = new PIXRepositoryJSON();
        this.paypalRepo = new PayPalRepositoryJSON();
    }

    // === CARTÃO ===
    public Cartao salvarCartao(Cartao cartao) {
        if (!cartao.isValido()) {
            throw new IllegalArgumentException("Cartão com dados inválidos!");
        }
        return cartaoRepo.salvar(cartao);
    }

    public List<Cartao> listarCartoesPorUsuario(int usuarioId) {
        return cartaoRepo.listarPorUsuario(usuarioId);
    }

    public Optional<Cartao> buscarCartaoPorId(int id) {
        return cartaoRepo.buscarPorId(id);
    }

    public void removerCartao(int id) {
        cartaoRepo.remover(id);
    }

    public void desativarCartao(int id) {
        cartaoRepo.desativar(id);
    }

    // === PIX ===
    public PIX salvarPIX(PIX pix) {
        if (!pix.isValido()) {
            throw new IllegalArgumentException("PIX com dados inválidos!");
        }

        // Verificar se a chave já existe para outro usuário
        if (pixRepo.chaveJaExiste(pix.getChave(), pix.getUsuarioId())) {
            throw new IllegalArgumentException("Esta chave PIX já está cadastrada para outro usuário!");
        }

        return pixRepo.salvar(pix);
    }

    public List<PIX> listarPIXPorUsuario(int usuarioId) {
        return pixRepo.listarPorUsuario(usuarioId);
    }

    public Optional<PIX> buscarPIXPorId(int id) {
        return pixRepo.buscarPorId(id);
    }

    public void removerPIX(int id) {
        pixRepo.remover(id);
    }

    public void desativarPIX(int id) {
        pixRepo.desativar(id);
    }

    // === PAYPAL ===
    public PayPal salvarPayPal(PayPal paypal) {
        if (!paypal.isValido()) {
            throw new IllegalArgumentException("PayPal com dados inválidos!");
        }

        // Verificar se o email já existe para outro usuário
        if (paypalRepo.emailJaExiste(paypal.getEmail(), paypal.getUsuarioId())) {
            throw new IllegalArgumentException("Este email PayPal já está cadastrado para outro usuário!");
        }

        return paypalRepo.salvar(paypal);
    }

    public List<PayPal> listarPayPalPorUsuario(int usuarioId) {
        return paypalRepo.listarPorUsuario(usuarioId);
    }

    public Optional<PayPal> buscarPayPalPorId(int id) {
        return paypalRepo.buscarPorId(id);
    }

    public void removerPayPal(int id) {
        paypalRepo.remover(id);
    }

    public void desativarPayPal(int id) {
        paypalRepo.desativar(id);
    }

    // === UTILITÁRIOS ===

    /**
     * Conta total de métodos de pagamento ativos do usuário
     */
    public int contarMetodosPagamento(int usuarioId) {
        return listarCartoesPorUsuario(usuarioId).size() +
                listarPIXPorUsuario(usuarioId).size() +
                listarPayPalPorUsuario(usuarioId).size();
    }

    /**
     * Verifica se o usuário tem pelo menos um método de pagamento
     */
    public boolean usuarioTemMetodosPagamento(int usuarioId) {
        return contarMetodosPagamento(usuarioId) > 0;
    }
}