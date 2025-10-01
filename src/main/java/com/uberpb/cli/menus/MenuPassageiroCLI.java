package com.uberpb.cli.menus;

import com.uberpb.enums.Categoria;
import com.uberpb.model.Corrida;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.services.CorridaService;
import com.uberpb.services.EstimativaService;
import com.uberpb.services.LocalizacaoService;

import java.util.Optional;
import java.util.Scanner;

public class MenuPassageiroCLI {
    private final Scanner sc;
    private final DatabaseManager db;
    private final Passageiro passageiro;
    private final LocalizacaoService localizacaoService;
    private final EstimativaService estimativaService;

    public MenuPassageiroCLI(Scanner sc, DatabaseManager db, Passageiro passageiro) {
        this.sc = sc;
        this.db = db;
        this.passageiro = passageiro;
        this.localizacaoService = new LocalizacaoService();
        this.estimativaService = new EstimativaService();
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Passageiro ===");
            System.out.println("1 - Cadastrar metodo de pagamento");
            System.out.println("2 - Solicitar corrida");
            System.out.println("3 - Ver historico de corridas");
            System.out.println("4 - Ver localizacao atual");
            System.out.println("5 - Ver status (em corrida ou nao)");
            System.out.println("6 - Atualizar localizacao");
            System.out.println("7 - Ver informacoes do perfil");
            System.out.println("8 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrarMetodoPagamento();
                case 2 -> solicitarCorrida();
                case 3 -> verHistoricoCorridas();
                case 4 -> verLocalizacaoAtual();
                case 5 -> verStatusCorrida();
                case 6 -> atualizarLocalizacao();
                case 7 -> verInformacoesPerfil();
                case 8 -> {
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

        CorridaService corridaService = new CorridaService();
        Optional<Corrida> corridaAtiva = corridaService.obterCorridaAtivaPassageiro(passageiro.getId());

        if (corridaAtiva.isPresent()) {
            Corrida corrida = corridaAtiva.get();
            System.out.println("Status: Em corrida");
            System.out.println("═══════════════════════════════════════");
            System.out.println("📍 Origem: " + corrida.getOrigem());
            System.out.println("📍 Destino: " + corrida.getDestino());
            System.out.println("🚗 Categoria: " + corrida.getCategoria().getNome());
            System.out.println("📊 Status: " + corrida.getStatus());
            System.out.println("💰 Preço: R$ " + String.format("%.2f", corrida.getPrecoEstimado()));
            if (corrida.getDistancia() > 0) {
                System.out.println("📏 Distância: " + String.format("%.1f km", corrida.getDistancia()));
            }

            // Mostrar informações do motorista atribuído
            if (corrida.getMotoristaId() > 0 && corrida.getStatus() != CorridaStatus.PENDENTE) {
                Optional<Motorista> motoristaOpt = db.findMotoristaById(corrida.getMotoristaId());
                if (motoristaOpt.isPresent()) {
                    Motorista motorista = motoristaOpt.get();
                    System.out.println("\n🚖 Motorista Atribuído:");
                    System.out.println("👤 Nome: " + motorista.getNome() + " " + motorista.getSobrenome());
                    System.out.println("⭐ Avaliação: " + String.format("%.1f", motorista.getAvaliacaoMedia()));
                } else {
                    System.out.println("\n⚠️ Motorista não encontrado (ID: " + corrida.getMotoristaId() + ")");
                }
            } else {
                System.out.println("\n🔍 Aguardando atribuição de motorista...");
            }

            if (corrida.getDataHoraSolicitacao() != null) {
                System.out.println("🕐 Solicitada em: " + corrida.getDataHoraSolicitacao()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            if (corrida.getDataHoraAceito() != null) {
                System.out.println("✅ Aceita em: " + corrida.getDataHoraAceito()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

                // Mostrar tempo restante em tempo real
                int tempoRestanteReal = corrida.calcularTempoRestanteReal();
                if (tempoRestanteReal > 0) {
                    System.out.println("⏱️ Tempo restante: " + tempoRestanteReal + " min");
                } else if (corrida.getStatus() == CorridaStatus.EM_ANDAMENTO) {
                    System.out.println("⏱️ Tempo esgotado - chegando em breve!");
                }
            }
            if (corrida.getDataHoraFim() != null) {
                System.out.println("🏁 Finalizada em: " + corrida.getDataHoraFim()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            System.out.println("═══════════════════════════════════════");
        } else {
            System.out.println("Status: Disponível");
            System.out.println("Você não possui nenhuma corrida ativa no momento.");
        }
    }

    private void atualizarLocalizacao() {
        System.out.println("\n--- Atualizar Localização ---");

        // Obter uma localização aleatória do JSON
        String localizacaoAleatoria = localizacaoService.getLocalizacaoAleatoria();

        if (localizacaoAleatoria != null) {
            passageiro.setLocalizacaoAtual(localizacaoAleatoria);
            db.updatePassageiro(passageiro);
            System.out.println("📍 Nova localização: " + localizacaoAleatoria);
            System.out.println("✓ Localização atualizada com sucesso!");
            System.out.println("Dados atualizados em: database/passageiros/passageiros.json");
        } else {
            System.out.println("❌ Erro: Não foi possível obter uma localização aleatória!");
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

    private void solicitarCorrida() {
        System.out.println("\n--- Solicitar Corrida ---");

        // Verificar se o passageiro já tem uma corrida ativa
        CorridaService corridaService = new CorridaService();
        if (corridaService.passageiroTemCorridaAtiva(passageiro.getId())) {
            System.out.println("❌ Erro: Você já tem uma corrida ativa!");
            System.out.println("Finalize ou cancele sua corrida atual antes de solicitar uma nova.");
            return;
        }

        // Escolher origem: localização atual ou outra
        System.out.println("\n=== Escolher Origem ===");
        System.out.println("1 - Usar localização atual (" + passageiro.getLocalizacaoAtual() + ")");
        System.out.println("2 - Escolher outra localização");
        System.out.print("Escolha: ");
        int opcaoOrigem = sc.nextInt();
        sc.nextLine();

        String origem;
        if (opcaoOrigem == 1) {
            origem = passageiro.getLocalizacaoAtual();
            System.out.println("📍 Origem selecionada: " + origem);
        } else if (opcaoOrigem == 2) {
            estimativaService.exibirLocalizacoes();

            System.out.print("Digite a origem: ");
            origem = sc.nextLine().trim();

            if (!estimativaService.isLocalizacaoValida(origem)) {
                System.out.println("❌ Erro: Localização de origem inválida!");
                return;
            }
        } else {
            System.out.println("❌ Erro: Opção inválida!");
            return;
        }

        System.out.print("Digite o destino: ");
        String destino = sc.nextLine().trim();

        if (!estimativaService.isLocalizacaoValida(destino)) {
            System.out.println("❌ Erro: Localização de destino inválida!");
            return;
        }

        // Mostrar todas as categorias com preços estimados
        Categoria[] categorias = Categoria.values();
        int tempo = estimativaService.estimarTempoMinutos(origem, destino);

        System.out.println("\nComo você quer viajar?");
        System.out.println("Rota: " + origem + " → " + destino);
        System.out.println("Tempo estimado: ≈ " + tempo + " min");
        System.out.println("────────────────────────────────────────");

        for (int i = 0; i < categorias.length; i++) {
            Categoria categoria = categorias[i];
            double preco = estimativaService.estimarPreco(origem, destino, categoria.getNome());

            System.out.printf("%d - %s: R$ %.2f%n",
                    i + 1, categoria.getNome(), preco);
        }
        System.out.println("────────────────────────────────────────");

        System.out.print("Escolha a categoria (número): ");
        int opcaoCategoria = sc.nextInt();
        sc.nextLine();

        if (opcaoCategoria < 1 || opcaoCategoria > categorias.length) {
            System.out.println("❌ Erro: Categoria inválida!");
            return;
        }

        Categoria categoriaEscolhida = categorias[opcaoCategoria - 1];
        double precoFinal = estimativaService.estimarPreco(origem, destino, categoriaEscolhida.getNome());

        // Criar a corrida com atribuição automática do motorista mais próximo
        int passageiroId = passageiro.getId();
        double distancia = estimativaService.calcularDistanciaKm(origem, destino);

        Corrida corrida = corridaService.criarCorrida(origem, destino, categoriaEscolhida,
                passageiroId, 0, 0, distancia);

        if (corrida.getMotoristaId() > 0) {
            System.out.println("\n✅ Corrida solicitada com sucesso!");
            System.out.println("📍 Origem: " + origem);
            System.out.println("📍 Destino: " + destino);
            System.out.println("🚗 Categoria: " + categoriaEscolhida.getNome());
            System.out.printf("📏 Distância: %.1f km%n", distancia);
            System.out.println("💰 Preço: R$ " + String.format("%.2f", precoFinal));

        } else {
            System.out.println("\n❌ Nenhum motorista disponível na categoria " + categoriaEscolhida.getNome());
            System.out.println("Tente novamente mais tarde ou escolha outra categoria.");
        }
    }
}