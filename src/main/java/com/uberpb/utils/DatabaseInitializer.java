package com.uberpb.utils;

import com.uberpb.enums.Categoria;
import com.uberpb.enums.TipoVeiculo;
import com.uberpb.enums.TipoChavePix;
import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.model.User;
import com.uberpb.model.Veiculo;
import com.uberpb.model.pagamento.Cartao;
import com.uberpb.model.pagamento.PIX;
import com.uberpb.model.pagamento.PayPal;
import com.uberpb.repository.DatabaseManager;
import com.uberpb.services.MetodoPagamentoService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Classe utilitária para resetar e popular o banco de dados com dados iniciais.
 */
public class DatabaseInitializer {

    private final DatabaseManager db;
    private final MetodoPagamentoService metodoPagamentoService;
    private final Random random;

    // Localizações válidas do localizacoes.json
    private final List<String> localizacoesValidas = Arrays.asList(
            "Aeroporto", "Hospital", "Shopping", "Universidade", "Centro",
            "Estádio", "Praia", "Rodoviária", "Teatro", "Parque");

    public DatabaseInitializer() {
        this.db = new DatabaseManager();
        this.metodoPagamentoService = new MetodoPagamentoService();
        this.random = new Random();
    }

    /**
     * Gera um CPF aleatório válido (apenas números)
     */
    private String gerarCPFAleatorio() {
        StringBuilder cpf = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            cpf.append(random.nextInt(10));
        }
        return cpf.toString();
    }

    /**
     * Obtém uma localização aleatória da lista válida
     */
    private String obterLocalizacaoAleatoria() {
        return localizacoesValidas.get(random.nextInt(localizacoesValidas.size()));
    }

    /**
     * Reseta todo o banco de dados exceto localizacoes.json
     */
    public void resetarBancoDados() {
        System.out.println("🔄 Resetando banco de dados...");

        // Arquivos principais
        deletarArquivo("database/users/users.json");
        deletarArquivo("database/passageiros/passageiros.json");
        deletarArquivo("database/motoristas/motoristas.json");
        deletarArquivo("database/veiculos/veiculos.json");
        deletarArquivo("database/corridas/corridas.json");
        deletarArquivo("database/id_counter.json");

        // Arquivos de métodos de pagamento
        deletarArquivo("database/pagamentos/cartoes.json");
        deletarArquivo("database/pagamentos/pix.json");
        deletarArquivo("database/pagamentos/paypal.json");

        System.out.println("Banco de dados resetado!");
    }

    /**
     * Popula o banco de dados com dados iniciais
     */
    public void popularBancoDados() {
        System.out.println("🌱 Populando banco de dados com dados iniciais...");

        // 1. Criar usuários primeiro
        criarUsuarios();

        // 2. Criar passageiros vinculados aos usuários
        criarPassageiros();

        // 3. Criar motoristas e veículos vinculados aos usuários
        criarMotoristas();

        System.out.println("✅ Banco de dados populado com sucesso!");
        exibirResumo();
    }

    /**
     * Cria os usuários base (Users) primeiro
     */
    private void criarUsuarios() {
        System.out.println("👤 Criando usuários base...");

        // Usuário 1 - Maria Silva (Passageiro)
        User userMaria = new User();
        userMaria.setUsername("maria.silva");
        userMaria.setSenha("senha123");
        userMaria.setNome("Maria");
        userMaria.setSobrenome("Silva");
        userMaria.setEmail("maria.silva@email.com");
        userMaria.setTelefone("(11) 98765-4321");
        userMaria.setTipo("PASSAGEIRO");
        userMaria.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        User mariaSalva = db.saveUser(userMaria);
        System.out.println(
                "   ✓ Usuário criado: " + mariaSalva.getNome() + " (ID: " + mariaSalva.getId() + ") - PASSAGEIRO");

        // Usuário 2 - João Santos (Passageiro)
        User userJoao = new User();
        userJoao.setUsername("joao.santos");
        userJoao.setSenha("senha456");
        userJoao.setNome("João");
        userJoao.setSobrenome("Santos");
        userJoao.setEmail("joao.santos@email.com");
        userJoao.setTelefone("(11) 91234-5678");
        userJoao.setTipo("PASSAGEIRO");
        userJoao.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        User joaoSalvo = db.saveUser(userJoao);
        System.out.println(
                "   ✓ Usuário criado: " + joaoSalvo.getNome() + " (ID: " + joaoSalvo.getId() + ") - PASSAGEIRO");

        // Usuário 3 - Carlos Oliveira (Motorista)
        User userCarlos = new User();
        userCarlos.setUsername("carlos.oliveira");
        userCarlos.setSenha("senha789");
        userCarlos.setNome("Carlos");
        userCarlos.setSobrenome("Oliveira");
        userCarlos.setEmail("carlos.oliveira@email.com");
        userCarlos.setTelefone("(11) 99111-2222");
        userCarlos.setTipo("MOTORISTA");
        userCarlos.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        User carlosSalvo = db.saveUser(userCarlos);
        System.out.println(
                "   ✓ Usuário criado: " + carlosSalvo.getNome() + " (ID: " + carlosSalvo.getId() + ") - MOTORISTA");

        // Usuário 4 - Ana Costa (Motorista)
        User userAna = new User();
        userAna.setUsername("ana.costa");
        userAna.setSenha("senha101");
        userAna.setNome("Ana");
        userAna.setSobrenome("Costa");
        userAna.setEmail("ana.costa@email.com");
        userAna.setTelefone("(11) 99333-4444");
        userAna.setTipo("MOTORISTA");
        userAna.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        User anaSalva = db.saveUser(userAna);
        System.out
                .println("   ✓ Usuário criado: " + anaSalva.getNome() + " (ID: " + anaSalva.getId() + ") - MOTORISTA");

        // Usuário 5 - Ricardo Ferreira (Motorista)
        User userRicardo = new User();
        userRicardo.setUsername("ricardo.ferreira");
        userRicardo.setSenha("senha202");
        userRicardo.setNome("Ricardo");
        userRicardo.setSobrenome("Ferreira");
        userRicardo.setEmail("ricardo.ferreira@email.com");
        userRicardo.setTelefone("(11) 99555-6666");
        userRicardo.setTipo("MOTORISTA");
        userRicardo.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        User ricardoSalvo = db.saveUser(userRicardo);
        System.out.println(
                "   ✓ Usuário criado: " + ricardoSalvo.getNome() + " (ID: " + ricardoSalvo.getId() + ") - MOTORISTA");

        // Usuário 6 - Patricia Lima (Motorista)
        User userPatricia = new User();
        userPatricia.setUsername("patricia.lima");
        userPatricia.setSenha("senha303");
        userPatricia.setNome("Patricia");
        userPatricia.setSobrenome("Lima");
        userPatricia.setEmail("patricia.lima@email.com");
        userPatricia.setTelefone("(11) 99777-8888");
        userPatricia.setTipo("MOTORISTA");
        userPatricia.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        User patriciaSalva = db.saveUser(userPatricia);
        System.out.println(
                "   ✓ Usuário criado: " + patriciaSalva.getNome() + " (ID: " + patriciaSalva.getId() + ") - MOTORISTA");
    }

    /**
     * Cria 2 passageiros vinculados aos usuários existentes
     */
    private void criarPassageiros() {
        System.out.println("👥 Criando passageiros...");

        // Buscar usuários passageiros
        List<User> usuarios = db.findAllUsers();
        User usuarioMaria = usuarios.stream()
                .filter(u -> u.getEmail().equals("maria.silva@email.com"))
                .findFirst().orElse(null);
        User usuarioJoao = usuarios.stream()
                .filter(u -> u.getEmail().equals("joao.santos@email.com"))
                .findFirst().orElse(null);

        if (usuarioMaria == null || usuarioJoao == null) {
            System.err.println("❌ Erro: Usuários não encontrados. Execute criarUsuarios() primeiro.");
            return;
        }

        // Passageiro 1 - Maria Silva (vinculado ao User ID)
        Passageiro passageiro1 = new Passageiro();
        passageiro1.setId(usuarioMaria.getId()); // Mesmo ID do User
        passageiro1.setNome(usuarioMaria.getNome());
        passageiro1.setSobrenome(usuarioMaria.getSobrenome());
        passageiro1.setEmail(usuarioMaria.getEmail());
        passageiro1.setTelefone(usuarioMaria.getTelefone());
        passageiro1.setIdade(28);
        passageiro1.setLocalizacaoAtual(obterLocalizacaoAleatoria());
        passageiro1.setHistoricoCorridas(new ArrayList<>());
        passageiro1.setEmCorrida(false);
        passageiro1.setAvaliacaoMedia(4.8);

        Passageiro passageiroSalvo1 = db.savePassageiro(passageiro1);
        System.out.println("   ✓ Passageiro criado: " + passageiroSalvo1.getNome() + " (ID: " + passageiroSalvo1.getId()
                + ") - " + passageiroSalvo1.getLocalizacaoAtual());

        // Métodos de pagamento para Maria
        criarMetodosPagamentoMaria(passageiroSalvo1.getId());

        // Passageiro 2 - João Santos (vinculado ao User ID)
        Passageiro passageiro2 = new Passageiro();
        passageiro2.setId(usuarioJoao.getId()); // Mesmo ID do User
        passageiro2.setNome(usuarioJoao.getNome());
        passageiro2.setSobrenome(usuarioJoao.getSobrenome());
        passageiro2.setEmail(usuarioJoao.getEmail());
        passageiro2.setTelefone(usuarioJoao.getTelefone());
        passageiro2.setIdade(35);
        passageiro2.setLocalizacaoAtual(obterLocalizacaoAleatoria());
        passageiro2.setHistoricoCorridas(new ArrayList<>());
        passageiro2.setEmCorrida(false);
        passageiro2.setAvaliacaoMedia(4.5);

        Passageiro passageiroSalvo2 = db.savePassageiro(passageiro2);
        System.out.println("   ✓ Passageiro criado: " + passageiroSalvo2.getNome() + " (ID: " + passageiroSalvo2.getId()
                + ") - " + passageiroSalvo2.getLocalizacaoAtual());

        // Métodos de pagamento para João
        criarMetodosPagamentoJoao(passageiroSalvo2.getId());
    }

    /**
     * Cria métodos de pagamento para Maria (Cartão de Crédito + PIX CPF)
     */
    private void criarMetodosPagamentoMaria(int usuarioId) {
        try {
            String cpfMaria = gerarCPFAleatorio();

            // Cartão de Crédito
            Cartao cartao = new Cartao(
                    usuarioId,
                    "4532123456789012",
                    "1234",
                    LocalDate.of(2027, 12, 31),
                    "Maria Silva",
                    cpfMaria,
                    "CREDITO");
            metodoPagamentoService.salvarCartao(cartao);
            System.out.println("     ✓ Cartão de crédito adicionado (CPF: " + cpfMaria + ")");

            // PIX com CPF
            PIX pix = new PIX(
                    usuarioId,
                    cpfMaria,
                    TipoChavePix.CPF,
                    "Meu CPF principal");
            metodoPagamentoService.salvarPIX(pix);
            System.out.println("     ✓ PIX CPF adicionado");

        } catch (Exception e) {
            System.err.println("     ❌ Erro ao criar métodos de pagamento para Maria: " + e.getMessage());
        }
    }

    /**
     * Cria métodos de pagamento para João (Cartão de Débito + PayPal + PIX Email)
     */
    private void criarMetodosPagamentoJoao(int usuarioId) {
        try {
            String cpfJoao = gerarCPFAleatorio();

            // Cartão de Débito
            Cartao cartao = new Cartao(
                    usuarioId,
                    "5555444433332222",
                    "5678",
                    LocalDate.of(2026, 8, 31),
                    "João Santos",
                    cpfJoao,
                    "DEBITO");
            metodoPagamentoService.salvarCartao(cartao);
            System.out.println("     ✓ Cartão de débito adicionado (CPF: " + cpfJoao + ")");

            // PayPal
            PayPal paypal = new PayPal(
                    usuarioId,
                    "joao.santos@paypal.com",
                    "João Santos");
            metodoPagamentoService.salvarPayPal(paypal);
            System.out.println("     ✓ PayPal adicionado");

            // PIX com Email
            PIX pix = new PIX(
                    usuarioId,
                    "joao.santos@email.com",
                    TipoChavePix.EMAIL,
                    "Email pessoal");
            metodoPagamentoService.salvarPIX(pix);
            System.out.println("     ✓ PIX Email adicionado");

        } catch (Exception e) {
            System.err.println("     ❌ Erro ao criar métodos de pagamento para João: " + e.getMessage());
        }
    }

    /**
     * Cria 4 motoristas com veículos de tipos diferentes
     */
    private void criarMotoristas() {
        System.out.println("🚗 Criando motoristas e veículos...");

        // Buscar usuários motoristas
        List<User> usuarios = db.findAllUsers();

        // Motorista 1 - Carlos (UBER_X - Carro)
        User usuarioCarlos = usuarios.stream()
                .filter(u -> u.getEmail().equals("carlos.oliveira@email.com"))
                .findFirst().orElse(null);
        if (usuarioCarlos != null) {
            criarMotoristaPorUser(usuarioCarlos, 42, TipoVeiculo.CARRO, "Toyota", "Corolla", "ABC-1234", 2020,
                    Categoria.UBER_X);
        }

        // Motorista 2 - Ana (BLACK - Carro)
        User usuarioAna = usuarios.stream()
                .filter(u -> u.getEmail().equals("ana.costa@email.com"))
                .findFirst().orElse(null);
        if (usuarioAna != null) {
            criarMotoristaPorUser(usuarioAna, 29, TipoVeiculo.CARRO, "BMW", "X3", "DEF-5678", 2022, Categoria.BLACK);
        }

        // Motorista 3 - Ricardo (COMFORT - Carro)
        User usuarioRicardo = usuarios.stream()
                .filter(u -> u.getEmail().equals("ricardo.ferreira@email.com"))
                .findFirst().orElse(null);
        if (usuarioRicardo != null) {
            criarMotoristaPorUser(usuarioRicardo, 38, TipoVeiculo.CARRO, "Mercedes-Benz", "E-Class", "GHI-9012", 2023,
                    Categoria.COMFORT);
        }

        // Motorista 4 - Patricia (UBER_X - Moto)
        User usuarioPatricia = usuarios.stream()
                .filter(u -> u.getEmail().equals("patricia.lima@email.com"))
                .findFirst().orElse(null);
        if (usuarioPatricia != null) {
            criarMotoristaPorUser(usuarioPatricia, 31, TipoVeiculo.MOTO, "Honda", "CB600", "JKL-3456", 2019,
                    Categoria.UBER_X);
        }
    }

    /**
     * Cria um motorista vinculado a um User existente
     */
    private void criarMotoristaPorUser(User usuario, int idade, TipoVeiculo tipoVeiculo,
            String marca, String modelo, String placa, int ano, Categoria categoria) {
        try {
            // Criar motorista vinculado ao User
            Motorista motorista = new Motorista();
            motorista.setId(usuario.getId()); // Mesmo ID do User
            motorista.setNome(usuario.getNome());
            motorista.setSobrenome(usuario.getSobrenome());
            motorista.setEmail(usuario.getEmail());
            motorista.setTelefone(usuario.getTelefone());
            motorista.setLocalizacaoAtual(obterLocalizacaoAleatoria());
            motorista.setDisponivel(true);
            motorista.setAvaliacaoMedia(4.0 + Math.random()); // Entre 4.0 e 5.0

            Motorista motoristaSalvo = db.saveMotorista(motorista);
            System.out.println("   ✓ Motorista criado: " + motoristaSalvo.getNome() + " (ID: " + motoristaSalvo.getId()
                    + ") - " + motoristaSalvo.getLocalizacaoAtual());

            // Criar veículo do motorista
            Veiculo veiculo = new Veiculo();
            veiculo.setMarca(marca);
            veiculo.setModelo(modelo);
            veiculo.setPlaca(placa);
            veiculo.setAno(ano);
            veiculo.setCor("Branco"); // Cor padrão
            veiculo.setCategoria(categoria.getNome());

            db.saveVeiculo(veiculo);
            System.out.println("     ✓ Veículo adicionado: " + marca + " " + modelo + " (" + categoria.getNome() + ")");

        } catch (Exception e) {
            System.err.println("   ❌ Erro ao criar motorista " + usuario.getNome() + ": " + e.getMessage());
        }
    }

    /**
     * Deleta um arquivo se ele existir
     */
    private void deletarArquivo(String caminho) {
        File arquivo = new File(caminho);
        if (arquivo.exists()) {
            if (arquivo.delete()) {
                System.out.println("   ✓ Arquivo deletado: " + caminho);
            } else {
                System.err.println("   ❌ Erro ao deletar: " + caminho);
            }
        }
    }

    /**
     * Exibe resumo dos dados criados
     */
    private void exibirResumo() {
        System.out.println("\n📊 RESUMO DOS DADOS CRIADOS:");
        System.out.println("════════════════════════════════════════");

        System.out.println("\n� USUÁRIOS CRIADOS (6 usuários base):");
        System.out.println("PASSAGEIROS:");
        System.out.println("1. Maria Silva (maria.silva@email.com) - Senha: senha123");
        System.out.println("2. João Santos (joao.santos@email.com) - Senha: senha123");
        System.out.println("MOTORISTAS:");
        System.out.println("3. Carlos Oliveira (carlos.oliveira@email.com) - Senha: senha123");
        System.out.println("4. Ana Costa (ana.costa@email.com) - Senha: senha123");
        System.out.println("5. Ricardo Ferreira (ricardo.ferreira@email.com) - Senha: senha123");
        System.out.println("6. Patricia Lima (patricia.lima@email.com) - Senha: senha123");

        System.out.println("\n👥 PASSAGEIROS (vinculados aos Users):");
        System.out.println("1. Maria Silva - CPF aleatório - Localização aleatória");
        System.out.println("   - Cartão de Crédito: ****-1234");
        System.out.println("   - PIX CPF: mesmo CPF do cartão");

        System.out.println("\n2. João Santos - CPF aleatório - Localização aleatória");
        System.out.println("   - Cartão de Débito: ****-2222");
        System.out.println("   - PayPal: j***@paypal.com");
        System.out.println("   - PIX Email: j***@email.com");

        System.out.println("\n🚗 MOTORISTAS E VEÍCULOS (vinculados aos Users):");
        System.out.println("1. Carlos Oliveira - Toyota Corolla (UBER_X) - CPF aleatório");
        System.out.println("2. Ana Costa - BMW X3 (BLACK) - CPF aleatório");
        System.out.println("3. Ricardo Ferreira - Mercedes E-Class (COMFORT) - CPF aleatório");
        System.out.println("4. Patricia Lima - Honda CB600 (UBER_X) - CPF aleatório");

        System.out.println("\n📍 LOCALIZAÇÕES VÁLIDAS:");
        System.out.println("   " + String.join(", ", localizacoesValidas));

        System.out.println("\n💡 HIERARQUIA DE DADOS:");
        System.out.println("- Primeiro são criados os USERS (com senha)");
        System.out.println("- Depois PASSAGEIROS e MOTORISTAS herdam/referenciam os USERS");
        System.out.println("- Cada PASSAGEIRO/MOTORISTA tem o mesmo ID do USER correspondente");
        System.out.println("- Todos usam CPF aleatório válido e localizações do JSON");

        System.out.println("\n🔓 PARA TESTAR:");
        System.out.println("- Faça login com qualquer email acima usando senha: senha123");
        System.out.println("- Todos os métodos de pagamento já estão cadastrados");
        System.out.println("- 4 motoristas disponíveis em diferentes categorias");
        System.out.println("════════════════════════════════════════");
    }

    /**
     * Método que apenas reseta o banco de dados sem popular com dados
     */
    public void apenasResetarBancoDados() {
        System.out.println("🔄 MODO: APENAS RESET DO BANCO DE DADOS");
        System.out.println("════════════════════════════════════════");

        resetarBancoDados();

        System.out.println("\n✅ Reset concluído! Banco de dados limpo e pronto para uso.");
        System.out.println("💡 Use popularBancoDados() ou resetarEPopular() para adicionar dados de teste.");
    }

    /**
     * Método principal para executar reset + população
     */
    public void resetarEPopular() {
        resetarBancoDados();
        popularBancoDados();
    }

    /**
     * Método main para execução standalone
     */
    public static void main(String[] args) {
        DatabaseInitializer initializer = new DatabaseInitializer();

        System.out.println("🚀 INICIALIZADOR DO BANCO DE DADOS");
        System.out.println("════════════════════════════════════════");

        // Para apenas resetar (sem dados):
        initializer.apenasResetarBancoDados();

        // Para resetar + popular com dados:
        // initializer.resetarEPopular();

        System.out.println("\n🎉 Inicialização concluída! O sistema está pronto para uso.");
    }
}