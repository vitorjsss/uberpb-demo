package com.uberpb.cli.menus;

import com.uberpb.cli.forms.AdicionarPagamentoCLI;
import com.uberpb.cli.forms.SolicitarCorridaCLI;
import com.uberpb.model.Corrida;
import com.uberpb.enums.CorridaStatus;
import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.services.CorridaService;
import com.uberpb.services.LocalizacaoService;
import com.uberpb.services.MetodoPagamentoService;
import com.uberpb.services.ReciboService;

import java.util.Optional;
import java.util.Scanner;

public class MenuPassageiroCLI {
    private final Scanner sc;
    private final DatabaseManager db;
    private final Passageiro passageiro;
    private final LocalizacaoService localizacaoService;
    private final MetodoPagamentoService metodoPagamentoService;

    public MenuPassageiroCLI(Scanner sc, DatabaseManager db, Passageiro passageiro) {
        this.sc = sc;
        this.db = db;
        this.passageiro = passageiro;
        this.localizacaoService = new LocalizacaoService();
        this.metodoPagamentoService = new MetodoPagamentoService();
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Passageiro ===");
            System.out.println("1 - Cadastrar metodo de pagamento");
            System.out.println("2 - Ver metodos de pagamento");
            System.out.println("3 - Solicitar corrida");
            System.out.println("4 - Ver historico de corridas");
            System.out.println("5 - Ver localizacao atual");
            System.out.println("6 - Ver status (em corrida ou nao)");
            System.out.println("7 - Atualizar localizacao");
            System.out.println("8 - Ver informacoes do perfil");
            System.out.println("9 - Gerar recibo de corrida");
            System.out.println("10 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrarMetodoPagamento();
                case 2 -> verMetodosPagamento();
                case 3 -> solicitarCorrida();
                case 4 -> verHistoricoCorridas();
                case 5 -> verLocalizacaoAtual();
                case 6 -> verStatusCorrida();
                case 7 -> atualizarLocalizacao();
                case 8 -> verInformacoesPerfil();
                case 9 -> gerarReciboCorrida();
                case 10 -> {
                    return;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private void cadastrarMetodoPagamento() {
        AdicionarPagamentoCLI menuPagamento = new AdicionarPagamentoCLI(sc, passageiro);
        menuPagamento.exibirMenu();
    }

    private void verMetodosPagamento() {
        System.out.println("\n--- Métodos de Pagamento Cadastrados ---");

        int total = 0;

        // Cartões
        var cartoes = metodoPagamentoService.listarCartoesPorUsuario(passageiro.getId());
        if (!cartoes.isEmpty()) {
            System.out.println("\n🏦 CARTÕES:");
            for (int i = 0; i < cartoes.size(); i++) {
                System.out.println((i + 1) + ". " + cartoes.get(i));
                System.out.println("   ────────────────────────────────");
            }
            total += cartoes.size();
        }

        // PIX
        var pixList = metodoPagamentoService.listarPIXPorUsuario(passageiro.getId());
        if (!pixList.isEmpty()) {
            System.out.println("\n💳 PIX:");
            for (int i = 0; i < pixList.size(); i++) {
                System.out.println((i + 1) + ". " + pixList.get(i));
                System.out.println("   ────────────────────────────────");
            }
            total += pixList.size();
        }

        // PayPal
        var paypalList = metodoPagamentoService.listarPayPalPorUsuario(passageiro.getId());
        if (!paypalList.isEmpty()) {
            System.out.println("\n🌐 PAYPAL:");
            for (int i = 0; i < paypalList.size(); i++) {
                System.out.println((i + 1) + ". " + paypalList.get(i));
                System.out.println("   ────────────────────────────────");
            }
            total += paypalList.size();
        }

        if (total == 0) {
            System.out.println("❌ Nenhum método de pagamento cadastrado ainda.");
        } else {
            System.out.println("\n📊 Total: " + total + " método(s) de pagamento cadastrado(s)");
        }

        System.out.println("\nPressione Enter para continuar...");
        sc.nextLine();
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
            System.out.println("📊 Status: " + corrida.getStatusString());
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
    }

    private void solicitarCorrida() {
        SolicitarCorridaCLI menuSolicitar = new SolicitarCorridaCLI(sc, passageiro);
        menuSolicitar.exibirMenu();
    }

    private void gerarReciboCorrida() {
        System.out.println("\n=== Gerar Recibo de Corrida ===");
        
        ReciboService reciboService = new ReciboService();
        
        // Listar corridas finalizadas do passageiro
        var corridasFinalizadas = reciboService.listarCorridasFinalizadasDoPassageiro(passageiro.getId());
        
        if (corridasFinalizadas.isEmpty()) {
            System.out.println("❌ Você não possui corridas finalizadas para gerar recibo.");
            System.out.println("\nPressione Enter para voltar...");
            sc.nextLine();
            return;
        }
        
        System.out.println("\n📋 Corridas Finalizadas Disponíveis:");
        System.out.println("═══════════════════════════════════════");
        
        for (int i = 0; i < corridasFinalizadas.size(); i++) {
            Corrida corrida = corridasFinalizadas.get(i);
            System.out.println((i + 1) + ". 📍 " + corrida.getOrigem() + " → " + corrida.getDestino());
            System.out.println("   🚗 " + corrida.getCategoria());
            System.out.println("   💰 R$ " + String.format("%.2f", corrida.getPrecoEstimado()));
            if (corrida.getDataHoraFim() != null) {
                System.out.println("   📅 " + corrida.getDataHoraFim()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            System.out.println("   ────────────────────────────────");
        }
        
        System.out.print("\nEscolha o número da corrida para gerar o recibo (0 para cancelar): ");
        int escolha = sc.nextInt();
        sc.nextLine();
        
        if (escolha == 0) {
            return;
        }
        
        if (escolha < 1 || escolha > corridasFinalizadas.size()) {
            System.out.println("❌ Opção inválida!");
            System.out.println("\nPressione Enter para voltar...");
            sc.nextLine();
            return;
        }
        
        Corrida corridaEscolhida = corridasFinalizadas.get(escolha - 1);
        
        System.out.println("\n🧾 Gerando recibo da corrida " + corridaEscolhida.getOrigem() + " → " + corridaEscolhida.getDestino());
        System.out.println("═══════════════════════════════════════");
        
        // Gerar e exibir recibo
        boolean sucesso = reciboService.exibirRecibo(corridaEscolhida.getId());
        
        if (sucesso) {
            System.out.println("\n📄 Deseja salvar o recibo em arquivo? (s/n): ");
            String resposta = sc.nextLine().trim().toLowerCase();
            
            if (resposta.equals("s") || resposta.equals("sim")) {
                System.out.print("Digite o nome do arquivo (deixe vazio para nome padrão): ");
                String nomeArquivo = sc.nextLine().trim();
                
                if (nomeArquivo.isEmpty()) {
                    nomeArquivo = null; // Usará nome padrão
                }
                
                boolean salvo = reciboService.salvarReciboEmArquivo(corridaEscolhida.getId(), nomeArquivo);
                
                if (salvo) {
                    System.out.println("✅ Recibo salvo com sucesso!");
                } else {
                    System.out.println("❌ Erro ao salvar recibo em arquivo.");
                }
            }
        } else {
            System.out.println("❌ Erro ao gerar recibo.");
        }
        
        System.out.println("\nPressione Enter para voltar...");
        sc.nextLine();
    }
}