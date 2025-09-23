package com.uberpb.cli.menus;

import com.uberpb.model.User;
import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.helpers.ValidadoresCadastro;
import com.uberpb.service.CorridaService;
import com.uberpb.model.Corrida;
import com.uberpb.model.CorridaStatus;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipalCLI {

    private Scanner sc;
    private DatabaseManager db;
    private User usuarioLogado;
    private CorridaService corridaService;

    public MenuPrincipalCLI(Scanner sc, DatabaseManager db, User usuarioLogado) {
        this.sc = sc;
        this.db = db;
        this.usuarioLogado = usuarioLogado;
        this.corridaService = new CorridaService();
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
        var passageiroOpt = db.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isPresent()) {
            System.out.println("Voce ja possui perfil de passageiro.");
            return;
        }

        System.out.println("\n--- Cadastro de Perfil Passageiro ---");

        int idade = -1;
        while (idade == -1) {
            System.out.print("Idade: ");
            try {
                int inputIdade = sc.nextInt();
                sc.nextLine();
                if (ValidadoresCadastro.validarIdade(inputIdade)) {
                    if (inputIdade >= 18) {
                        idade = inputIdade;
                        System.out.println("✓ Idade válida!");
                    } else {
                        System.out.println("ERRO: Passageiro deve ser maior de 18 anos!");
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO: Digite uma idade válida!");
                sc.nextLine();
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

        db.savePassageiro(p);
        System.out.println("\n🎉 Perfil de passageiro cadastrado com sucesso!");
    }

    private void cadastrarPerfilMotorista() {
        var motoristaOpt = db.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isPresent()) {
            System.out.println("Voce ja possui perfil de motorista.");
            return;
        }

        System.out.println("\n--- Cadastro de Perfil Motorista ---");

        String cnh = null;
        String validade = null;

        while (cnh == null) {
            System.out.print("CNH (11 dígitos): ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarCNH(input)) {
                cnh = input;
                System.out.println("✓ CNH válida!");
            }
        }

        while (validade == null) {
            System.out.print("Validade da CNH (dd/mm/yyyy): ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarDataValidade(input)) {
                validade = input;
                System.out.println("✓ Validade válida!");
            }
        }

        Motorista m = new Motorista(usuarioLogado.getId(), true, cnh, validade, 0.0, 0, true, "Nao definida");
        m.setUsername(usuarioLogado.getUsername());
        m.setSenha(usuarioLogado.getSenha());
        m.setNome(usuarioLogado.getNome());
        m.setSobrenome(usuarioLogado.getSobrenome());
        m.setEmail(usuarioLogado.getEmail());
        m.setTelefone(usuarioLogado.getTelefone());
        m.setTipo("motorista");
        m.setDataCadastro(usuarioLogado.getDataCadastro());

        db.saveMotorista(m);
        System.out.println("\n🎉 Perfil de motorista cadastrado com sucesso!");
    }

    private void menuPassageiro() {
        var passageiroOpt = db.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de passageiro. Cadastre primeiro.");
            return;
        }
        Passageiro p = passageiroOpt.get();

        while (true) {
            System.out.println("\n--- Menu Passageiro ---");
            System.out.println("1 - Solicitar corrida");
            System.out.println("2 - Listar minhas corridas");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> solicitarCorrida(p);
                case 2 -> listarCorridasPassageiro(p);
                case 9 -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private void solicitarCorrida(Passageiro p) {
        System.out.print("Origem: ");
        String origem = sc.nextLine();
        System.out.print("Destino: ");
        String destino = sc.nextLine();
        System.out.print("ID Motorista: ");
        int motoristaId = sc.nextInt();
        System.out.print("Distancia (km): ");
        double distancia = sc.nextDouble();
        sc.nextLine();

        Corrida corrida = corridaService.criarCorrida(origem, destino, p.getId(), motoristaId, distancia);
        System.out.println("🎉 Corrida solicitada! ID: " + corrida.getId());
    }

    private void listarCorridasPassageiro(Passageiro p) {
        List<Corrida> corridas = corridaService.listarCorridasPorPassageiro(p.getId());
        if (corridas.isEmpty()) {
            System.out.println("Nenhuma corrida encontrada.");
            return;
        }
        System.out.println("\n--- Minhas Corridas ---");
        for (Corrida c : corridas) {
            System.out.println("ID: " + c.getId() + " | Origem: " + c.getOrigem() +
                    " | Destino: " + c.getDestino() + " | Status: " + c.getStatus());
        }
    }

    private void menuMotorista() {
        var motoristaOpt = db.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de motorista. Cadastre primeiro.");
            return;
        }
        Motorista m = motoristaOpt.get();

        while (true) {
            System.out.println("\n--- Menu Motorista ---");
            System.out.println("1 - Listar corridas pendentes");
            System.out.println("2 - Iniciar corrida");
            System.out.println("3 - Finalizar corrida");
            System.out.println("4 - Cancelar corrida");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> listarCorridasPendentes(m);
                case 2 -> atualizarStatusCorrida(m, "iniciar");
                case 3 -> atualizarStatusCorrida(m, "finalizar");
                case 4 -> atualizarStatusCorrida(m, "cancelar");
                case 9 -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private void listarCorridasPendentes(Motorista m) {
        List<Corrida> corridas = corridaService.listarCorridasPorMotorista(m.getId());
        boolean hasPendentes = false;
        for (Corrida c : corridas) {
            if (c.getStatus() == CorridaStatus.PENDENTE) {
                System.out.println("ID: " + c.getId() + " | Origem: " + c.getOrigem() +
                        " | Destino: " + c.getDestino());
                hasPendentes = true;
            }
        }
        if (!hasPendentes) {
            System.out.println("Nenhuma corrida pendente.");
        }
    }

    private void atualizarStatusCorrida(Motorista m, String acao) {
        System.out.print("ID da corrida: ");
        int corridaId = sc.nextInt();
        sc.nextLine();

        Corrida corrida = corridaService.getCorridaById(corridaId);
        if (corrida == null || corrida.getMotoristaId() != m.getId()) {
            System.out.println("Corrida nao encontrada para este motorista.");
            return;
        }

        switch (acao) {
            case "iniciar" -> corridaService.iniciarCorrida(corridaId);
            case "finalizar" -> corridaService.finalizarCorrida(corridaId);
            case "cancelar" -> corridaService.cancelarCorrida(corridaId);
        }

        System.out.println("Status da corrida atualizado: " + corrida.getStatus());
    }
}
