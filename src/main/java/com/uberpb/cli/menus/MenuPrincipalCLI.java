package com.uberpb.cli.menus;

import com.uberpb.model.*;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.helpers.ValidadoresCadastro;
import com.uberpb.services.CorridaService;

import java.util.Optional;
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
                // Verificar se CNH já existe antes de aceitar
                var existingMotorista = db.findMotoristaByCnh(input);
                if (existingMotorista.isPresent()) {
                    System.out.println("❌ Erro: CNH " + input + " já está cadastrada!");
                    continue;
                }
                cnh = input;
                System.out.println("✓ CNH válida!");
            }
        }

        while (validade == null) {
            System.out.print("Validade da CNH (dd/mm/yyyy): ");
            String input = sc.nextLine();
            if (ValidadoresCadastro.validarDataValidade(input)) {
                validade = input;
                System.out.println("✓ Validade!");
            }
        }

        Motorista m = new Motorista(usuarioLogado.getId(), true, cnh, validade, 0.0, 0, true, "Nao definida",
                "Nao definida");
        m.setUsername(usuarioLogado.getUsername());
        m.setSenha(usuarioLogado.getSenha());
        m.setNome(usuarioLogado.getNome());
        m.setSobrenome(usuarioLogado.getSobrenome());
        m.setEmail(usuarioLogado.getEmail());
        m.setTelefone(usuarioLogado.getTelefone());
        m.setTipo("motorista");
        m.setDataCadastro(usuarioLogado.getDataCadastro());

        db.saveMotorista(m);
    }

    private void menuPassageiro() {
        var passageiroOpt = db.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de passageiro. Cadastre primeiro.");
            return;
        }

        Passageiro passageiro = passageiroOpt.get();

        // Lógica de avaliação de corrida pendente (deve ser antes do menu)
        CorridaService corridaService = new CorridaService();
        Optional<Corrida> corridaAvaliacao = corridaService.obterCorridaAtivaPassageiro(passageiro.getId())
                .filter(c -> c.getStatus() == com.uberpb.enums.CorridaStatus.AVALIACAO && !c.isAvaliada_passageiro());
        if (corridaAvaliacao.isPresent()) {
            Corrida corrida = corridaAvaliacao.get();
            System.out.println("\nVocê possui uma corrida anterior aguardando avaliação do motorista!");
            System.out.println("Origem: " + corrida.getOrigem() + " | Destino: " + corrida.getDestino());
            System.out.print("Deseja avaliar o motorista agora? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("s") || resp.equals("sim")) {
                com.uberpb.cli.forms.AvaliarMotoristaCLI avaliarMenu = new com.uberpb.cli.forms.AvaliarMotoristaCLI(sc,
                        corrida, db);
                avaliarMenu.exibirMenu();
                corridaService.updateCorrida(corrida);
                // Se ambos avaliaram, finalizar
                if (corrida.isAvaliada_passageiro() && corrida.isAvaliada_motorista()) {
                    corrida.setStatus(com.uberpb.enums.CorridaStatus.FINALIZADA);
                    corridaService.updateCorrida(corrida);
                }
            }
        }

        MenuPassageiroCLI menuPassageiro = new MenuPassageiroCLI(sc, db, passageiro);
        menuPassageiro.exibirMenu();
    }

    private void menuMotorista() {
        var motoristaOpt = db.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de motorista. Cadastre primeiro.");
            return;
        }

        Motorista motorista = motoristaOpt.get();

        // Lógica de avaliação de corrida pendente (deve ser antes do menu)
        CorridaService corridaService = new CorridaService();
        Optional<Corrida> corridaAvaliacao = corridaService.obterCorridaAtivaMotorista(motorista.getId())
                .filter(c -> c.getStatus() == com.uberpb.enums.CorridaStatus.AVALIACAO && !c.isAvaliada_motorista());
        if (corridaAvaliacao.isPresent()) {
            Corrida corrida = corridaAvaliacao.get();
            System.out.println("\nVocê possui uma corrida anterior aguardando avaliação do passageiro!");
            System.out.println("Origem: " + corrida.getOrigem() + " | Destino: " + corrida.getDestino());
            System.out.print("Deseja avaliar o passageiro agora? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("s") || resp.equals("sim")) {
                com.uberpb.cli.forms.AvaliarPassageiroCLI avaliarMenu = new com.uberpb.cli.forms.AvaliarPassageiroCLI(sc, corrida, db);
                avaliarMenu.exibirMenu();
                corridaService.updateCorrida(corrida);
                // Se ambos avaliaram, finalizar
                if (corrida.isAvaliada_passageiro() && corrida.isAvaliada_motorista()) {
                    corrida.setStatus(com.uberpb.enums.CorridaStatus.FINALIZADA);
                    corridaService.updateCorrida(corrida);
                }
            }
        }

        MenuMotoristaCLI menuMotorista = new MenuMotoristaCLI(sc, db, motorista);
        menuMotorista.exibirMenu();
    }
}
