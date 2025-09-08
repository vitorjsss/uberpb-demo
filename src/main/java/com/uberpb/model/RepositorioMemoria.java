package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;

public class RepositorioMemoria {

    private List<User> users = new ArrayList<>();
    private List<Passageiro> passageiros = new ArrayList<>();
    private List<Motorista> motoristas = new ArrayList<>();

    // ===== USERS =====
    public void salvar(User u) {
        users.add(u);
        System.out.println("User salvo: " + u.getNome() + " (" + u.getEmail() + ")");
    }

    public User buscarPorEmailSenha(String email, String senha) {
        return users.stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)
                        && u.getSenha().equals(senha))
                .findFirst()
                .orElse(null);
    }

    // ===== ASSOCIAR PERFIS =====
    public void associarPassageiro(User u) {
        if (getPassageiro(u) != null) {
            System.out.println("Você já é um passageiro.");
            return;
        }

        // Cria Passageiro usando os dados do User
        Passageiro p = new Passageiro(
                u.getId(),           // id
                "Nao definida",      // localizacao inicial
                false                // emCorrida
        );
        p.setUsername(u.getUsername());
        p.setSenha(u.getSenha());
        p.setNome(u.getNome());
        p.setSobrenome(u.getSobrenome());
        p.setEmail(u.getEmail());
        p.setTelefone(u.getTelefone());

        passageiros.add(p);
        u.setTipo("passageiro");
        System.out.println("Perfil Passageiro cadastrado com sucesso!");
    }

    public void associarMotorista(User u, String cnh, String validade) {
        if (getMotorista(u) != null) {
            System.out.println("Você já é um motorista.");
            return;
        }

        // Cria Motorista usando os dados do User
        Motorista m = new Motorista(
                u.getId(),      // id
                true,           // ativo
                cnh,            // CNH
                validade,       // validade CNH
                0.0,            // avaliacaoMedia
                0,              // totalAvaliacoes
                true,           // disponivel
                "Nao definida"  // localizacaoAtual
        );
        m.setUsername(u.getUsername());
        m.setSenha(u.getSenha());
        m.setNome(u.getNome());
        m.setSobrenome(u.getSobrenome());
        m.setEmail(u.getEmail());
        m.setTelefone(u.getTelefone());

        motoristas.add(m);
        u.setTipo("motorista");
        System.out.println("Perfil Motorista cadastrado com sucesso!");
    }

    // ===== RECUPERAR PERFIS =====
    public Passageiro getPassageiro(User u) {
        return passageiros.stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public Motorista getMotorista(User u) {
        return motoristas.stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
