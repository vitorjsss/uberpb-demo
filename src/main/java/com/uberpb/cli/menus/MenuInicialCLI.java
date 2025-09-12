package com.uberpb.cli.menus;

import com.uberpb.helpers.ValidadoresCadastro;
import com.uberpb.model.*;
import com.uberpb.repository.DatabaseManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuInicialCLI {
    private static Scanner sc = new Scanner(System.in);
    private static DatabaseManager db = new DatabaseManager();
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

    // ===== CADASTRO DE USUARIO COM VALIDAÇÕES =====
    private static void cadastrarUser() {
        System.out.println("\n--- Cadastro de Usuario ---");

        String username = null;
        String senha = null;
        String nome = null;
        String sobrenome = null;
        String email = null;
        String telefone = null;

        // Username
        while (username == null) {
            System.out.print("Username: ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarCampoObrigatorio(input, "Username")) {
                username = input;
                System.out.println("✓ Username válido!");
            }
        }

        // Senha
        while (senha == null) {
            System.out.print("Senha: ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarSenha(input)) {
                senha = input;
                System.out.println("✓ Senha válida!");
            }
        }

        // Nome
        while (nome == null) {
            System.out.print("Nome: ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarNome(input)) {
                nome = input;
                System.out.println("✓ Nome válido!");
            }
        }

        // Sobrenome
        while (sobrenome == null) {
            System.out.print("Sobrenome: ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarCampoObrigatorio(input, "Sobrenome")) {
                sobrenome = input;
                System.out.println("✓ Sobrenome válido!");
            }
        }

        // Email
        while (email == null) {
            System.out.print("Email: ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarEmail(input)) {
                // Verificar email duplicado
                if (db.findUserByEmail(input).isPresent()) {
                    System.out.println("ERRO: Já existe um usuário com este e-mail!");
                } else {
                    email = input;
                    System.out.println("✓ Email válido!");
                }
            }
        }

        // Telefone
        while (telefone == null) {
            System.out.print("Telefone: ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarTelefone(input)) {
                telefone = input;
                System.out.println("✓ Telefone válido!");
            }
        }

        User u = new User();
        u.setUsername(username);
        u.setSenha(senha);
        u.setNome(nome);
        u.setSobrenome(sobrenome);
        u.setEmail(email);
        u.setTelefone(telefone);
        u.setTipo("usuario");
        u.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        System.out.println("\nUsuario cadastrado com sucesso!");
        System.out.println("Dados salvos em: database/users/users.json");
    }

    // ===== LOGIN =====
    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        // Buscar usuário por email
        var userOpt = db.findUserByEmail(email);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            // Verificar senha
            if (senha.equals(u.getSenha())) {
                usuarioLogado = u;
                System.out.println("Login bem-sucedido! Bem-vindo, " + u.getNome());
                System.out.println("Dados carregados de: database/users/users.json");
            } else {
                System.out.println("Senha incorreta!");
            }
        } else {
            System.out.println("Email não encontrado!");
        }
    }

    // ===== MENU PRINCIPAL (APOS LOGIN) =====
    private static void menuPrincipal() {
        MenuPrincipalCLI menuPrincipal = new MenuPrincipalCLI(sc, db, usuarioLogado);
        menuPrincipal.exibirMenu();
        usuarioLogado = menuPrincipal.getUsuarioLogado(); // caso o usuário faça logout
    }
}