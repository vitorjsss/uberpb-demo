package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;

public class MotoristaService {

    private List<Motorista> motoristas = new ArrayList<>();

    // Cadastrar novo motorista
    public void cadastrar(Motorista m) throws Exception {
        if (buscarPorEmail(m.getEmail()) != null) {
            throw new Exception("Já existe um motorista com este e-mail!");
        }
        motoristas.add(m);
    }

    // Buscar motorista por e-mail
    public Motorista buscarPorEmail(String email) {
        return motoristas.stream()
                .filter(m -> m.getEmail() != null && m.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Buscar motorista por ID
    public Motorista buscarPorId(int id) {
        return motoristas.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Atualizar dados do motorista
    public void atualizar(Motorista motoristaAtualizado) throws Exception {
        Motorista existente = buscarPorId(motoristaAtualizado.getId());
        if (existente == null) {
            throw new Exception("Motorista não encontrado!");
        }

        // Atualiza apenas campos editáveis
        existente.setAtivo(motoristaAtualizado.isAtivo());
        existente.setDisponivel(motoristaAtualizado.isDisponivel());
        existente.setLocalizacaoAtual(motoristaAtualizado.getLocalizacaoAtual());
        existente.setAvaliacaoMedia(motoristaAtualizado.getAvaliacaoMedia());
        existente.setTotalAvaliacoes(motoristaAtualizado.getTotalAvaliacoes());
        existente.setCnh(motoristaAtualizado.getCnh());
        existente.setValidadeCnh(motoristaAtualizado.getValidadeCnh());
    }

    // Remover motorista
    public void remover(int id) throws Exception {
        Motorista motorista = buscarPorId(id);
        if (motorista == null) {
            throw new Exception("Motorista não encontrado!");
        }
        motoristas.remove(motorista);
    }

    // Listar todos os motoristas
    public List<Motorista> listar() {
        return new ArrayList<>(motoristas);
    }
}
