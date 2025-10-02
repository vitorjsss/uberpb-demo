package com.uberpb.repository.json;

import com.uberpb.model.pagamento.Cartao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartaoRepositoryJSON {
    private static final String ARQUIVO_CARTOES = "database/pagamentos/cartoes.json";
    private final ObjectMapper objectMapper;
    private List<Cartao> cartoes;
    private int proximoId = 1;

    public CartaoRepositoryJSON() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.cartoes = new ArrayList<>();
        carregarCartoes();
    }

    private void carregarCartoes() {
        carregarCartoesDoDisco();
    }

    public void recarregar() {
        carregarCartoesDoDisco();
    }

    private void carregarCartoesDoDisco() {
        try {
            File arquivo = new File(ARQUIVO_CARTOES);
            if (arquivo.exists()) {
                cartoes = objectMapper.readValue(arquivo, new TypeReference<List<Cartao>>() {
                });
                if (!cartoes.isEmpty()) {
                    proximoId = cartoes.stream().mapToInt(Cartao::getId).max().orElse(0) + 1;
                }
            } else {
                // Criar diretório se não existir
                arquivo.getParentFile().mkdirs();
                salvarArquivo();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar cartões: " + e.getMessage());
            cartoes = new ArrayList<>();
        }
    }

    private void salvarArquivo() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ARQUIVO_CARTOES), cartoes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar cartões: " + e.getMessage());
        }
    }

    public Cartao salvar(Cartao cartao) {
        if (cartao.getId() == 0) {
            cartao.setId(proximoId++);
            cartoes.add(cartao);
        } else {
            for (int i = 0; i < cartoes.size(); i++) {
                if (cartoes.get(i).getId() == cartao.getId()) {
                    cartoes.set(i, cartao);
                    break;
                }
            }
        }
        salvarArquivo();
        return cartao;
    }

    public List<Cartao> listarPorUsuario(int usuarioId) {
        recarregar(); // Garantir dados atualizados
        return cartoes.stream()
                .filter(c -> c.getUsuarioId() == usuarioId && c.isAtivo())
                .toList();
    }

    public Optional<Cartao> buscarPorId(int id) {
        return cartoes.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    public void remover(int id) {
        cartoes.removeIf(c -> c.getId() == id);
        salvarArquivo();
    }

    public void desativar(int id) {
        buscarPorId(id).ifPresent(cartao -> {
            cartao.setAtivo(false);
            salvar(cartao);
        });
    }

    public List<Cartao> listarTodos() {
        return new ArrayList<>(cartoes);
    }
}