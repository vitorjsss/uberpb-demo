package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;

public class PassageiroService {

    private List<Passageiro> passageiros = new ArrayList<>();

    // Cadastrar novo passageiro
    public void cadastrar(Passageiro p) throws Exception {
        if (buscarPorEmail(p.getEmail()) != null) {
            throw new Exception("Já existe um passageiro com este e-mail!");
        }
        passageiros.add(p);
    }

    // Buscar passageiro por e-mail
    public Passageiro buscarPorEmail(String email) {
        return passageiros.stream()
                .filter(p -> p.getEmail() != null && p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Buscar passageiro por ID
    public Passageiro buscarPorId(int id) {
        return passageiros.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Atualizar dados do passageiro
    public void atualizar(Passageiro passageiroAtualizado) throws Exception {
        Passageiro existente = buscarPorId(passageiroAtualizado.getId());
        if (existente == null) {
            throw new Exception("Passageiro não encontrado!");
        }

        // Atualiza apenas campos editáveis
        existente.setLocalizacaoAtual(passageiroAtualizado.getLocalizacaoAtual());
        existente.setEmCorrida(passageiroAtualizado.isEmCorrida());
        existente.setMetodosPagamento(passageiroAtualizado.getMetodosPagamento());
        existente.setAvaliacaoMedia(passageiroAtualizado.getAvaliacaoMedia());
    }

    // Remover passageiro
    public void remover(int id) throws Exception {
        Passageiro passageiro = buscarPorId(id);
        if (passageiro == null) {
            throw new Exception("Passageiro não encontrado!");
        }
        passageiros.remove(passageiro);
    }

    // Listar todos os passageiros
    public List<Passageiro> listar() {
        return new ArrayList<>(passageiros);
    }
}
