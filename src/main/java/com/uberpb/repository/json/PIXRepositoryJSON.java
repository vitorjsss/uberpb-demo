package com.uberpb.repository.json;

import com.uberpb.model.pagamento.PIX;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PIXRepositoryJSON {
    private static final String ARQUIVO_PIX = "database/pagamentos/pix.json";
    private final ObjectMapper objectMapper;
    private List<PIX> pixList;
    private int proximoId = 1;

    public PIXRepositoryJSON() {
        this.objectMapper = new ObjectMapper();
        this.pixList = new ArrayList<>();
        carregarPIX();
    }

    private void carregarPIX() {
        carregarPIXDoDisco();
    }

    public void recarregar() {
        carregarPIXDoDisco();
    }

    private void carregarPIXDoDisco() {
        try {
            File arquivo = new File(ARQUIVO_PIX);
            if (arquivo.exists()) {
                pixList = objectMapper.readValue(arquivo, new TypeReference<List<PIX>>() {
                });
                if (!pixList.isEmpty()) {
                    proximoId = pixList.stream().mapToInt(PIX::getId).max().orElse(0) + 1;
                }
            } else {
                // Criar diretório se não existir
                arquivo.getParentFile().mkdirs();
                salvarArquivo();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar PIX: " + e.getMessage());
            pixList = new ArrayList<>();
        }
    }

    private void salvarArquivo() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ARQUIVO_PIX), pixList);
        } catch (IOException e) {
            System.err.println("Erro ao salvar PIX: " + e.getMessage());
        }
    }

    public PIX salvar(PIX pix) {
        if (pix.getId() == 0) {
            pix.setId(proximoId++);
            pixList.add(pix);
        } else {
            for (int i = 0; i < pixList.size(); i++) {
                if (pixList.get(i).getId() == pix.getId()) {
                    pixList.set(i, pix);
                    break;
                }
            }
        }
        salvarArquivo();
        return pix;
    }

    public List<PIX> listarPorUsuario(int usuarioId) {
        recarregar(); // Garantir dados atualizados
        return pixList.stream()
                .filter(p -> p.getUsuarioId() == usuarioId && p.isAtivo())
                .toList();
    }

    public Optional<PIX> buscarPorId(int id) {
        return pixList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public boolean chaveJaExiste(String chave, int usuarioId) {
        return pixList.stream()
                .anyMatch(p -> p.getChave().equals(chave) && p.getUsuarioId() != usuarioId && p.isAtivo());
    }

    public void remover(int id) {
        pixList.removeIf(p -> p.getId() == id);
        salvarArquivo();
    }

    public void desativar(int id) {
        buscarPorId(id).ifPresent(pix -> {
            pix.setAtivo(false);
            salvar(pix);
        });
    }

    public List<PIX> listarTodos() {
        return new ArrayList<>(pixList);
    }
}