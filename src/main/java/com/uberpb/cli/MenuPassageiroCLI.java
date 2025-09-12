package com.uberpb.cli;

import com.uberpb.model.Passageiro;
import com.uberpb.repository.DatabaseManager;
import java.util.Scanner;

public class MenuPassageiroCLI {
    private final Scanner sc;
    private final DatabaseManager db;
    private final Passageiro passageiro;

    public MenuPassageiroCLI(Scanner sc, DatabaseManager db, Passageiro passageiro) {
        this.sc = sc;
        this.db = db;
        this.passageiro = passageiro;
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Passageiro ===");
            System.out.println("1 - Cadastrar metodo de pagamento");
            System.out.println("2 - Realizar corrida");
            System.out.println("3 - Ver avaliacao media");
            System.out.println("4 - Ver historico de corridas");
            System.out.println("5 - Ver localizacao atual");
            System.out.println("6 - Ver status (em corrida ou nao)");
            System.out.println("7 - Atualizar localizacao");
            System.out.println("8 - Ver informacoes do perfil");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrarMetodoPagamento();
                case 2 -> System.out.println("Funcionalidade de corrida em desenvolvimento...");
                case 3 -> verAvaliacaoMedia();
                case 4 -> verHistoricoCorridas();
                case 5 -> verLocalizacaoAtual();
                case 6 -> verStatusCorrida();
                case 7 -> atualizarLocalizacao();
                case 8 -> verInformacoesPerfil();
                case 9 -> {
                    return;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private void cadastrarMetodoPagamento() {
        System.out.println("\n--- Cadastrar Método de Pagamento ---");
        System.out.print("Digite o método de pagamento: ");
        String metodo = sc.nextLine();
        if (metodo != null && !metodo.trim().isEmpty()) {
            passageiro.getMetodosPagamento().add(metodo.trim());
            db.updatePassageiro(passageiro);
            System.out.println("✓ Método de pagamento cadastrado com sucesso!");
            System.out.println("Dados atualizados em: database/passageiros/passageiros.json");
        } else {
            System.out.println("ERRO: Método de pagamento não pode estar vazio!");
        }
    }

    private void verAvaliacaoMedia() {
        System.out.println("\n--- Avaliação Média ---");
        System.out.println("Avaliação média: " + passageiro.getAvaliacaoMedia() + " ⭐");
    }

    private void verHistoricoCorridas() {
        System.out.println("\n--- Histórico de Corridas ---");
        if (passageiro.getHistoricoCorridas().isEmpty()) {
            System.out.println("Nenhuma corrida realizada ainda.");
        } else {
            for (int i = 0; i < passageiro.getHistoricoCorridas().size(); i++) {
                System.out.println((i + 1) + ". " + passageiro.getHistoricoCorridas().get(i));
            }
        }
    }

    private void verLocalizacaoAtual() {
        System.out.println("\n--- Localização Atual ---");
        System.out.println("Localização: " + passageiro.getLocalizacaoAtual());
    }

    private void verStatusCorrida() {
        System.out.println("\n--- Status da Corrida ---");
        System.out.println("Status: " + (passageiro.isEmCorrida() ? "Em corrida" : "Disponível"));
    }

    private void atualizarLocalizacao() {
        System.out.println("\n--- Atualizar Localização ---");
        System.out.print("Digite a nova localização: ");
        String localizacao = sc.nextLine();
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            passageiro.setLocalizacaoAtual(localizacao.trim());
            db.updatePassageiro(passageiro);
            System.out.println("✓ Localização atualizada com sucesso!");
            System.out.println("Dados atualizados em: database/passageiros/passageiros.json");
        } else {
            System.out.println("ERRO: Localização não pode estar vazia!");
        }
    }

    private void verInformacoesPerfil() {
        System.out.println("\n--- Informações do Perfil ---");
        System.out.println("Nome: " + passageiro.getNome() + " " + passageiro.getSobrenome());
        System.out.println("Email: " + passageiro.getEmail());
        System.out.println("Telefone: " + passageiro.getTelefone());
        System.out.println("Idade: " + passageiro.getIdade() + " anos");
        System.out.println("Localização: " + passageiro.getLocalizacaoAtual());
        System.out.println("Avaliação média: " + passageiro.getAvaliacaoMedia() + " ⭐");
        System.out.println("Status: " + (passageiro.isEmCorrida() ? "Em corrida" : "Disponível"));
        System.out.println("Métodos de pagamento: " + passageiro.getMetodosPagamento());
    }
}
