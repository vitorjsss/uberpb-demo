package com.uberpb.cli.menus;

import com.uberpb.cli.forms.CadastroVeiculoCLI;
import com.uberpb.model.Motorista;
import com.uberpb.repository.DatabaseManager;
import java.util.Scanner;

public class MenuMotoristaCLI {
    private final Scanner sc;
    private final DatabaseManager db;
    private final Motorista motorista;

    public MenuMotoristaCLI(Scanner sc, DatabaseManager db, Motorista motorista) {
        this.sc = sc;
        this.db = db;
        this.motorista = motorista;
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Motorista ===");
            System.out.println("1 - Ver status ativo");
            System.out.println("2 - Ver avaliacao media");
            System.out.println("3 - Ver total de avaliacoes");
            System.out.println("4 - Ver localizacao atual");
            System.out.println("5 - Atualizar localizacao");
            System.out.println("6 - Ver informacoes do perfil");
            System.out.println("7 - Ver CNH e validade");
            System.out.println("8 - Ver status disponibilidade");
            System.out.println("9 - Cadastrar veículo");
            System.out.println("10 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    verStatusAtivo();
                    break;
                case 2:
                    verAvaliacaoMedia();
                    break;
                case 3:
                    verTotalAvaliacoes();
                    break;
                case 4:
                    verLocalizacaoAtual();
                    break;
                case 5:
                    atualizarLocalizacao();
                    break;
                case 6:
                    verInformacoesPerfil();
                    break;
                case 7:
                    verCnhValidade();
                    break;
                case 8:
                    verStatusDisponibilidade();
                    break;
                case 9:
                    CadastroVeiculoCLI.cadastrarVeiculo(sc, db, motorista);
                    break;
                case 10:
                    return;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        }
    }

    private void verStatusAtivo() {
        System.out.println("\n--- Status Ativo ---");
        System.out.println("Status: " + (motorista.isAtivo() ? "Ativo" : "Inativo"));
    }

    private void verAvaliacaoMedia() {
        System.out.println("\n--- Avaliação Média ---");
        System.out.println("Avaliação média: " + motorista.getAvaliacaoMedia() + " ⭐");
    }

    private void verTotalAvaliacoes() {
        System.out.println("\n--- Total de Avaliações ---");
        System.out.println("Total de avaliações: " + motorista.getTotalAvaliacoes());
    }

    private void verLocalizacaoAtual() {
        System.out.println("\n--- Localização Atual ---");
        System.out.println("Localização: " + motorista.getLocalizacaoAtual());
    }

    private void atualizarLocalizacao() {
        System.out.println("\n--- Atualizar Localização ---");
        System.out.print("Digite a nova localização: ");
        String localizacao = sc.nextLine();
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            motorista.setLocalizacaoAtual(localizacao.trim());
            db.updateMotorista(motorista);
            System.out.println("✓ Localização atualizada com sucesso!");
            System.out.println("Dados atualizados em: database/motoristas/motoristas.json");
        } else {
            System.out.println("ERRO: Localização não pode estar vazia!");
        }
    }

    private void verInformacoesPerfil() {
        System.out.println("\n--- Informações do Perfil ---");
        System.out.println("Nome: " + motorista.getNome() + " " + motorista.getSobrenome());
        System.out.println("Email: " + motorista.getEmail());
        System.out.println("Telefone: " + motorista.getTelefone());
        System.out.println("CNH: " + motorista.getCnh());
        System.out.println("Validade CNH: " + motorista.getValidadeCnh());
        System.out.println("Localização: " + motorista.getLocalizacaoAtual());
        System.out.println("Avaliação média: " + motorista.getAvaliacaoMedia() + " ⭐");
        System.out.println("Total de avaliações: " + motorista.getTotalAvaliacoes());
        System.out.println("Status: " + (motorista.isAtivo() ? "Ativo" : "Inativo"));
        System.out.println("Disponibilidade: " + (motorista.isDisponivel() ? "Disponível" : "Indisponível"));
    }

    private void verCnhValidade() {
        System.out.println("\n--- CNH e Validade ---");
        System.out.println("CNH: " + motorista.getCnh());
        System.out.println("Validade: " + motorista.getValidadeCnh());
    }

    private void verStatusDisponibilidade() {
        System.out.println("\n--- Status de Disponibilidade ---");
        System.out.println("Disponibilidade: " + (motorista.isDisponivel() ? "Disponível" : "Indisponível"));
    }
}