package com.uberpb.sevices;

import com.uberpb.model.User;
import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;

public class AuthService {
    private final PassageiroService passageiroService;
    private final MotoristaService motoristaService;

    // Construtor que recebe as dependências
    public AuthService(PassageiroService passageiroService, MotoristaService motoristaService) {
        this.passageiroService = passageiroService;
        this.motoristaService = motoristaService;
    }

    /**
     * Autentica um usuário com base no email e senha
     * @param email email do usuário
     * @param senha senha do usuário
     * @return User se autenticado, null se falhar
     */
    public User autenticar(String email, String senha) {
        // Primeiro tenta autenticar como passageiro
        Passageiro passageiro = autenticarPassageiro(email, senha);
        if (passageiro != null) {
            return passageiro;
        }

        // Se não encontrou passageiro, tenta como motorista
        return autenticarMotorista(email, senha);
    }

    private Passageiro autenticarPassageiro(String email, String senha) {
        for (Passageiro passageiro : passageiroService.listar()) {
            if (passageiro.getEmail().equalsIgnoreCase(email) && 
                passageiro.getSenha().equals(senha)) {
                return passageiro;
            }
        }
        return null;
    }

    private Motorista autenticarMotorista(String email, String senha) {
        for (Motorista motorista : motoristaService.listar()) {
            if (motorista.getEmail().equalsIgnoreCase(email) && 
                motorista.getSenha().equals(senha)) {
                return motorista;
            }
        }
        return null;
    }

    /**
     * Retorna o tipo do usuário (PASSAGEIRO ou MOTORISTA)
     * @param usuario usuário a ser verificado
     * @return String com o tipo do usuário
     */
    public String getTipoUsuario(User usuario) {
        if (usuario instanceof Passageiro) {
            return "PASSAGEIRO";
        } else if (usuario instanceof Motorista) {
            return "MOTORISTA";
        }
        return "DESCONHECIDO";
    }
}