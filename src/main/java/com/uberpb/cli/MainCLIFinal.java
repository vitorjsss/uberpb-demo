package com.uberpb.cli;

import com.uberpb.model.*;
import com.uberpb.repository.SimpleJSONRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * MainCLI Final - Versão completa e perfeita do sistema UberPB
 * Com todas as funcionalidades e persistência completa
 */
public class MainCLIFinal {

    private static Scanner sc = new Scanner(System.in);
    private static SimpleJSONRepository repo = new SimpleJSONRepository();
    private static User usuarioLogado = null;
    
    // Padrão para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🚗 UBERPB - SISTEMA COMPLETO 🚗           ║");
        System.out.println("║                                                              ║");
        System.out.println("║  ✅ Cadastro de usuários com validações                     ║");
        System.out.println("║  ✅ Login seguro                                            ║");
        System.out.println("║  ✅ Perfis de passageiro e motorista                       ║");
        System.out.println("║  ✅ Persistência completa em arquivos                      ║");
        System.out.println("║  ✅ Menus funcionais com todas as operações                ║");
        System.out.println("║  ✅ Validações de dados robustas                           ║");
        System.out.println("║  ✅ Estatísticas em tempo real                             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
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
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🏠 MENU INICIAL");
        System.out.println("═".repeat(60));
        System.out.println("1️⃣  - Cadastrar novo usuário");
        System.out.println("2️⃣  - Fazer login");
        System.out.println("3️⃣  - Ver estatísticas do sistema");
        System.out.println("0️⃣  - Sair do sistema");
        System.out.println("═".repeat(60));
        System.out.print("👉 Escolha uma opção: ");
        
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1 -> cadastrarUser();
            case 2 -> login();
            case 3 -> verEstatisticasSistema();
            case 0 -> {
                System.out.println("\n👋 Obrigado por usar o UberPB! Até logo!");
                System.exit(0);
            }
            default -> {
                System.out.println("\n❌ Opção inválida! Tente novamente.");
                System.out.println("💡 Dica: Use apenas os números 1, 2, 3 ou 0");
            }
        }
    }

    // ===== CADASTRO DE USUARIO COM VALIDAÇÕES COMPLETAS =====
    private static void cadastrarUser() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📝 CADASTRO DE NOVO USUÁRIO");
        System.out.println("═".repeat(60));
        
        String username = null;
        String senha = null;
        String nome = null;
        String sobrenome = null;
        String email = null;
        String telefone = null;
        
        // Username
        while (username == null) {
            System.out.print("👤 Username: ");
            String input = sc.nextLine();
            if (validarCampoObrigatorio(input, "Username")) {
                username = input;
                System.out.println("✅ Username válido!");
            }
        }
        
        // Senha
        while (senha == null) {
            System.out.print("🔒 Senha: ");
            String input = sc.nextLine();
            if (validarSenha(input)) {
                senha = input;
                System.out.println("✅ Senha válida!");
            }
        }
        
        // Nome
        while (nome == null) {
            System.out.print("📛 Nome: ");
            String input = sc.nextLine();
            if (validarNome(input)) {
                nome = input;
                System.out.println("✅ Nome válido!");
            }
        }
        
        // Sobrenome
        while (sobrenome == null) {
            System.out.print("📛 Sobrenome: ");
            String input = sc.nextLine();
            if (validarCampoObrigatorio(input, "Sobrenome")) {
                sobrenome = input;
                System.out.println("✅ Sobrenome válido!");
            }
        }
        
        // Email
        while (email == null) {
            System.out.print("📧 Email: ");
            String input = sc.nextLine();
            if (validarEmail(input)) {
                // Verificar email duplicado
                if (repo.findUserByEmail(input).isPresent()) {
                    System.out.println("❌ ERRO: Já existe um usuário com este e-mail!");
                } else {
                    email = input;
                    System.out.println("✅ Email válido!");
                }
            }
        }
        
        // Telefone
        while (telefone == null) {
            System.out.print("📱 Telefone: ");
            String input = sc.nextLine();
            if (validarTelefone(input)) {
                telefone = input;
                System.out.println("✅ Telefone válido!");
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

        User savedUser = repo.saveUser(u);

        System.out.println("\n" + "🎉".repeat(20));
        System.out.println("🎉 USUÁRIO CADASTRADO COM SUCESSO! 🎉");
        System.out.println("🎉".repeat(20));
        System.out.println("🆔 ID do usuário: " + savedUser.getId());
        System.out.println("💾 Dados salvos em: database/users/users.txt");
        System.out.println("⏰ Data de cadastro: " + savedUser.getDataCadastro());
    }

    // ===== LOGIN SEGURO =====
    private static void login() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🔐 LOGIN NO SISTEMA");
        System.out.println("═".repeat(60));
        
        System.out.print("📧 Email: ");
        String email = sc.nextLine();
        System.out.print("🔒 Senha: ");
        String senha = sc.nextLine();

        // Buscar usuário por email
        var userOpt = repo.findUserByEmail(email);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            // Verificar senha
            if (senha.equals(u.getSenha())) {
                usuarioLogado = u;
                System.out.println("\n" + "🎉".repeat(20));
                System.out.println("🎉 LOGIN REALIZADO COM SUCESSO! 🎉");
                System.out.println("🎉".repeat(20));
                System.out.println("👋 Bem-vindo, " + u.getNome() + "!");
                System.out.println("📧 Email: " + u.getEmail());
                System.out.println("💾 Dados carregados de: database/users/users.txt");
            } else {
                System.out.println("\n❌ Senha incorreta!");
                System.out.println("💡 Dica: Verifique se a senha está digitada corretamente");
            }
        } else {
            System.out.println("\n❌ Email não encontrado!");
            System.out.println("💡 Dica: Verifique se o email está correto ou cadastre-se primeiro");
        }
    }

    // ===== MENU PRINCIPAL (APOS LOGIN) =====
    private static void menuPrincipal() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🏠 MENU PRINCIPAL");
        System.out.println("═".repeat(60));
        System.out.println("👤 Usuário logado: " + usuarioLogado.getNome() + " (" + usuarioLogado.getEmail() + ")");
        System.out.println("═".repeat(60));
        System.out.println("1️⃣  - Cadastrar perfil de Passageiro");
        System.out.println("2️⃣  - Cadastrar perfil de Motorista");
        System.out.println("3️⃣  - Menu Passageiro");
        System.out.println("4️⃣  - Menu Motorista");
        System.out.println("5️⃣  - Ver estatísticas do banco de dados");
        System.out.println("6️⃣  - Ver informações da conta");
        System.out.println("9️⃣  - Logout");
        System.out.println("═".repeat(60));
        System.out.print("👉 Escolha uma opção: ");
        
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1 -> cadastrarPerfilPassageiro();
            case 2 -> cadastrarPerfilMotorista();
            case 3 -> menuPassageiro();
            case 4 -> menuMotorista();
            case 5 -> verEstatisticasBanco();
            case 6 -> verInformacoesConta();
            case 9 -> {
                System.out.println("\n👋 Saindo da conta... Até logo, " + usuarioLogado.getNome() + "!");
                usuarioLogado = null;
            }
            default -> {
                System.out.println("\n❌ Opção inválida! Tente novamente.");
                System.out.println("💡 Dica: Use apenas os números 1, 2, 3, 4, 5, 6 ou 9");
            }
        }
    }

    // ===== CADASTRO PERFIL PASSAGEIRO =====
    private static void cadastrarPerfilPassageiro() {
        // Verificar se já possui perfil de passageiro
        var passageiroOpt = repo.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isPresent()) {
            System.out.println("\n⚠️  Você já possui perfil de passageiro!");
            System.out.println("💡 Use o Menu Passageiro para gerenciar seu perfil");
            return;
        }
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🚶 CADASTRO DE PERFIL PASSAGEIRO");
        System.out.println("═".repeat(60));
        
        int idade = -1;
        // Validar idade (deve ser maior ou igual a 18)
        while (idade == -1) {
            System.out.print("🎂 Idade: ");
            try {
                int inputIdade = sc.nextInt();
                sc.nextLine();
                
                if (validarIdade(inputIdade)) {
                    if (inputIdade >= 18) {
                        idade = inputIdade;
                        System.out.println("✅ Idade válida!");
                    } else {
                        System.out.println("❌ ERRO: Passageiro deve ser maior de 18 anos!");
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ ERRO: Digite uma idade válida!");
                sc.nextLine(); // Limpar buffer
            }
        }

        Passageiro p = new Passageiro(usuarioLogado.getId(), "Não definida", false);
        p.setUsername(usuarioLogado.getUsername());
        p.setSenha(usuarioLogado.getSenha());
        p.setNome(usuarioLogado.getNome());
        p.setSobrenome(usuarioLogado.getSobrenome());
        p.setEmail(usuarioLogado.getEmail());
        p.setTelefone(usuarioLogado.getTelefone());
        p.setTipo("passageiro");
        p.setDataCadastro(usuarioLogado.getDataCadastro());
        p.setIdade(idade);

        Passageiro savedPassageiro = repo.savePassageiro(p);
        
        System.out.println("\n" + "🎉".repeat(20));
        System.out.println("🎉 PERFIL PASSAGEIRO CRIADO COM SUCESSO! 🎉");
        System.out.println("🎉".repeat(20));
        System.out.println("🆔 ID do passageiro: " + savedPassageiro.getId());
        System.out.println("🎂 Idade: " + savedPassageiro.getIdade() + " anos");
        System.out.println("💾 Dados salvos em: database/users/users.txt");
        System.out.println("💾 Dados salvos em: database/passageiros/passageiros.txt");
    }

    // ===== CADASTRO PERFIL MOTORISTA COM VALIDAÇÕES =====
    private static void cadastrarPerfilMotorista() {
        // Verificar se já possui perfil de motorista
        var motoristaOpt = repo.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isPresent()) {
            System.out.println("\n⚠️  Você já possui perfil de motorista!");
            System.out.println("💡 Use o Menu Motorista para gerenciar seu perfil");
            return;
        }
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🚗 CADASTRO DE PERFIL MOTORISTA");
        System.out.println("═".repeat(60));
        
        String cnh = null;
        String validade = null;
        
        // CNH - Validar formato (apenas números, 11 caracteres)
        while (cnh == null) {
            System.out.print("🆔 CNH (apenas números, 11 dígitos): ");
            String input = sc.nextLine();
            if (validarCNH(input)) {
                cnh = input;
                System.out.println("✅ CNH válida!");
            }
        }
        
        // Validade da CNH
        while (validade == null) {
            System.out.print("📅 Validade da CNH (dd/mm/yyyy): ");
            String input = sc.nextLine();
            if (validarDataValidade(input)) {
                validade = input;
                System.out.println("✅ Validade válida!");
            }
        }

        // Criar motorista com os dados
        Motorista m = new Motorista(usuarioLogado.getId(), true, cnh, validade, 0.0, 0, true, "Não definida");
        m.setUsername(usuarioLogado.getUsername());
        m.setSenha(usuarioLogado.getSenha());
        m.setNome(usuarioLogado.getNome());
        m.setSobrenome(usuarioLogado.getSobrenome());
        m.setEmail(usuarioLogado.getEmail());
        m.setTelefone(usuarioLogado.getTelefone());
        m.setTipo("motorista");
        m.setDataCadastro(usuarioLogado.getDataCadastro());
        
        Motorista savedMotorista = repo.saveMotorista(m);
        
        System.out.println("\n" + "🎉".repeat(20));
        System.out.println("🎉 PERFIL MOTORISTA CRIADO COM SUCESSO! 🎉");
        System.out.println("🎉".repeat(20));
        System.out.println("🆔 ID do motorista: " + savedMotorista.getId());
        System.out.println("🆔 CNH: " + savedMotorista.getCnh());
        System.out.println("📅 Validade CNH: " + savedMotorista.getValidadeCnh());
        System.out.println("💾 Dados salvos em: database/users/users.txt");
        System.out.println("💾 Dados salvos em: database/motoristas/motoristas.txt");
    }

    // ===== MENU PASSAGEIRO COMPLETO =====
    private static void menuPassageiro() {
        var passageiroOpt = repo.findPassageiroById(usuarioLogado.getId());
        if (passageiroOpt.isEmpty()) {
            System.out.println("\n⚠️  Você ainda não possui perfil de passageiro!");
            System.out.println("💡 Cadastre primeiro usando a opção 1 do Menu Principal");
            return;
        }
        
        Passageiro p = passageiroOpt.get();

        while (true) {
            System.out.println("\n" + "═".repeat(60));
            System.out.println("🚶 MENU PASSAGEIRO");
            System.out.println("═".repeat(60));
            System.out.println("👤 Passageiro: " + p.getNome() + " " + p.getSobrenome());
            System.out.println("═".repeat(60));
            System.out.println("1️⃣  - Cadastrar método de pagamento");
            System.out.println("2️⃣  - Realizar corrida");
            System.out.println("3️⃣  - Ver avaliação média");
            System.out.println("4️⃣  - Ver histórico de corridas");
            System.out.println("5️⃣  - Ver localização atual");
            System.out.println("6️⃣  - Ver status (em corrida ou não)");
            System.out.println("7️⃣  - Atualizar localização");
            System.out.println("8️⃣  - Ver informações completas do perfil");
            System.out.println("9️⃣  - Voltar ao Menu Principal");
            System.out.println("═".repeat(60));
            System.out.print("👉 Escolha uma opção: ");
            
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrarMetodoPagamento(p);
                case 2 -> realizarCorrida(p);
                case 3 -> verAvaliacaoMedia(p);
                case 4 -> verHistoricoCorridas(p);
                case 5 -> verLocalizacaoAtual(p);
                case 6 -> verStatusCorrida(p);
                case 7 -> atualizarLocalizacao(p);
                case 8 -> verInformacoesPerfilCompleto(p);
                case 9 -> { return; }
                default -> {
                    System.out.println("\n❌ Opção inválida! Tente novamente.");
                    System.out.println("💡 Dica: Use apenas os números de 1 a 9");
                }
            }
        }
    }

    // ===== MENU MOTORISTA COMPLETO =====
    private static void menuMotorista() {
        var motoristaOpt = repo.findMotoristaById(usuarioLogado.getId());
        if (motoristaOpt.isEmpty()) {
            System.out.println("\n⚠️  Você ainda não possui perfil de motorista!");
            System.out.println("💡 Cadastre primeiro usando a opção 2 do Menu Principal");
            return;
        }
        
        Motorista m = motoristaOpt.get();

        while (true) {
            System.out.println("\n" + "═".repeat(60));
            System.out.println("🚗 MENU MOTORISTA");
            System.out.println("═".repeat(60));
            System.out.println("👤 Motorista: " + m.getNome() + " " + m.getSobrenome());
            System.out.println("═".repeat(60));
            System.out.println("1️⃣  - Ver status ativo");
            System.out.println("2️⃣  - Ver avaliação média");
            System.out.println("3️⃣  - Ver total de avaliações");
            System.out.println("4️⃣  - Ver localização atual");
            System.out.println("5️⃣  - Atualizar localização");
            System.out.println("6️⃣  - Ver informações completas do perfil");
            System.out.println("7️⃣  - Ver CNH e validade");
            System.out.println("8️⃣  - Ver status de disponibilidade");
            System.out.println("9️⃣  - Voltar ao Menu Principal");
            System.out.println("═".repeat(60));
            System.out.print("👉 Escolha uma opção: ");
            
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> verStatusAtivo(m);
                case 2 -> verAvaliacaoMediaMotorista(m);
                case 3 -> verTotalAvaliacoes(m);
                case 4 -> verLocalizacaoAtualMotorista(m);
                case 5 -> atualizarLocalizacaoMotorista(m);
                case 6 -> verInformacoesPerfilMotoristaCompleto(m);
                case 7 -> verCnhValidade(m);
                case 8 -> verStatusDisponibilidade(m);
                case 9 -> { return; }
                default -> {
                    System.out.println("\n❌ Opção inválida! Tente novamente.");
                    System.out.println("💡 Dica: Use apenas os números de 1 a 9");
                }
            }
        }
    }
    
    // ===== MÉTODOS DE ESTATÍSTICAS =====
    
    private static void verEstatisticasSistema() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📊 ESTATÍSTICAS DO SISTEMA");
        System.out.println("═".repeat(60));
        System.out.println(repo.getDatabaseStats());
        System.out.println("═".repeat(60));
        System.out.println("💾 Estrutura dos arquivos de persistência:");
        System.out.println("📁 database/");
        System.out.println("   ├── 📄 users/users.txt (dados básicos de todos os usuários)");
        System.out.println("   ├── 📄 passageiros/passageiros.txt (dados específicos dos passageiros)");
        System.out.println("   ├── 📄 motoristas/motoristas.txt (dados específicos dos motoristas)");
        System.out.println("   ├── 📄 veiculos/veiculos.txt (dados dos veículos)");
        System.out.println("   └── 📄 id_counter.txt (contador de ID)");
    }
    
    private static void verEstatisticasBanco() {
        verEstatisticasSistema();
    }
    
    private static void verInformacoesConta() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("👤 INFORMAÇÕES DA CONTA");
        System.out.println("═".repeat(60));
        System.out.println("🆔 ID: " + usuarioLogado.getId());
        System.out.println("👤 Nome: " + usuarioLogado.getNome() + " " + usuarioLogado.getSobrenome());
        System.out.println("📧 Email: " + usuarioLogado.getEmail());
        System.out.println("📱 Telefone: " + usuarioLogado.getTelefone());
        System.out.println("👤 Username: " + usuarioLogado.getUsername());
        System.out.println("🏷️  Tipo: " + usuarioLogado.getTipo());
        System.out.println("📅 Data de cadastro: " + usuarioLogado.getDataCadastro());
        
        // Verificar perfis
        var passageiroOpt = repo.findPassageiroById(usuarioLogado.getId());
        var motoristaOpt = repo.findMotoristaById(usuarioLogado.getId());
        
        System.out.println("\n📋 Perfis cadastrados:");
        if (passageiroOpt.isPresent()) {
            System.out.println("✅ Perfil de Passageiro - Ativo");
        } else {
            System.out.println("❌ Perfil de Passageiro - Não cadastrado");
        }
        
        if (motoristaOpt.isPresent()) {
            System.out.println("✅ Perfil de Motorista - Ativo");
        } else {
            System.out.println("❌ Perfil de Motorista - Não cadastrado");
        }
    }
    
    // ===== MÉTODOS DO MENU PASSAGEIRO =====
    
    private static void cadastrarMetodoPagamento(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("💳 CADASTRAR MÉTODO DE PAGAMENTO");
        System.out.println("═".repeat(60));
        System.out.print("💳 Digite o método de pagamento: ");
        String metodo = sc.nextLine();
        
        if (metodo != null && !metodo.trim().isEmpty()) {
            p.getMetodosPagamento().add(metodo.trim());
            repo.updatePassageiro(p);
            System.out.println("\n✅ Método de pagamento cadastrado com sucesso!");
            System.out.println("💾 Dados atualizados em: database/passageiros/passageiros.txt");
            System.out.println("💳 Métodos cadastrados: " + p.getMetodosPagamento());
        } else {
            System.out.println("\n❌ ERRO: Método de pagamento não pode estar vazio!");
        }
    }
    
    private static void realizarCorrida(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🚗 REALIZAR CORRIDA");
        System.out.println("═".repeat(60));
        System.out.println("🚧 Funcionalidade em desenvolvimento...");
        System.out.println("💡 Em breve você poderá:");
        System.out.println("   • Solicitar corridas");
        System.out.println("   • Escolher motoristas");
        System.out.println("   • Acompanhar em tempo real");
        System.out.println("   • Avaliar o serviço");
    }
    
    private static void verAvaliacaoMedia(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("⭐ AVALIAÇÃO MÉDIA");
        System.out.println("═".repeat(60));
        System.out.println("⭐ Avaliação média: " + p.getAvaliacaoMedia() + " estrelas");
        if (p.getAvaliacaoMedia() == 0.0) {
            System.out.println("💡 Você ainda não possui avaliações");
        }
    }
    
    private static void verHistoricoCorridas(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📋 HISTÓRICO DE CORRIDAS");
        System.out.println("═".repeat(60));
        if (p.getHistoricoCorridas().isEmpty()) {
            System.out.println("📭 Nenhuma corrida realizada ainda.");
            System.out.println("💡 Use a opção 'Realizar corrida' para começar!");
        } else {
            System.out.println("📋 Suas corridas:");
            for (int i = 0; i < p.getHistoricoCorridas().size(); i++) {
                System.out.println("   " + (i + 1) + ". " + p.getHistoricoCorridas().get(i));
            }
        }
    }
    
    private static void verLocalizacaoAtual(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📍 LOCALIZAÇÃO ATUAL");
        System.out.println("═".repeat(60));
        System.out.println("📍 Localização: " + p.getLocalizacaoAtual());
    }
    
    private static void verStatusCorrida(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🚦 STATUS DA CORRIDA");
        System.out.println("═".repeat(60));
        System.out.println("🚦 Status: " + (p.isEmCorrida() ? "🚗 Em corrida" : "✅ Disponível"));
    }
    
    private static void atualizarLocalizacao(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📍 ATUALIZAR LOCALIZAÇÃO");
        System.out.println("═".repeat(60));
        System.out.println("📍 Localização atual: " + p.getLocalizacaoAtual());
        System.out.print("📍 Digite a nova localização: ");
        String localizacao = sc.nextLine();
        
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            p.setLocalizacaoAtual(localizacao.trim());
            repo.updatePassageiro(p);
            System.out.println("\n✅ Localização atualizada com sucesso!");
            System.out.println("💾 Dados atualizados em: database/passageiros/passageiros.txt");
            System.out.println("📍 Nova localização: " + p.getLocalizacaoAtual());
        } else {
            System.out.println("\n❌ ERRO: Localização não pode estar vazia!");
        }
    }
    
    private static void verInformacoesPerfilCompleto(Passageiro p) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("👤 INFORMAÇÕES COMPLETAS DO PERFIL");
        System.out.println("═".repeat(60));
        System.out.println("🆔 ID: " + p.getId());
        System.out.println("👤 Nome: " + p.getNome() + " " + p.getSobrenome());
        System.out.println("📧 Email: " + p.getEmail());
        System.out.println("📱 Telefone: " + p.getTelefone());
        System.out.println("🎂 Idade: " + p.getIdade() + " anos");
        System.out.println("📍 Localização: " + p.getLocalizacaoAtual());
        System.out.println("⭐ Avaliação média: " + p.getAvaliacaoMedia() + " estrelas");
        System.out.println("🚦 Status: " + (p.isEmCorrida() ? "🚗 Em corrida" : "✅ Disponível"));
        System.out.println("💳 Métodos de pagamento: " + p.getMetodosPagamento());
        System.out.println("📅 Data de cadastro: " + p.getDataCadastro());
    }
    
    // ===== MÉTODOS DO MENU MOTORISTA =====
    
    private static void verStatusAtivo(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🚦 STATUS ATIVO");
        System.out.println("═".repeat(60));
        System.out.println("🚦 Status: " + (m.isAtivo() ? "✅ Ativo" : "❌ Inativo"));
    }
    
    private static void verAvaliacaoMediaMotorista(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("⭐ AVALIAÇÃO MÉDIA");
        System.out.println("═".repeat(60));
        System.out.println("⭐ Avaliação média: " + m.getAvaliacaoMedia() + " estrelas");
        System.out.println("📊 Total de avaliações: " + m.getTotalAvaliacoes());
    }
    
    private static void verTotalAvaliacoes(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📊 TOTAL DE AVALIAÇÕES");
        System.out.println("═".repeat(60));
        System.out.println("📊 Total de avaliações: " + m.getTotalAvaliacoes());
        System.out.println("⭐ Avaliação média: " + m.getAvaliacaoMedia() + " estrelas");
    }
    
    private static void verLocalizacaoAtualMotorista(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📍 LOCALIZAÇÃO ATUAL");
        System.out.println("═".repeat(60));
        System.out.println("📍 Localização: " + m.getLocalizacaoAtual());
    }
    
    private static void atualizarLocalizacaoMotorista(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📍 ATUALIZAR LOCALIZAÇÃO");
        System.out.println("═".repeat(60));
        System.out.println("📍 Localização atual: " + m.getLocalizacaoAtual());
        System.out.print("📍 Digite a nova localização: ");
        String localizacao = sc.nextLine();
        
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            m.setLocalizacaoAtual(localizacao.trim());
            repo.updateMotorista(m);
            System.out.println("\n✅ Localização atualizada com sucesso!");
            System.out.println("💾 Dados atualizados em: database/motoristas/motoristas.txt");
            System.out.println("📍 Nova localização: " + m.getLocalizacaoAtual());
        } else {
            System.out.println("\n❌ ERRO: Localização não pode estar vazia!");
        }
    }
    
    private static void verInformacoesPerfilMotoristaCompleto(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("👤 INFORMAÇÕES COMPLETAS DO PERFIL");
        System.out.println("═".repeat(60));
        System.out.println("🆔 ID: " + m.getId());
        System.out.println("👤 Nome: " + m.getNome() + " " + m.getSobrenome());
        System.out.println("📧 Email: " + m.getEmail());
        System.out.println("📱 Telefone: " + m.getTelefone());
        System.out.println("🆔 CNH: " + m.getCnh());
        System.out.println("📅 Validade CNH: " + m.getValidadeCnh());
        System.out.println("📍 Localização: " + m.getLocalizacaoAtual());
        System.out.println("⭐ Avaliação média: " + m.getAvaliacaoMedia() + " estrelas");
        System.out.println("📊 Total de avaliações: " + m.getTotalAvaliacoes());
        System.out.println("🚦 Status: " + (m.isAtivo() ? "✅ Ativo" : "❌ Inativo"));
        System.out.println("🚗 Disponibilidade: " + (m.isDisponivel() ? "✅ Disponível" : "❌ Indisponível"));
        System.out.println("📅 Data de cadastro: " + m.getDataCadastro());
    }
    
    private static void verCnhValidade(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🆔 CNH E VALIDADE");
        System.out.println("═".repeat(60));
        System.out.println("🆔 CNH: " + m.getCnh());
        System.out.println("📅 Validade: " + m.getValidadeCnh());
    }
    
    private static void verStatusDisponibilidade(Motorista m) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🚗 STATUS DE DISPONIBILIDADE");
        System.out.println("═".repeat(60));
        System.out.println("🚗 Disponibilidade: " + (m.isDisponivel() ? "✅ Disponível" : "❌ Indisponível"));
    }
    
    // ===== MÉTODOS DE VALIDAÇÃO COMPLETOS =====
    
    private static boolean validarCampoObrigatorio(String campo, String nomeCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            System.out.println("❌ ERRO: " + nomeCampo + " é obrigatório!");
            return false;
        }
        return true;
    }
    
    private static boolean validarEmail(String email) {
        if (!validarCampoObrigatorio(email, "Email")) return false;
        
        String emailLimpo = email.trim();
        if (!EMAIL_PATTERN.matcher(emailLimpo).matches()) {
            System.out.println("❌ ERRO: Formato de e-mail inválido!");
            System.out.println("💡 Use formato: exemplo@dominio.com");
            return false;
        }
        return true;
    }
    
    private static boolean validarSenha(String senha) {
        if (!validarCampoObrigatorio(senha, "Senha")) return false;
        
        String senhaLimpa = senha.trim();
        
        if (senhaLimpa.length() < 5) {
            System.out.println("❌ ERRO: Senha deve ter no mínimo 5 caracteres!");
            return false;
        }
        
        if (!senhaLimpa.chars().anyMatch(Character::isUpperCase)) {
            System.out.println("❌ ERRO: Senha deve conter pelo menos 1 caractere maiúsculo!");
            return false;
        }
        
        if (!senhaLimpa.chars().anyMatch(Character::isDigit)) {
            System.out.println("❌ ERRO: Senha deve conter pelo menos 1 número!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarNome(String nome) {
        if (!validarCampoObrigatorio(nome, "Nome")) return false;
        
        String nomeLimpo = nome.trim();
        if (nomeLimpo.matches("^[0-9]+$")) {
            System.out.println("❌ ERRO: Nome não pode conter apenas números!");
            return false;
        }
        
        if (nomeLimpo.length() < 2) {
            System.out.println("❌ ERRO: Nome deve ter pelo menos 2 caracteres!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarTelefone(String telefone) {
        if (!validarCampoObrigatorio(telefone, "Telefone")) return false;
        
        String telefoneLimpo = telefone.trim().replaceAll("[\\s()-]", "");
        
        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            System.out.println("❌ ERRO: Telefone deve ter 10 ou 11 dígitos!");
            return false;
        }
        
        if (!telefoneLimpo.matches("^[0-9]+$")) {
            System.out.println("❌ ERRO: Telefone deve conter apenas números!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarIdade(int idade) {
        if (idade < 0) {
            System.out.println("❌ ERRO: Idade não pode ser negativa!");
            return false;
        }
        
        if (idade > 120) {
            System.out.println("❌ ERRO: Idade não pode ser maior que 120 anos!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarCNH(String cnh) {
        if (!validarCampoObrigatorio(cnh, "CNH")) return false;
        
        String cnhLimpa = cnh.trim().replaceAll("[\\s.-]", "");
        
        if (cnhLimpa.length() != 11) {
            System.out.println("❌ ERRO: CNH deve ter exatamente 11 dígitos!");
            return false;
        }
        
        if (!cnhLimpa.matches("^[0-9]+$")) {
            System.out.println("❌ ERRO: CNH deve conter apenas números!");
            return false;
        }
        
        if (cnhLimpa.matches("(.)\\1{10}")) {
            System.out.println("❌ ERRO: CNH não pode ter todos os dígitos iguais!");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarDataValidade(String data) {
        if (!validarCampoObrigatorio(data, "Data de validade")) return false;
        
        String dataLimpa = data.trim();
        
        if (!dataLimpa.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            System.out.println("❌ ERRO: Data deve estar no formato dd/mm/aaaa!");
            System.out.println("💡 Exemplo: 15/12/2025");
            return false;
        }
        
        String[] partes = dataLimpa.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        
        if (dia < 1 || dia > 31) {
            System.out.println("❌ ERRO: Dia deve estar entre 01 e 31!");
            return false;
        }
        
        if (mes < 1 || mes > 12) {
            System.out.println("❌ ERRO: Mês deve estar entre 01 e 12!");
            return false;
        }
        
        int anoAtual = 2024;
        if (ano < anoAtual) {
            System.out.println("❌ ERRO: CNH não pode estar vencida!");
            return false;
        }
        
        if (ano > anoAtual + 10) {
            System.out.println("❌ ERRO: Data de validade muito distante no futuro!");
            return false;
        }
        
        if (mes == 2) {
            boolean bissexto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
            if (dia > (bissexto ? 29 : 28)) {
                System.out.println("❌ ERRO: Fevereiro não pode ter mais de " + (bissexto ? "29" : "28") + " dias!");
                return false;
            }
        } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            if (dia > 30) {
                System.out.println("❌ ERRO: Este mês só pode ter até 30 dias!");
                return false;
            }
        }
        
        return true;
    }
}
