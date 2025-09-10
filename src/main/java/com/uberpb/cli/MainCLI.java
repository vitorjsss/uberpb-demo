package com.uberpb.cli;

import com.uberpb.model.*;
import com.uberpb.repository.DatabaseManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainCLI {

    private static Scanner sc = new Scanner(System.in);
    private static DatabaseManager db = new DatabaseManager();
    private static User usuarioLogado = null;
    
    // Padrão para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

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
            if (validarCampoObrigatorio(input, "Username")) {
                username = input;
                System.out.println("✓ Username válido!");
            }
        }
        
        // Senha
        while (senha == null) {
            System.out.print("Senha: ");
            String input = sc.nextLine();
            if (validarSenha(input)) {
                senha = input;
                System.out.println("✓ Senha válida!");
            }
        }
        
        // Nome
        while (nome == null) {
            System.out.print("Nome: ");
            String input = sc.nextLine();
            if (validarNome(input)) {
                nome = input;
                System.out.println("✓ Nome válido!");
            }
        }
        
        // Sobrenome
        while (sobrenome == null) {
            System.out.print("Sobrenome: ");
            String input = sc.nextLine();
            if (validarCampoObrigatorio(input, "Sobrenome")) {
                sobrenome = input;
                System.out.println("✓ Sobrenome válido!");
            }
        }
        
        // Email
        while (email == null) {
            System.out.print("Email: ");
            String input = sc.nextLine();
            if (validarEmail(input)) {
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
            if (validarTelefone(input)) {
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

        User savedUser = db.saveUser(u);

        System.out.println("\n🎉 Usuario cadastrado com sucesso!");
        System.out.println("ID do usuário: " + savedUser.getId());
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
        System.out.println("\n=== Menu Principal ===");
        System.out.println("Usuario logado: " + usuarioLogado.getNome() + " (" + usuarioLogado.getEmail() + ")");
        System.out.println("1 - Cadastrar perfil de Passageiro");
        System.out.println("2 - Cadastrar perfil de Motorista");
        System.out.println("3 - Menu Passageiro");
        System.out.println("4 - Menu Motorista");
        System.out.println("5 - Ver estatísticas do banco de dados");
        System.out.println("9 - Logout");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1 -> cadastrarPerfilPassageiro();
            case 2 -> cadastrarPerfilMotorista();
            case 3 -> menuPassageiro();
            case 4 -> menuMotorista();
            case 5 -> verEstatisticasBanco();
            case 9 -> {
                System.out.println("Saindo da conta...");
                usuarioLogado = null;
            }
            default -> System.out.println("Opcao invalida!");
        }
    }

    // ===== CADASTRO PERFIL PASSAGEIRO =====
    private static void cadastrarPerfilPassageiro() {
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
                
                if (validarIdade(inputIdade)) {
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
        System.out.println("ID do passageiro: " + savedPassageiro.getId());
        System.out.println("Dados salvos em: database/users/users.json");
        System.out.println("Dados salvos em: database/passageiros/passageiros.json");
    }

    // ===== CADASTRO PERFIL MOTORISTA COM VALIDAÇÕES =====
    private static void cadastrarPerfilMotorista() {
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
            if (validarCNH(input)) {
                cnh = input;
                System.out.println("✓ CNH válida!");
            }
        }
        
        // Validade da CNH
        while (validade == null) {
            System.out.print("Validade da CNH (dd/mm/yyyy): ");
            String input = sc.nextLine();
            if (validarDataValidade(input)) {
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
        System.out.println("ID do motorista: " + savedMotorista.getId());
        System.out.println("Dados salvos em: database/users/users.json");
        System.out.println("Dados salvos em: database/motoristas/motoristas.json");
    }

    // ===== MENU PASSAGEIRO =====
    private static void menuPassageiro() {
        var passageiroOpt = db.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de passageiro. Cadastre primeiro.");
            return;
        }
        
        Passageiro p = passageiroOpt.get();

        while (true) {
            System.out.println("\n=== Menu Passageiro ===");
            System.out.println("1 - Cadastrar metodo de pagamento");
            System.out.println("2 - Realizar corrida");
            System.out.println("3 - Ver avaliacao media");
            System.out.println("4 - Ver historico de corridas");
            System.out.println("5 - Ver localizacao atual");
            System.out.println("6 - Ver status (em corrida ou nao)");
            System.out.println("7 - Atualizar localizacao");
            System.out.println("8 - Ver informacoes do perfil");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrarMetodoPagamento(p);
                case 2 -> System.out.println("Funcionalidade de corrida em desenvolvimento...");
                case 3 -> verAvaliacaoMedia(p);
                case 4 -> verHistoricoCorridas(p);
                case 5 -> verLocalizacaoAtual(p);
                case 6 -> verStatusCorrida(p);
                case 7 -> atualizarLocalizacao(p);
                case 8 -> verInformacoesPerfil(p);
                case 9 -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    // ===== MENU MOTORISTA =====
    private static void menuMotorista() {
        var motoristaOpt = db.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isEmpty()) {
            System.out.println("Voce ainda nao possui perfil de motorista. Cadastre primeiro.");
            return;
        }
        
        Motorista m = motoristaOpt.get();

        while (true) {
            System.out.println("\n=== Menu Motorista ===");
            System.out.println("1 - Ver status ativo");
            System.out.println("2 - Ver avaliacao media");
            System.out.println("3 - Ver total de avaliacoes");
            System.out.println("4 - Ver localizacao atual");
            System.out.println("5 - Atualizar localizacao");
            System.out.println("6 - Ver informacoes do perfil");
            System.out.println("7 - Ver CNH e validade");
            System.out.println("8 - Ver status disponibilidade");
            System.out.println("9 - Voltar");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> verStatusAtivo(m);
                case 2 -> verAvaliacaoMediaMotorista(m);
                case 3 -> verTotalAvaliacoes(m);
                case 4 -> verLocalizacaoAtualMotorista(m);
                case 5 -> atualizarLocalizacaoMotorista(m);
                case 6 -> verInformacoesPerfilMotorista(m);
                case 7 -> verCnhValidade(m);
                case 8 -> verStatusDisponibilidade(m);
                case 9 -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }
    
    // ===== MÉTODO PARA VER ESTATÍSTICAS DO BANCO =====
    
    private static void verEstatisticasBanco() {
        System.out.println("\n--- Estatísticas do Banco de Dados ---");
        System.out.println(db.getDatabaseStats());
        System.out.println("\nEstrutura dos arquivos JSON:");
        System.out.println("database/");
        System.out.println("├── users/users.json (dados básicos de todos os usuários)");
        System.out.println("├── passageiros/passageiros.json (dados específicos dos passageiros)");
        System.out.println("├── motoristas/motoristas.json (dados específicos dos motoristas)");
        System.out.println("├── veiculos/veiculos.json (dados dos veículos)");
        System.out.println("└── id_counter.json (contadores de ID para cada entidade)");
    }
    
    // ===== MÉTODOS DO MENU PASSAGEIRO =====
    
    private static void cadastrarMetodoPagamento(Passageiro p) {
        System.out.println("\n--- Cadastrar Método de Pagamento ---");
        System.out.print("Digite o método de pagamento: ");
        String metodo = sc.nextLine();
        
        if (metodo != null && !metodo.trim().isEmpty()) {
            p.getMetodosPagamento().add(metodo.trim());
            db.updatePassageiro(p);
            System.out.println("✓ Método de pagamento cadastrado com sucesso!");
            System.out.println("Dados atualizados em: database/passageiros/passageiros.json");
        } else {
            System.out.println("ERRO: Método de pagamento não pode estar vazio!");
        }
    }
    
    private static void verAvaliacaoMedia(Passageiro p) {
        System.out.println("\n--- Avaliação Média ---");
        System.out.println("Avaliação média: " + p.getAvaliacaoMedia() + " ⭐");
    }
    
    private static void verHistoricoCorridas(Passageiro p) {
        System.out.println("\n--- Histórico de Corridas ---");
        if (p.getHistoricoCorridas().isEmpty()) {
            System.out.println("Nenhuma corrida realizada ainda.");
        } else {
            for (int i = 0; i < p.getHistoricoCorridas().size(); i++) {
                System.out.println((i + 1) + ". " + p.getHistoricoCorridas().get(i));
            }
        }
    }
    
    private static void verLocalizacaoAtual(Passageiro p) {
        System.out.println("\n--- Localização Atual ---");
        System.out.println("Localização: " + p.getLocalizacaoAtual());
    }
    
    private static void verStatusCorrida(Passageiro p) {
        System.out.println("\n--- Status da Corrida ---");
        System.out.println("Status: " + (p.isEmCorrida() ? "Em corrida" : "Disponível"));
    }
    
    private static void atualizarLocalizacao(Passageiro p) {
        System.out.println("\n--- Atualizar Localização ---");
        System.out.print("Digite a nova localização: ");
        String localizacao = sc.nextLine();
        
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            p.setLocalizacaoAtual(localizacao.trim());
            db.updatePassageiro(p);
            System.out.println("✓ Localização atualizada com sucesso!");
            System.out.println("Dados atualizados em: database/passageiros/passageiros.json");
        } else {
            System.out.println("ERRO: Localização não pode estar vazia!");
        }
    }
    
    private static void verInformacoesPerfil(Passageiro p) {
        System.out.println("\n--- Informações do Perfil ---");
        System.out.println("ID: " + p.getId());
        System.out.println("Nome: " + p.getNome() + " " + p.getSobrenome());
        System.out.println("Email: " + p.getEmail());
        System.out.println("Telefone: " + p.getTelefone());
        System.out.println("Idade: " + p.getIdade() + " anos");
        System.out.println("Localização: " + p.getLocalizacaoAtual());
        System.out.println("Avaliação média: " + p.getAvaliacaoMedia() + " ⭐");
        System.out.println("Status: " + (p.isEmCorrida() ? "Em corrida" : "Disponível"));
        System.out.println("Métodos de pagamento: " + p.getMetodosPagamento());
    }
    
    // ===== MÉTODOS DO MENU MOTORISTA =====
    
    private static void verStatusAtivo(Motorista m) {
        System.out.println("\n--- Status Ativo ---");
        System.out.println("Status: " + (m.isAtivo() ? "Ativo" : "Inativo"));
    }
    
    private static void verAvaliacaoMediaMotorista(Motorista m) {
        System.out.println("\n--- Avaliação Média ---");
        System.out.println("Avaliação média: " + m.getAvaliacaoMedia() + " ⭐");
    }
    
    private static void verTotalAvaliacoes(Motorista m) {
        System.out.println("\n--- Total de Avaliações ---");
        System.out.println("Total de avaliações: " + m.getTotalAvaliacoes());
    }
    
    private static void verLocalizacaoAtualMotorista(Motorista m) {
        System.out.println("\n--- Localização Atual ---");
        System.out.println("Localização: " + m.getLocalizacaoAtual());
    }
    
    private static void atualizarLocalizacaoMotorista(Motorista m) {
        System.out.println("\n--- Atualizar Localização ---");
        System.out.print("Digite a nova localização: ");
        String localizacao = sc.nextLine();
        
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            m.setLocalizacaoAtual(localizacao.trim());
            db.updateMotorista(m);
            System.out.println("✓ Localização atualizada com sucesso!");
            System.out.println("Dados atualizados em: database/motoristas/motoristas.json");
        } else {
            System.out.println("ERRO: Localização não pode estar vazia!");
        }
    }
    
    private static void verInformacoesPerfilMotorista(Motorista m) {
        System.out.println("\n--- Informações do Perfil ---");
        System.out.println("ID: " + m.getId());
        System.out.println("Nome: " + m.getNome() + " " + m.getSobrenome());
        System.out.println("Email: " + m.getEmail());
        System.out.println("Telefone: " + m.getTelefone());
        System.out.println("CNH: " + m.getCnh());
        System.out.println("Validade CNH: " + m.getValidadeCnh());
        System.out.println("Localização: " + m.getLocalizacaoAtual());
        System.out.println("Avaliação média: " + m.getAvaliacaoMedia() + " ⭐");
        System.out.println("Total de avaliações: " + m.getTotalAvaliacoes());
        System.out.println("Status: " + (m.isAtivo() ? "Ativo" : "Inativo"));
        System.out.println("Disponibilidade: " + (m.isDisponivel() ? "Disponível" : "Indisponível"));
    }
    
    private static void verCnhValidade(Motorista m) {
        System.out.println("\n--- CNH e Validade ---");
        System.out.println("CNH: " + m.getCnh());
        System.out.println("Validade: " + m.getValidadeCnh());
    }
    
    private static void verStatusDisponibilidade(Motorista m) {
        System.out.println("\n--- Status de Disponibilidade ---");
        System.out.println("Disponibilidade: " + (m.isDisponivel() ? "Disponível" : "Indisponível"));
    }
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    private static boolean validarCampoObrigatorio(String campo, String nomeCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            System.out.println("ERRO: " + nomeCampo + " é obrigatório!");
            return false;
        }
        return true;
    }
    
    private static boolean validarEmail(String email) {
        if (!validarCampoObrigatorio(email, "Email")) return false;
        
        String emailLimpo = email.trim();
        if (!EMAIL_PATTERN.matcher(emailLimpo).matches()) {
            System.out.println("ERRO: Formato de e-mail inválido! Use formato: exemplo@dominio.com");
            return false;
        }
        return true;
    }
    
    private static boolean validarSenha(String senha) {
        if (!validarCampoObrigatorio(senha, "Senha")) return false;
        
        String senhaLimpa = senha.trim();
        
        if (senhaLimpa.length() < 5) {
            System.out.println("ERRO: Senha deve ter no mínimo 5 caracteres!");
            return false;
        }
        
        if (!senhaLimpa.chars().anyMatch(Character::isUpperCase)) {
            System.out.println("ERRO: Senha deve conter pelo menos 1 caractere maiúsculo!");
            return false;
        }
        
        if (!senhaLimpa.chars().anyMatch(Character::isDigit)) {
            System.out.println("ERRO: Senha deve conter pelo menos 1 número!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarNome(String nome) {
        if (!validarCampoObrigatorio(nome, "Nome")) return false;
        
        String nomeLimpo = nome.trim();
        if (nomeLimpo.matches("^[0-9]+$")) {
            System.out.println("ERRO: Nome não pode conter apenas números!");
            return false;
        }
        
        if (nomeLimpo.length() < 2) {
            System.out.println("ERRO: Nome deve ter pelo menos 2 caracteres!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarTelefone(String telefone) {
        if (!validarCampoObrigatorio(telefone, "Telefone")) return false;
        
        String telefoneLimpo = telefone.trim().replaceAll("[\\s()-]", "");
        
        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            System.out.println("ERRO: Telefone deve ter 10 ou 11 dígitos!");
            return false;
        }
        
        if (!telefoneLimpo.matches("^[0-9]+$")) {
            System.out.println("ERRO: Telefone deve conter apenas números!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida idade do passageiro (validação genérica)
     */
    private static boolean validarIdade(int idade) {
        if (idade < 0) {
            System.out.println("ERRO: Idade não pode ser negativa!");
            return false;
        }
        
        if (idade > 120) {
            System.out.println("ERRO: Idade não pode ser maior que 120 anos!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida CNH do motorista
     */
    private static boolean validarCNH(String cnh) {
        if (!validarCampoObrigatorio(cnh, "CNH")) return false;
        
        String cnhLimpa = cnh.trim().replaceAll("[\\s.-]", "");
        
        // CNH deve ter exatamente 11 dígitos
        if (cnhLimpa.length() != 11) {
            System.out.println("ERRO: CNH deve ter exatamente 11 dígitos!");
            return false;
        }
        
        if (!cnhLimpa.matches("^[0-9]+$")) {
            System.out.println("ERRO: CNH deve conter apenas números!");
            return false;
        }
        
        // Verificar se não são todos os dígitos iguais
        if (cnhLimpa.matches("(.)\\1{10}")) {
            System.out.println("ERRO: CNH não pode ter todos os dígitos iguais!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida data de validade da CNH
     */
    private static boolean validarDataValidade(String data) {
        if (!validarCampoObrigatorio(data, "Data de validade")) return false;
        
        String dataLimpa = data.trim();
        
        // Verificar formato dd/mm/aaaa
        if (!dataLimpa.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            System.out.println("ERRO: Data deve estar no formato dd/mm/aaaa (ex: 15/12/2025)!");
            return false;
        }
        
        String[] partes = dataLimpa.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        
        // Validar dia
        if (dia < 1 || dia > 31) {
            System.out.println("ERRO: Dia deve estar entre 01 e 31!");
            return false;
        }
        
        // Validar mês
        if (mes < 1 || mes > 12) {
            System.out.println("ERRO: Mês deve estar entre 01 e 12!");
            return false;
        }
        
        // Validar ano (não pode ser muito antigo nem muito futuro)
        int anoAtual = 2024; // ou use Calendar.getInstance().get(Calendar.YEAR)
        if (ano < anoAtual) {
            System.out.println("ERRO: CNH não pode estar vencida!");
            return false;
        }
        
        if (ano > anoAtual + 10) {
            System.out.println("ERRO: Data de validade muito distante no futuro!");
            return false;
        }
        
        // Validações específicas de dias por mês
        if (mes == 2) { // Fevereiro
            boolean bissexto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
            if (dia > (bissexto ? 29 : 28)) {
                System.out.println("ERRO: Fevereiro não pode ter mais de " + (bissexto ? "29" : "28") + " dias!");
                return false;
            }
        } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) { // Meses com 30 dias
            if (dia > 30) {
                System.out.println("ERRO: Este mês só pode ter até 30 dias!");
                return false;
            }
        }
        
        return true;
    }
}