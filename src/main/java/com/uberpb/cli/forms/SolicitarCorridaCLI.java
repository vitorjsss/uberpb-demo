package com.uberpb.cli.forms;

import com.uberpb.enums.Categoria;
import com.uberpb.model.Corrida;
import com.uberpb.model.Passageiro;
import com.uberpb.services.CorridaService;
import com.uberpb.services.EstimativaService;
import com.uberpb.services.MetodoPagamentoService;

import java.util.Scanner;

/**
 * Menu dedicado para solicitar corridas
 */
public class SolicitarCorridaCLI {
    private final Scanner sc;
    private final Passageiro passageiro;
    private final EstimativaService estimativaService;
    private final MetodoPagamentoService metodoPagamentoService;

    public SolicitarCorridaCLI(Scanner sc, Passageiro passageiro) {
        this.sc = sc;
        this.passageiro = passageiro;
        this.estimativaService = new EstimativaService();
        this.metodoPagamentoService = new MetodoPagamentoService();
    }

    public void exibirMenu() {
        System.out.println("\n=== Solicitar Corrida ===");

        // Verificar se o passageiro já tem uma corrida ativa
        CorridaService corridaService = new CorridaService();
        if (corridaService.passageiroTemCorridaAtiva(passageiro.getId())) {
            System.out.println("❌ Erro: Você já tem uma corrida ativa!");
            System.out.println("Finalize ou cancele sua corrida atual antes de solicitar uma nova.");
            return;
        }

        // Escolher origem
        String origem = escolherOrigem();
        if (origem == null)
            return;

        // Escolher destino
        String destino = escolherDestino();
        if (destino == null)
            return;

        // Escolher categoria
        Categoria categoriaEscolhida = escolherCategoria(origem, destino);
        if (categoriaEscolhida == null)
            return;

        // Selecionar método de pagamento
        MetodoPagamentoSelecionado metodoPagamentoSelecionado = selecionarMetodoPagamento();
        if (metodoPagamentoSelecionado == null) {
            System.out.println("❌ Operação cancelada - método de pagamento não selecionado.");
            return;
        }

        // Criar a corrida
        criarCorrida(origem, destino, categoriaEscolhida, metodoPagamentoSelecionado);
    }

    private String escolherOrigem() {
        System.out.println("\n=== Escolher Origem ===");
        System.out.println("1 - Usar localização atual (" + passageiro.getLocalizacaoAtual() + ")");
        System.out.println("2 - Escolher outra localização");
        System.out.println("3 - Voltar");
        System.out.print("Escolha: ");
        int opcaoOrigem = sc.nextInt();
        sc.nextLine();

        switch (opcaoOrigem) {
            case 1:
                String origem = passageiro.getLocalizacaoAtual();
                System.out.println("📍 Origem selecionada: " + origem);
                return origem;

            case 2:
                estimativaService.exibirLocalizacoes();
                System.out.print("Digite a origem: ");
                String origemEscolhida = sc.nextLine().trim();

                if (!estimativaService.isLocalizacaoValida(origemEscolhida)) {
                    System.out.println("❌ Erro: Localização de origem inválida!");
                    return null;
                }
                return origemEscolhida;

            case 3:
                return null; // Voltar

            default:
                System.out.println("❌ Erro: Opção inválida!");
                return null;
        }
    }

    private String escolherDestino() {
        System.out.println("\n=== Escolher Destino ===");
        estimativaService.exibirLocalizacoes();
        System.out.print("Digite o destino: ");
        String destino = sc.nextLine().trim();

        if (!estimativaService.isLocalizacaoValida(destino)) {
            System.out.println("❌ Erro: Localização de destino inválida!");
            return null;
        }

        return destino;
    }

    private Categoria escolherCategoria(String origem, String destino) {
        System.out.println("\n=== Escolher Categoria ===");

        // Mostrar todas as categorias com preços estimados
        Categoria[] categorias = Categoria.values();
        int tempo = estimativaService.estimarTempoMinutos(origem, destino);

        System.out.println("Rota: " + origem + " → " + destino);
        System.out.println("Tempo estimado: ≈ " + tempo + " min");
        System.out.println("────────────────────────────────────────");

        for (int i = 0; i < categorias.length; i++) {
            Categoria categoria = categorias[i];
            double preco = estimativaService.estimarPreco(origem, destino, categoria.getNome());

            System.out.printf("%d - %s: R$ %.2f%n",
                    i + 1, categoria.getNome(), preco);
        }
        System.out.println((categorias.length + 1) + " - Voltar");
        System.out.println("────────────────────────────────────────");

        System.out.print("Escolha a categoria (número): ");
        int opcaoCategoria = sc.nextInt();
        sc.nextLine();

        if (opcaoCategoria == categorias.length + 1) {
            return null; // Voltar
        }

        if (opcaoCategoria < 1 || opcaoCategoria > categorias.length) {
            System.out.println("❌ Erro: Categoria inválida!");
            return null;
        }

        return categorias[opcaoCategoria - 1];
    }

