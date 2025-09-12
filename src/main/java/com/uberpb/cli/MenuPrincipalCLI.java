package com.uberpb.cli;

import com.uberpb.model.User;
import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.cli.MenuPassageiroCLI;
import java.util.Scanner;

public class MenuPrincipalCLI {
    private Scanner sc;
    private DatabaseManager db;
    private User usuarioLogado;

    public MenuPrincipalCLI(Scanner sc, DatabaseManager db, User usuarioLogado) {
        this.sc = sc;
        this.db = db;
        this.usuarioLogado = usuarioLogado;
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("Usuario logado: " + usuarioLogado.getNome() + " (" + usuarioLogado.getEmail() + ")");
            System.out.println("1 - Cadastrar perfil de Passageiro");
            System.out.println("2 - Cadastrar perfil de Motorista");
            System.out.println("3 - Menu Passageiro");
            System.out.println("4 - Menu Motorista");
            System.out.println("9 - Logout");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrarPerfilPassageiro();
                case 2 -> cadastrarPerfilMotorista();
                case 3 -> menuPassageiro();
                case 4 -> menuMotorista();
                case 9 -> {
                    System.out.println("Saindo da conta...");
                    usuarioLogado = null;
                    return;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private void cadastrarPerfilPassageiro() {
        // Verificar se já possui perfil de passageiro
        var passageiroOpt = db.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isPresent()) {
            System.out.println("Voce ja possui perfil de passageiro.");
            return;
        }

        System.out.println("\n--- Cadastro de Perfil Passageiro ---");

        int idade = -1;
        // Validar idade (deve ser maior ou igual a 18)
        while (idade == -1) {
            System.out.print("Idade: ");
            try {
                int inputIdade = sc.nextInt();
                sc.nextLine();

                if (ValidadorCLI.validarIdade(inputIdade)) {
                    if (inputIdade >= 18) {
                        idade = inputIdade;
                        System.out.println("✓ Idade válida!");
                    } else {
                        System.out.println("ERRO: Passageiro deve ser maior de 18 anos!");
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO: Digite uma idade válida!");
                sc.nextLine(); // Limpar buffer
            }
        }

        Passageiro p = new Passageiro(usuarioLogado.getId(), "Nao definida", false);
        p.setUsername(usuarioLogado.getUsername());
        p.setSenha(usuarioLogado.getSenha());
        p.setNome(usuarioLogado.getNome());
        p.setSobrenome(usuarioLogado.getSobrenome());
        p.setEmail(usuarioLogado.getEmail());
        p.setTelefone(usuarioLogado.getTelefone());
        p.setTipo("passageiro");
        p.setDataCadastro(usuarioLogado.getDataCadastro());
        p.setIdade(idade);

        Passageiro savedPassageiro = db.savePassageiro(p);
        System.out.println("\n🎉 Perfil de passageiro cadastrado com sucesso!");
        System.out.println("Dados salvos em: database/users/users.json");
        System.out.println("Dados salvos em: database/passageiros/passageiros.json");
    }

    private void cadastrarPerfilMotorista() {
        // Verificar se já possui perfil de motorista
        var motoristaOpt = db.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isPresent()) {
            System.out.println("Voce ja possui perfil de motorista.");
            return;
        }

        System.out.println("\n--- Cadastro de Perfil Motorista ---");

        String cnh = null;
        String validade = null;

        // CNH - Validar formato (apenas números, 11 caracteres)
        while (cnh == null) {
            System.out.print("CNH (apenas números, 11 dígitos): ");
            String input = sc.nextLine();
            if (ValidadorCLI.validarCNH(input)) {
                cnh = input;
                System.out.println("✓ CNH válida!");
            }
        }

        // Validade da CNH
        while (validade == null) {
            System.out.print("Validade da CNH (dd/mm/yyyy): ");
            String input = sc.nextLine();
            if (ValidadorCLI.validarDataValidade(input)) {
                validade = input;
                System.out.println("✓ Validade válida!");
            }
        }

        // Criar motorista com os dados
        Motorista m = new Motorista(usuarioLogado.getId(), true, cnh, validade, 0.0, 0, true, "Nao definida");
        m.setUsername(usuarioLogado.getUsername());
        m.setSenha(usuarioLogado.getSenha());
        m.setNome(usuarioLogado.getNome());
        m.setSobrenome(usuarioLogado.getSobrenome());
        m.setEmail(usuarioLogado.getEmail());
        m.setTelefone(usuarioLogado.getTelefone());
        m.setTipo("motorista");
        m.setDataCadastro(usuarioLogado.getDataCadastro());

        Motorista savedMotorista = db.saveMotorista(m);

        System.out.println("\n🎉 Perfil de motorista cadastrado com sucesso!");
        System.out.println("Dados salvos em: database/users/users.json");
        System.out.println("Dados salvos em: database/motoristas/motoristas.json");
    }

    private void menuPassageiro() {
        var passageiroOpt = db.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de passageiro. Cadastre primeiro.");
            return;
        }
        Passageiro p = passageiroOpt.get();
        MenuPassageiroCLI menuPassageiro = new MenuPassageiroCLI(sc, db, p);
        menuPassageiro.exibirMenu();
    }

    private void menuMotorista() {
        var motoristaOpt = db.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de motorista. Cadastre primeiro.");
            return;
        }
        Motorista m = motoristaOpt.get();
        MenuMotoristaCLI menuMotorista = new MenuMotoristaCLI(sc, db, m);
        menuMotorista.exibirMenu();
    }

    public User getUsuarioLogado() {
        return usuarioLogado;
    }
}
