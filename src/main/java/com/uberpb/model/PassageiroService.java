package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;

public class PassageiroService {
    private List<Passageiro> passageiros = new ArrayList<>();

    public void cadastrar(Passageiro p) throws Exception {
        if (passageiros.contains(p)) {
            throw new Exception("Passageiro com este e-mail já existe!");
        }
        passageiros.add(p);
    }

    public Passageiro buscarPorEmail(String email) {
        return passageiros.stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public List<Passageiro> listar() {
        return new ArrayList<>(passageiros);
    }
}