    private void criarCorrida(String origem, String destino, Categoria categoriaEscolhida,
            MetodoPagamentoSelecionado metodoPagamento) {

        CorridaService corridaService = new CorridaService();
        double precoFinal = estimativaService.estimarPreco(origem, destino, categoriaEscolhida.getNome());
        int passageiroId = passageiro.getId();
        double distancia = estimativaService.calcularDistanciaKm(origem, destino);

        Corrida corrida = corridaService.criarCorrida(origem, destino, categoriaEscolhida,
                passageiroId, 0, 0, distancia);

        if (corrida != null && corrida.getMotoristaId() > 0) {
            System.out.println("\n✅ Corrida solicitada com sucesso!");
            System.out.println("📍 Origem: " + origem);
            System.out.println("📍 Destino: " + destino);
            System.out.println("🚗 Categoria: " + categoriaEscolhida.getNome());
            System.out.printf("📏 Distância: %.1f km%n", distancia);
            System.out.println("💰 Preço: R$ " + String.format("%.2f", precoFinal));
            System.out.println("💳 Método de pagamento: " + metodoPagamento.tipo);

        } else {
            System.out.println("\n❌ Nenhum motorista disponível na categoria " + categoriaEscolhida.getNome());
            System.out.println("Tente novamente mais tarde ou escolha outra categoria.");
        }

        System.out.println("\nPressione Enter para continuar...");
        sc.nextLine();
    }

    /**
     * Classe interna para representar um método de pagamento selecionado
     */
    private static class MetodoPagamentoSelecionado {
        final String tipo;
        final int id;

        MetodoPagamentoSelecionado(String tipo, int id) {
            this.tipo = tipo;
            this.id = id;
        }
    }

    /**
     * Permite ao usuário selecionar um método de pagamento
     */
    private MetodoPagamentoSelecionado selecionarMetodoPagamento() {
        System.out.println("\n=== Selecionar Método de Pagamento ===");

        int total = 0;
        var cartoes = metodoPagamentoService.listarCartoesPorUsuario(passageiro.getId());
        var pixList = metodoPagamentoService.listarPIXPorUsuario(passageiro.getId());
        var paypalList = metodoPagamentoService.listarPayPalPorUsuario(passageiro.getId());

        // Verificar se tem métodos cadastrados
        total = cartoes.size() + pixList.size() + paypalList.size();

        if (total == 0) {
            System.out.println("❌ Você não possui métodos de pagamento cadastrados!");
            System.out.println("\n1 - Cadastrar novo método de pagamento");
            System.out.println("2 - Voltar ao menu anterior");
            System.out.print("Escolha: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            if (opcao == 1) {
                AdicionarPagamentoCLI menuPagamento = new AdicionarPagamentoCLI(sc, passageiro);
                menuPagamento.exibirMenu();
                // Tentar novamente após cadastro
                return selecionarMetodoPagamento();
            } else {
                return null; // Cancelar
            }
        }

        // Exibir métodos disponíveis
        int contador = 1;

        // Listar cartões
        if (!cartoes.isEmpty()) {
            System.out.println("\n🏦 CARTÕES:");
            for (int i = 0; i < cartoes.size(); i++) {
                var cartao = cartoes.get(i);
                System.out.println(contador + " - " + cartao.getTipo() + " - " + cartao.getNumeroMascarado());
                contador++;
            }
        }

        // Listar PIX
        if (!pixList.isEmpty()) {
            System.out.println("\n💳 PIX:");
            for (int i = 0; i < pixList.size(); i++) {
                var pix = pixList.get(i);
                System.out.println(contador + " - PIX " + pix.getTipoChave() + " - " + pix.getChaveMascarada());
                contador++;
            }
        }

        // Listar PayPal
        if (!paypalList.isEmpty()) {
            System.out.println("\n🌐 PAYPAL:");
            for (int i = 0; i < paypalList.size(); i++) {
                var paypal = paypalList.get(i);
                System.out.println(contador + " - PayPal - " + paypal.getEmailMascarado());
                contador++;
            }
        }

        System.out.println("\n" + contador + " - Cadastrar novo método");
        System.out.println((contador + 1) + " - Voltar");
        System.out.print("Escolha o método de pagamento: ");

        int escolha = sc.nextInt();
        sc.nextLine();

        // Processar escolha
        int indiceAtual = 1;

        // Verificar se é um cartão
        if (escolha >= indiceAtual && escolha < indiceAtual + cartoes.size()) {
            var cartao = cartoes.get(escolha - indiceAtual);
            return new MetodoPagamentoSelecionado("CARTAO_" + cartao.getTipo(), cartao.getId());
        }
        indiceAtual += cartoes.size();

        // Verificar se é PIX
        if (escolha >= indiceAtual && escolha < indiceAtual + pixList.size()) {
            var pix = pixList.get(escolha - indiceAtual);
            return new MetodoPagamentoSelecionado("PIX", pix.getId());
        }
        indiceAtual += pixList.size();

        // Verificar se é PayPal
        if (escolha >= indiceAtual && escolha < indiceAtual + paypalList.size()) {
            var paypal = paypalList.get(escolha - indiceAtual);
            return new MetodoPagamentoSelecionado("PAYPAL", paypal.getId());
        }
        indiceAtual += paypalList.size();

        // Cadastrar novo método
        if (escolha == indiceAtual) {
            AdicionarPagamentoCLI menuPagamento = new AdicionarPagamentoCLI(sc, passageiro);
            menuPagamento.exibirMenu();
            return selecionarMetodoPagamento(); // Tentar novamente
        }

        // Voltar
        if (escolha == indiceAtual + 1) {
            return null;
        }

        System.out.println("❌ Opção inválida!");
        return selecionarMetodoPagamento(); // Tentar novamente
    }
}