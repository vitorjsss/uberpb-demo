package com.uberpb.repository.json;

import com.uberpb.model.pagamento.PayPal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PayPalRepositoryJSON {
    private static final String ARQUIVO_PAYPAL = "database/pagamentos/paypal.json";
    private final ObjectMapper objectMapper;
    private List<PayPal> paypalList;
    private int proximoId = 1;

    public PayPalRepositoryJSON() {
        this.objectMapper = new ObjectMapper();
        this.paypalList = new ArrayList<>();
        carregarPayPal();
    }

    private void carregarPayPal() {
        carregarPayPalDoDisco();
    }

    public void recarregar() {
        carregarPayPalDoDisco();
    }

    private void carregarPayPalDoDisco() {
        try {
            File arquivo = new File(ARQUIVO_PAYPAL);
            if (arquivo.exists()) {
                paypalList = objectMapper.readValue(arquivo, new TypeReference<List<PayPal>>() {
                });
                if (!paypalList.isEmpty()) {
                    proximoId = paypalList.stream().mapToInt(PayPal::getId).max().orElse(0) + 1;
                }
            } else {
                // Criar diretório se não existir
                arquivo.getParentFile().mkdirs();
                salvarArquivo();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar PayPal: " + e.getMessage());
            paypalList = new ArrayList<>();
        }
    }

    private void salvarArquivo() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ARQUIVO_PAYPAL), paypalList);
        } catch (IOException e) {
            System.err.println("Erro ao salvar PayPal: " + e.getMessage());
        }
    }

    public PayPal salvar(PayPal paypal) {
        if (paypal.getId() == 0) {
            paypal.setId(proximoId++);
            paypalList.add(paypal);
        } else {
            for (int i = 0; i < paypalList.size(); i++) {
                if (paypalList.get(i).getId() == paypal.getId()) {
                    paypalList.set(i, paypal);
                    break;
                }
            }
        }
        salvarArquivo();
        return paypal;
    }

    public List<PayPal> listarPorUsuario(int usuarioId) {
        recarregar(); // Garantir dados atualizados
        return paypalList.stream()
                .filter(p -> p.getUsuarioId() == usuarioId && p.isAtivo())
                .toList();
    }

    public Optional<PayPal> buscarPorId(int id) {
        return paypalList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public boolean emailJaExiste(String email, int usuarioId) {
        return paypalList.stream()
                .anyMatch(p -> p.getEmail().equals(email) && p.getUsuarioId() != usuarioId && p.isAtivo());
    }

    public void remover(int id) {
        paypalList.removeIf(p -> p.getId() == id);
        salvarArquivo();
    }

    public void desativar(int id) {
        buscarPorId(id).ifPresent(paypal -> {
            paypal.setAtivo(false);
            salvar(paypal);
        });
    }

    public List<PayPal> listarTodos() {
        return new ArrayList<>(paypalList);
    }
}