package com.uberpb.cli.menus;

import com.uberpb.sevices.CorridaService;
import com.uberpb.cli.forms.CadastroVeiculoCLI;
import com.uberpb.model.Corrida;
import com.uberpb.model.CorridaStatus;
import com.uberpb.model.Motorista;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.services.LocalizacaoService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuMotoristaCLI {
    private final Scanner sc;
    private final DatabaseManager db;
    private final Motorista motorista;
    private final CorridaService corridaService;
    private final LocalizacaoService localizacaoService;

    public MenuMotoristaCLI(Scanner sc, DatabaseManager db, Motorista motorista) {
        this.sc = sc;
        this.db = db;
        this.motorista = motorista;
        this.corridaService = new CorridaService();
        this.localizacaoService = new LocalizacaoService();
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Motorista ===");
            System.out.println("1 - Ver status ativo");
            System.out.println("2 - Ver avaliação média");
            System.out.println("3 - Ver total de avaliações");
            System.out.println("4 - Ver localização atual");
            System.out.println("5 - Atualizar localização");
            System.out.println("6 - Ver informações do perfil");
            System.out.println("7 - Ver CNH e validade");
            System.out.println("8 - Ver status disponibilidade");
            System.out.println("9 - Cadastrar veículo");
            System.out.println("10 - Notificações de corrida");
            System.out.println("11 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> verStatusAtivo();
                case 2 -> verAvaliacaoMedia();
                case 3 -> verTotalAvaliacoes();
                case 4 -> verLocalizacaoAtual();
                case 5 -> atualizarLocalizacao();
                case 6 -> verInformacoesPerfil();
                case 7 -> verCnhValidade();
                case 8 -> verStatusDisponibilidade();
                case 9 -> CadastroVeiculoCLI.cadastrarVeiculo(sc, db, motorista);
                case 10 -> notificacoesCorrida();
                case 11 -> {
                    return;
                }
                default -> System.out.println("Opção inválida!");
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

        // Obter uma localização aleatória do JSON
        String localizacaoAleatoria = localizacaoService.getLocalizacaoAleatoria();

        if (localizacaoAleatoria != null) {
            motorista.setLocalizacaoAtual(localizacaoAleatoria);
            db.updateMotorista(motorista);
            System.out.println("📍 Nova localização: " + localizacaoAleatoria);
            System.out.println("✓ Localização atualizada com sucesso!");
            System.out.println("Dados atualizados em: database/motoristas/motoristas.json");
        } else {
            System.out.println("❌ Erro: Não foi possível obter uma localização aleatória!");
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
        System.out.println(
                "Categoria: " + (motorista.getCategoria() != null ? motorista.getCategoria() : "Não definida"));
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

    private void notificacoesCorrida() {
        System.out.println("\n--- Corrida Disponível ---");

        // Busca corridas pendentes da categoria do motorista
        List<Corrida> corridasPendentes = db.listarCorridasPendentesPorCategoria(motorista.getCategoria());
        Optional<Corrida> corridaOpt = corridasPendentes.stream()
                .max(Comparator.comparing(Corrida::getDataHoraSolicitacao)); // mais recente

        if (corridaOpt.isEmpty()) {
            System.out.println("Não há corridas disponíveis para sua categoria no momento.");
            return;
        }

        Corrida corridaEscolhida = corridaOpt.get();

        // Mostra detalhes da corrida
        System.out.printf(
                "Origem: %s | Destino: %s | Distância: %.2f km | Valor: R$ %.2f%n",
                corridaEscolhida.getOrigem(),
                corridaEscolhida.getDestino(),
                corridaEscolhida.getDistancia(),
                corridaEscolhida.getPrecoEstimado());

        System.out.print("Deseja aceitar esta corrida? (s/n): ");
        String resposta = sc.nextLine();

        if (resposta.equalsIgnoreCase("s")) {
            // Associa o motorista e inicia a corrida
            corridaEscolhida.setMotoristaId(motorista.getId());
            corridaEscolhida.setStatus(CorridaStatus.EM_ANDAMENTO);
            db.updateCorrida(corridaEscolhida);

            // Atualiza disponibilidade do motorista
            motorista.setDisponivel(false);
            db.updateMotorista(motorista);

            System.out.println("✓ Corrida aceita e iniciada com sucesso!");
        } else {
            System.out.println("Corrida recusada.");
        }
    }
}