package com.uberpb.cli.menus;

import com.uberpb.sevices.CorridaService;
import com.uberpb.cli.forms.CadastroVeiculoCLI;
import com.uberpb.model.Corrida;
import com.uberpb.model.CorridaStatus;
import com.uberpb.model.Motorista;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.services.LocalizacaoService;

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
            System.out.println("11 - Finalizar corrida atual");
            System.out.println("12 - Voltar");
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
                case 11 -> finalizarCorridaAtual();
                case 12 -> {
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
        System.out.println("\n--- Corridas Atribuídas ---");

        // Buscar corridas atribuídas especificamente a este motorista
        CorridaService corridaService = new CorridaService();
        List<Corrida> corridasAtribuidas = corridaService.listarCorridasPorMotorista(motorista.getId())
                .stream()
                .filter(c -> c.getStatus() == CorridaStatus.PENDENTE)
                .sorted((c1, c2) -> c2.getDataHoraSolicitacao().compareTo(c1.getDataHoraSolicitacao()))
                .toList();

        if (corridasAtribuidas.isEmpty()) {
            System.out.println("Não há corridas atribuídas a você no momento.");
            return;
        }

        System.out.println("🔔 Você tem " + corridasAtribuidas.size() + " corrida(s) atribuída(s):");
        System.out.println("═══════════════════════════════════════════════════════════════");

        for (int i = 0; i < corridasAtribuidas.size(); i++) {
            Corrida corrida = corridasAtribuidas.get(i);
            System.out.printf("%d. 📍 %s → %s | 💰 R$ %.2f | 📏 %.1f km%n",
                    (i + 1),
                    corrida.getOrigem(),
                    corrida.getDestino(),
                    corrida.getPrecoEstimado(),
                    corrida.getDistancia());

            if (corrida.getDataHoraSolicitacao() != null) {
                System.out.println("   🕐 Solicitada em: " + corrida.getDataHoraSolicitacao()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        }

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.print("Digite o número da corrida para aceitar (0 para voltar): ");
        int opcao = sc.nextInt();
        sc.nextLine();

        if (opcao > 0 && opcao <= corridasAtribuidas.size()) {
            Corrida corridaEscolhida = corridasAtribuidas.get(opcao - 1);

            System.out.println("\n📋 Detalhes da corrida:");
            System.out.println("📍 Origem: " + corridaEscolhida.getOrigem());
            System.out.println("📍 Destino: " + corridaEscolhida.getDestino());
            System.out.printf("📏 Distância: %.1f km%n", corridaEscolhida.getDistancia());
            System.out.printf("💰 Valor: R$ %.2f%n", corridaEscolhida.getPrecoEstimado());

            // Calcular tempo estimado baseado na distância
            int tempoEstimado = (int) Math.ceil(corridaEscolhida.getDistancia() * 3);
            System.out.println("⏱️ Tempo estimado: " + tempoEstimado + " min");

            System.out.print("\nDeseja aceitar esta corrida? (s/n): ");
            String resposta = sc.nextLine();

            if (resposta.equalsIgnoreCase("s")) {
                // Inicia a corrida
                corridaService.iniciarCorrida(corridaEscolhida.getId());

                // Atualiza disponibilidade do motorista
                motorista.setDisponivel(false);
                db.updateMotorista(motorista);

                System.out.println("✅ Corrida aceita e iniciada com sucesso!");
                System.out.println("🚗 Você agora está a caminho do passageiro!");
            } else {
                System.out.println("❌ Corrida recusada.");
                System.out.println("🔄 Buscando próximo motorista disponível...");

                // Mostrar informações sobre motoristas disponíveis
                List<Motorista> motoristasDisponiveis = corridaService.listarMotoristasPorProximidade(
                        corridaEscolhida.getOrigem(),
                        corridaEscolhida.getCategoria(),
                        motorista.getId());

                if (!motoristasDisponiveis.isEmpty()) {
                    System.out.println("📋 Motoristas disponíveis encontrados: " + motoristasDisponiveis.size());

                    // Reatribuir corrida para o próximo motorista mais próximo
                    boolean reatribuida = corridaService.reatribuirCorrida(corridaEscolhida.getId(), motorista.getId());

                    if (reatribuida) {
                        System.out.println("✅ Corrida reatribuída com sucesso para outro motorista.");
                    } else {
                        System.out.println("❌ Erro ao reatribuir a corrida.");
                    }
                } else {
                    System.out.println("❌ Nenhum motorista disponível na categoria " +
                            corridaEscolhida.getCategoria().getNome() + ".");
                    System.out.println("🚫 Corrida será cancelada automaticamente.");

                    // Forçar cancelamento
                    corridaService.reatribuirCorrida(corridaEscolhida.getId(), motorista.getId());
                }
            }
        }
    }

    private void finalizarCorridaAtual() {
        System.out.println("\n--- Finalizar Corrida Atual ---");

        // Buscar corrida em andamento do motorista usando CorridaService
        List<Corrida> todasCorridas = corridaService.listarCorridasPorMotorista(motorista.getId());

        Optional<Corrida> corridaEmAndamento = todasCorridas.stream()
                .filter(corrida -> corrida.getStatus() == CorridaStatus.EM_ANDAMENTO)
                .findFirst();

        if (corridaEmAndamento.isEmpty()) {
            System.out.println("❌ Você não possui nenhuma corrida em andamento.");
            return;
        }

        Corrida corrida = corridaEmAndamento.get();

        System.out.println("📍 Corrida encontrada:");
        System.out.println("   ID: " + corrida.getId());
        System.out.println("   Origem: " + corrida.getOrigem());
        System.out.println("   Destino: " + corrida.getDestino());
        System.out.println("   Valor: R$ " + String.format("%.2f", corrida.getPrecoEstimado()));

        System.out.print("\nConfirma a finalização desta corrida? (s/n): ");
        String confirmacao = sc.nextLine().trim().toLowerCase();

        if (confirmacao.equals("s") || confirmacao.equals("sim")) {
            try {
                corridaService.finalizarCorrida(corrida.getId());
                System.out.println("✅ Corrida finalizada com sucesso!");
                System.out.println("💰 Valor recebido: R$ " + String.format("%.2f", corrida.getPrecoEstimado()));
            } catch (Exception e) {
                System.out.println("❌ Erro ao finalizar corrida: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Finalização cancelada.");
        }
    }
}