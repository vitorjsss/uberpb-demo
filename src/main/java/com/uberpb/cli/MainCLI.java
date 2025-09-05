package com.uberpb.cli;

import com.uberpb.model.*;
import java.util.Scanner;

public class MainCLI {

    private static Scanner sc = new Scanner(System.in);
    private static RepositorioMemoria repo = new RepositorioMemoria();
    private static User usuarioLogado = null;

    public static void main(String[] args) {
        while (true) {
            if (usuarioLogado == null) {
                menuInicial();
            } else {
                menuPrincipal();
            }
        }
    }

    // ===== MENU INICIAL (CADASTRO / LOGIN) =====
    private static void menuInicial() {
        System.out.println("\n=== Bem-vindo ao UberPB ===");
        System.out.println("1 - Cadastrar novo usuario");
        System.out.println("2 - Login");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1 -> cadastrarUser();
            case 2 -> login();
            case 0 -> {
                System.out.println("Encerrando...");
                System.exit(0);
            }
            default -> System.out.println("Opcao invalida!");
        }
    }

    // ===== CADASTRO DE USUARIO (CREDENCIAIS GERAIS) =====
    private static void cadastrarUser() {
        System.out.println("\n--- Cadastro de Usuario ---");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Sobrenome: ");
        String sobrenome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        User u = new User();
        u.setUsername(username);
        u.setSenha(senha);
        u.setNome(nome);
        u.setSobrenome(sobrenome);
        u.setEmail(email);
        u.setTelefone(telefone);

        repo.salvar(u);

        System.out.println("Usuario cadastrado com sucesso!");
    }

    // ===== LOGIN =====
    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        User u = repo.buscarPorEmailSenha(email, senha);
        if (u != null) {
            usuarioLogado = u;
            System.out.println("Login bem-sucedido! Bem-vindo, " + u.getNome());
        } else {
            System.out.println("Email ou senha invalido!");
        }
    }

    // ===== MENU PRINCIPAL (APOS LOGIN) =====
    private static void menuPrincipal() {
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
            }
            default -> System.out.println("Opcao invalida!");
        }
    }

    // ===== CADASTRO PERFIL PASSAGEIRO =====
    private static void cadastrarPerfilPassageiro() {
        if (repo.getPassageiro(usuarioLogado) != null) {
            System.out.println("Voce ja possui perfil de passageiro.");
            return;
        }
        System.out.print("Idade: ");
        int idade = sc.nextInt();
        sc.nextLine();

        // Atualiza ou cria perfil passageiro
        Passageiro p = new Passageiro(usuarioLogado.getId(), "Nao definida", false);
        p.setUsername(usuarioLogado.getUsername());
        p.setSenha(usuarioLogado.getSenha());
        p.setNome(usuarioLogado.getNome());
        p.setSobrenome(usuarioLogado.getSobrenome());
        p.setEmail(usuarioLogado.getEmail());
        p.setTelefone(usuarioLogado.getTelefone());
        p.setIdade(idade);

        repo.associarPassageiro(usuarioLogado);
    }

    // ===== CADASTRO PERFIL MOTORISTA =====
    private static void cadastrarPerfilMotorista() {
        if (repo.getMotorista(usuarioLogado) != null) {
            System.out.println("Voce ja possui perfil de motorista.");
            return;
        }
        System.out.print("CNH: ");
        String cnh = sc.nextLine();
        System.out.print("Validade da CNH: ");
        String validade = sc.nextLine();

        repo.associarMotorista(usuarioLogado, cnh, validade);
    }

    // ===== MENU PASSAGEIRO =====
    private static void menuPassageiro() {
        Passageiro p = repo.getPassageiro(usuarioLogado);
        if (p == null) {
            System.out.println("Voce ainda nao possui perfil de passageiro. Cadastre primeiro.");
            return;
        }

        while (true) {
            System.out.println("\n=== Menu Passageiro ===");
            System.out.println("1 - Cadastrar metodo de pagamento");
            System.out.println("2 - Realizar corrida");
            System.out.println("3 - Ver avaliacao media");
            System.out.println("4 - Ver historico de corridas");
            System.out.println("5 - Ver localizacao atual");
            System.out.println("6 - Ver status (em corrida ou nao)");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            if (op == 9) break;
            else System.out.println("Em breve...");
        }
    }

    // ===== MENU MOTORISTA =====
    private static void menuMotorista() {
        Motorista m = repo.getMotorista(usuarioLogado);
        if (m == null) {
            System.out.println("Voce ainda nao possui perfil de motorista. Cadastre primeiro.");
            return;
        }

        while (true) {
            System.out.println("\n=== Menu Motorista ===");
            System.out.println("1 - Ver status ativo");
            System.out.println("2 - Ver avaliacao media");
            System.out.println("3 - Ver total de avaliacoes");
            System.out.println("4 - Ver localizacao atual");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            if (op == 9) break;
            else System.out.println("Em breve...");
        }
    }
}
