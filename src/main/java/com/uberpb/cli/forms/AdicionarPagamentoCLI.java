package com.uberpb.cli.forms;

import com.uberpb.model.pagamento.Cartao;
import com.uberpb.model.pagamento.PIX;
import com.uberpb.model.pagamento.PayPal;
import com.uberpb.enums.TipoChavePix;
import com.uberpb.model.Passageiro;
import com.uberpb.services.MetodoPagamentoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Menu CLI para adicionar métodos de pagamento.
 */
public class AdicionarPagamentoCLI {
    private final Scanner sc;
    private final Passageiro passageiro;
    private final MetodoPagamentoService metodoPagamentoService;

    public AdicionarPagamentoCLI(Scanner sc, Passageiro passageiro) {
        this.sc = sc;
        this.passageiro = passageiro;
        this.metodoPagamentoService = new MetodoPagamentoService();
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Adicionar Método de Pagamento ===");
            System.out.println("1 - Cartão (Crédito/Débito)");
            System.out.println("2 - PIX");
            System.out.println("3 - PayPal");
            System.out.println("4 - Voltar");
            System.out.print("Escolha: ");

            int opcao = sc.nextInt();
            sc.nextLine(); // Consumir quebra de linha

            switch (opcao) {
                case 1 -> cadastrarCartao();
                case 2 -> cadastrarPIX();
                case 3 -> cadastrarPayPal();
                case 4 -> {
                    return;
                }
                default -> System.out.println("❌ Opção inválida!");
            }
        }
    }

    private void cadastrarCartao() {
        System.out.println("\n--- Cadastrar Cartão ---");

        try {
            System.out.print("Número do cartão (16 dígitos): ");
            String numeroCartao = sc.nextLine().trim().replaceAll("\\s+", "");

            System.out.print("PIN (4 dígitos): ");
            String pin = sc.nextLine().trim();

            System.out.print("Nome do titular: ");
            String nomeTitular = sc.nextLine().trim();

            System.out.print("CPF do titular (apenas números): ");
            String cpf = sc.nextLine().trim();

            System.out.print("Data de validade (MM/yyyy): ");
            String validadeStr = sc.nextLine().trim();

            System.out.println("Tipo do cartão:");
            System.out.println("1 - Crédito");
            System.out.println("2 - Débito");
            System.out.print("Escolha: ");
            int tipoOpcao = sc.nextInt();
            sc.nextLine();

            String tipo = switch (tipoOpcao) {
                case 1 -> "CREDITO";
                case 2 -> "DEBITO";
                default -> {
                    System.out.println("❌ Tipo inválido!");
                    yield null;
                }
            };

            if (tipo == null)
                return;

            // Converter data
            LocalDate validade;
            try {
                // Assumir dia 01 para a data de validade
                validade = LocalDate.parse("01/" + validadeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // Ir para o último dia do mês
                validade = validade.withDayOfMonth(validade.lengthOfMonth());
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de data inválido! Use MM/yyyy (ex: 12/2025)");
                return;
            }

            // Criar cartão
            Cartao cartao = new Cartao(passageiro.getId(), numeroCartao, pin, validade,
                    nomeTitular, cpf, tipo);

            // Salvar
            Cartao cartaoSalvo = metodoPagamentoService.salvarCartao(cartao);

            System.out.println("✅ Cartão cadastrado com sucesso!");
            System.out.println(cartaoSalvo);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void cadastrarPIX() {
        System.out.println("\n--- Cadastrar PIX ---");

        try {
            System.out.println("Tipos de chave PIX:");
            TipoChavePix[] tipos = TipoChavePix.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.println((i + 1) + " - " + tipos[i].getDescricao());
            }

            System.out.print("Escolha o tipo de chave: ");
            int tipoOpcao = sc.nextInt();
            sc.nextLine();

            if (tipoOpcao < 1 || tipoOpcao > tipos.length) {
                System.out.println("❌ Tipo de chave inválido!");
                return;
            }

            TipoChavePix tipoChave = tipos[tipoOpcao - 1];

            System.out.print("Digite a chave " + tipoChave.getDescricao() + ": ");
            String chave = sc.nextLine().trim();

            System.out.print("Descrição/Apelido (opcional): ");
            String descricao = sc.nextLine().trim();
            if (descricao.isEmpty()) {
                descricao = "PIX " + tipoChave.getDescricao();
            }

            // Criar PIX
            PIX pix = new PIX(passageiro.getId(), chave, tipoChave, descricao);

            // Salvar
            PIX pixSalvo = metodoPagamentoService.salvarPIX(pix);

            System.out.println("✅ PIX cadastrado com sucesso!");
            System.out.println(pixSalvo);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void cadastrarPayPal() {
        System.out.println("\n--- Cadastrar PayPal ---");

        try {
            System.out.print("Email do PayPal: ");
            String email = sc.nextLine().trim();

            System.out.print("Nome completo: ");
            String nomeCompleto = sc.nextLine().trim();

            // Criar PayPal
            PayPal paypal = new PayPal(passageiro.getId(), email, nomeCompleto);

            // Salvar
            PayPal paypalSalvo = metodoPagamentoService.salvarPayPal(paypal);

            System.out.println("✅ PayPal cadastrado com sucesso!");
            System.out.println(paypalSalvo);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }
}