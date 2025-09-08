package com.uberpb;

import com.uberpb.model.User;
import com.uberpb.repository.UsuarioRepository;
import com.uberpb.repository.UsuarioRepositoryJSON;

import java.util.List;
import java.util.Optional;

/**
 * Demonstração do uso do UsuarioRepositoryJSON
 */
import com.uberpb.model.*;
import java.util.Scanner;
import java.util.List;

public class App {

    // Simula repositório de memória
    private static PassageiroService passageiroService = new PassageiroService();
    private static MotoristaService motoristaService = new MotoristaService();
    private static int nextId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Listar Passageiros");
            System.out.println("3 - Listar Motoristas");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarUsuario(scanner);
                case 2 -> listarPassageiros();
                case 3 -> listarMotoristas();
                case 0 -> {
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarUsuario(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE USUÁRIO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sobrenome: ");
        String sobrenome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Tipo (PASSAGEIRO/MOTORISTA): ");
        String tipo = scanner.nextLine().toUpperCase();

        int id = nextId++;
        switch (tipo) {
            case "PASSAGEIRO" -> {
                Passageiro p = new Passageiro(id, "Local padrão", false);
                p.setNome(nome);
                p.setSobrenome(sobrenome);
                p.setEmail(email);
                p.setTelefone(telefone);
                p.setSenha(senha);
                p.setTipo("PASSAGEIRO");

                try {
                    passageiroService.cadastrar(p);
                    System.out.println("Passageiro cadastrado com sucesso!");
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
            case "MOTORISTA" -> {
                Motorista m = new Motorista(id, true, "CNH0000", "31/12/2030", 0.0, 0, true, "Local padrão");
                m.setNome(nome);
                m.setSobrenome(sobrenome);
                m.setEmail(email);
                m.setTelefone(telefone);
                m.setSenha(senha);
                m.setTipo("MOTORISTA");

                try {
                    motoristaService.cadastrar(m);
                    System.out.println("Motorista cadastrado com sucesso!");
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
            default -> System.out.println("Tipo inválido. Cadastro cancelado.");
        }
    }

    private static void listarPassageiros() {
        System.out.println("\n--- LISTA DE PASSAGEIROS ---");
        List<Passageiro> passageiros = passageiroService.listar();
        if (passageiros.isEmpty()) {
            System.out.println("Nenhum passageiro cadastrado.");
        } else {
            passageiros.forEach(System.out::println);
        }
    }

    private static void listarMotoristas() {
        System.out.println("\n--- LISTA DE MOTORISTAS ---");
        List<Motorista> motoristas = motoristaService.listar();
        if (motoristas.isEmpty()) {
            System.out.println("Nenhum motorista cadastrado.");
        } else {
            motoristas.forEach(System.out::println);
        }
    }
}
