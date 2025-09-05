package com.uberpb;

import com.uberpb.model.Passageiro;
import com.uberpb.model.PassageiroService;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        PassageiroService service = new PassageiroService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- MENU PASSAGEIRO ---");
            System.out.println("1 - Cadastrar Passageiro");
            System.out.println("2 - Listar Passageiros");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Telefone: ");
                String telefone = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                try {
                    service.cadastrar(new Passageiro(nome, email, telefone, senha));
                    System.out.println("Passageiro cadastrado com sucesso!");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else if (opcao == 2) {
                service.listar().forEach(System.out::println);

            } else if (opcao == 0) {
                System.out.println("Saindo...");
                break;
            } else {
                System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }
}
