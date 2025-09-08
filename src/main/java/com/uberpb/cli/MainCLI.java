package com.uberpb.cli;

import com.uberpb.model.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainCLI {

    private static Scanner sc = new Scanner(System.in);
    private static RepositorioMemoria repo = new RepositorioMemoria();
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
                if (repo.buscarPorEmailSenha(input, "") != null) {
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

        repo.salvar(u);

        System.out.println("\n🎉 Usuario cadastrado com sucesso!");
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
        p.setIdade(idade);

        repo.associarPassageiro(usuarioLogado);
        System.out.println("\n🎉 Perfil de passageiro cadastrado com sucesso!");
    }

    // ===== CADASTRO PERFIL MOTORISTA COM VALIDAÇÕES =====
    private static void cadastrarPerfilMotorista() {
        if (repo.getMotorista(usuarioLogado) != null) {
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

        repo.associarMotorista(usuarioLogado, cnh, validade);
        
        System.out.println("\n🎉 Perfil de motorista cadastrado com sucesso!");
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