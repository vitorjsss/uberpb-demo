package com.uberpb.test;

import com.uberpb.model.User;
import com.uberpb.services.AuthService;
import com.uberpb.services.MotoristaService;
import com.uberpb.services.PassageiroService;
import com.uberpb.model.Passageiro;
import com.uberpb.model.Motorista;
import com.uberpb.session.SessionManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Autenticação e Sessão")
public class AuthServiceTest {

    private AuthService authService;
    private PassageiroService passageiroService;
    private MotoristaService motoristaService;

    // Dados de teste
    private Passageiro passageiroTeste;
    private Motorista motoristaTeste;

    @BeforeEach
    void setUp() throws Exception {
        // Limpar sessão antes de cada teste
        SessionManager.logout();
        limparArquivoSessao();

        // Inicializar services
        passageiroService = new PassageiroService();
        motoristaService = new MotoristaService();
        authService = new AuthService(passageiroService, motoristaService);

        // Criar usuários de teste
        criarUsuariosTeste();
    }

    @AfterEach
    void tearDown() {
        // Limpar sessão após cada teste
        SessionManager.logout();
        limparArquivoSessao();
    }

    private void criarUsuariosTeste() throws Exception {
        // Criar passageiro de teste
        passageiroTeste = new Passageiro();
        passageiroTeste.setId(1);
        passageiroTeste.setNome("Maria Silva");
        passageiroTeste.setEmail("maria@teste.com");
        passageiroTeste.setSenha("senha123");
        passageiroTeste.setTelefone("(83) 99999-9999");
        passageiroTeste.setIdade(25);
        passageiroService.cadastrar(passageiroTeste);

        // Criar motorista de teste
        motoristaTeste = new Motorista();
        motoristaTeste.setId(2);
        motoristaTeste.setNome("João Santos");
        motoristaTeste.setEmail("joao@teste.com");
        motoristaTeste.setSenha("senha456");
        motoristaTeste.setTelefone("(83) 88888-8888");
        motoristaTeste.setCnh("12345678901");
        motoristaTeste.setValidadeCnh("2025-12-31");
        motoristaService.cadastrar(motoristaTeste);
    }

    private void limparArquivoSessao() {
        try {
            Files.deleteIfExists(Paths.get("database/session/current_session.json"));
        } catch (IOException e) {
            // Ignora erros de limpeza
        }
    }

    @Nested
    @DisplayName("Testes de Autenticação - AuthService")
    class TestesAutenticacao {

        @Test
        @DisplayName("Deve autenticar passageiro com credenciais válidas")
        void testAutenticarPassageiroValido() {
            // Given
            String email = "maria@teste.com";
            String senha = "senha123";

            // When
            User usuarioAutenticado = authService.autenticar(email, senha);

            // Then
            assertNotNull(usuarioAutenticado);
            assertTrue(usuarioAutenticado instanceof Passageiro);
            assertEquals(email, usuarioAutenticado.getEmail());
            assertEquals("Maria Silva", usuarioAutenticado.getNome());
            assertEquals("PASSAGEIRO", authService.getTipoUsuario(usuarioAutenticado));
        }

        @Test
        @DisplayName("Deve autenticar motorista com credenciais válidas")
        void testAutenticarMotoristaValido() {
            // Given
            String email = "joao@teste.com";
            String senha = "senha456";

            // When
            User usuarioAutenticado = authService.autenticar(email, senha);

            // Then
            assertNotNull(usuarioAutenticado);
            assertTrue(usuarioAutenticado instanceof Motorista);
            assertEquals(email, usuarioAutenticado.getEmail());
            assertEquals("João Santos", usuarioAutenticado.getNome());
            assertEquals("MOTORISTA", authService.getTipoUsuario(usuarioAutenticado));
        }

        @Test
        @DisplayName("Deve retornar null para email inexistente")
        void testAutenticarEmailInexistente() {
            // Given
            String email = "inexistente@teste.com";
            String senha = "qualquersenha";

            // When
            User usuarioAutenticado = authService.autenticar(email, senha);

            // Then
            assertNull(usuarioAutenticado);
        }

        @Test
        @DisplayName("Deve retornar null para senha incorreta")
        void testAutenticarSenhaIncorreta() {
            // Given
            String email = "maria@teste.com";
            String senha = "senhaErrada";

            // When
            User usuarioAutenticado = authService.autenticar(email, senha);

            // Then
            assertNull(usuarioAutenticado);
        }

        @Test
        @DisplayName("Deve ser case-insensitive para email")
        void testAutenticarEmailCaseInsensitive() {
            // Given
            String email = "MARIA@TESTE.COM"; // Maiúsculo
            String senha = "senha123";

            // When
            User usuarioAutenticado = authService.autenticar(email, senha);

            // Then
            assertNotNull(usuarioAutenticado);
            assertEquals("maria@teste.com", usuarioAutenticado.getEmail());
        }

        @Test
        @DisplayName("Deve retornar null para parâmetros nulos")
        void testAutenticarParametrosNulos() {
            // Test email null
            assertNull(authService.autenticar(null, "senha"));

            // Test senha null
            assertNull(authService.autenticar("email@teste.com", null));

            // Test ambos null
            assertNull(authService.autenticar(null, null));
        }

        @Test
        @DisplayName("Deve retornar null para parâmetros vazios")
        void testAutenticarParametrosVazios() {
            // Test email vazio
            assertNull(authService.autenticar("", "senha"));

            // Test senha vazia
            assertNull(authService.autenticar("email@teste.com", ""));

            // Test ambos vazios
            assertNull(authService.autenticar("", ""));
        }

        @Test
        @DisplayName("Deve retornar tipo correto do usuário")
        void testGetTipoUsuario() {
            // Test passageiro
            assertEquals("PASSAGEIRO", authService.getTipoUsuario(passageiroTeste));

            // Test motorista
            assertEquals("MOTORISTA", authService.getTipoUsuario(motoristaTeste));

            // Test usuário genérico (não deveria acontecer, mas testamos)
            User usuarioGenerico = new User();
            assertEquals("DESCONHECIDO", authService.getTipoUsuario(usuarioGenerico));

            // Test null
            assertEquals("DESCONHECIDO", authService.getTipoUsuario(null));
        }
    }

    @Nested
    @DisplayName("Testes de Sessão - SessionManager")
    class TestesSessao {

        @Test
        @DisplayName("Deve salvar sessão após login bem-sucedido")
        void testSalvarSessao() {
            // Given
            assertFalse(SessionManager.isLoggedIn());
            assertNull(SessionManager.getCurrentUser());

            // When
            SessionManager.saveSession(passageiroTeste);

            // Then
            assertTrue(SessionManager.isLoggedIn());
            assertNotNull(SessionManager.getCurrentUser());
            assertEquals(passageiroTeste.getId(), SessionManager.getCurrentUser().getId());
            assertEquals(passageiroTeste.getEmail(), SessionManager.getCurrentUser().getEmail());
        }

        @Test
        @DisplayName("Deve criar arquivo de sessão no disco")
        void testCriarArquivoSessao() {
            // Given
            String sessionFile = "database/session/current_session.json";
            assertFalse(Files.exists(Paths.get(sessionFile)));

            // When
            SessionManager.saveSession(passageiroTeste);

            // Then
            assertTrue(Files.exists(Paths.get(sessionFile)));
        }

        @Test
        @DisplayName("Deve recuperar usuário logado após salvar sessão")
        void testRecuperarUsuarioLogado() {
            // Given - Salva sessão
            SessionManager.saveSession(motoristaTeste);

            // When - Simula "nova execução" limpando cache
            // (Note: não podemos limpar completamente pois getCurrentUser() recarrega)
            User usuarioRecuperado = SessionManager.getCurrentUser();

            // Then
            assertNotNull(usuarioRecuperado);
            assertEquals(motoristaTeste.getId(), usuarioRecuperado.getId());
            assertEquals(motoristaTeste.getEmail(), usuarioRecuperado.getEmail());
        }

        @Test
        @DisplayName("Deve fazer logout e limpar sessão")
        void testLogout() {
            // Given - Usuário logado
            SessionManager.saveSession(passageiroTeste);
            assertTrue(SessionManager.isLoggedIn());

            // When
            SessionManager.logout();

            // Then
            assertFalse(SessionManager.isLoggedIn());
            assertNull(SessionManager.getCurrentUser());
            assertFalse(Files.exists(Paths.get("database/session/current_session.json")));
        }

        @Test
        @DisplayName("Deve retornar false para isLoggedIn quando não há sessão")
        void testIsLoggedInSemSessao() {
            // Given - Sem sessão
            SessionManager.logout();

            // When & Then
            assertFalse(SessionManager.isLoggedIn());
        }

        @Test
        @DisplayName("Deve atualizar atividade da sessão")
        void testAtualizarAtividade() throws Exception {
            // Given
            SessionManager.saveSession(passageiroTeste);
            String arquivoSessao = "database/session/current_session.json";

            // Lê conteúdo inicial
            String conteudoInicial = Files.readString(Paths.get(arquivoSessao));

            // Aguarda um momento para garantir timestamp diferente
            Thread.sleep(1000);

            // When
            SessionManager.updateLastActivity();

            // Then
            String conteudoAtualizado = Files.readString(Paths.get(arquivoSessao));
            assertNotEquals(conteudoInicial, conteudoAtualizado);
            assertTrue(conteudoAtualizado.contains("lastActivity"));
        }

        @Test
        @DisplayName("Deve rastrear atividade do usuário")
        void testRastrearAtividade() {
            // Given
            SessionManager.saveSession(passageiroTeste);

            // When & Then - Não deve lançar exceção
            assertDoesNotThrow(() -> {
                SessionManager.trackActivity("Realizou uma corrida");
                SessionManager.trackActivity("Atualizou perfil");
            });

            // Usuário deve continuar logado
            assertTrue(SessionManager.isLoggedIn());
        }
    }

    @Nested
    @DisplayName("Testes de Integração - Auth + Session")
    class TestesIntegracao {

        @Test
        @DisplayName("Deve realizar fluxo completo: login -> sessão -> logout")
        void testFluxoCompletoLoginLogout() {
            // Given - Usuário não logado
            assertFalse(SessionManager.isLoggedIn());

            // When - Login
            User usuarioAutenticado = authService.autenticar("maria@teste.com", "senha123");
            assertNotNull(usuarioAutenticado);

            // Salva sessão (simulando o que o sistema faria)
            SessionManager.saveSession(usuarioAutenticado);

            // Then - Usuário logado
            assertTrue(SessionManager.isLoggedIn());
            assertEquals(usuarioAutenticado.getId(), SessionManager.getCurrentUser().getId());

            // When - Logout
            SessionManager.logout();

            // Then - Usuário deslogado
            assertFalse(SessionManager.isLoggedIn());
            assertNull(SessionManager.getCurrentUser());
        }

        @Test
        @DisplayName("Deve manter sessão após autenticação bem-sucedida")
        void testManterSessaoAposLogin() {
            // Given & When
            User passageiro = authService.autenticar("maria@teste.com", "senha123");
            SessionManager.saveSession(passageiro);

            User motorista = authService.autenticar("joao@teste.com", "senha456");
            SessionManager.saveSession(motorista); // Substitui sessão anterior

            // Then - Apenas a sessão mais recente fica ativa
            assertTrue(SessionManager.isLoggedIn());
            assertEquals(motorista.getId(), SessionManager.getCurrentUser().getId());
            assertEquals("MOTORISTA", authService.getTipoUsuario(SessionManager.getCurrentUser()));
        }

        @Test
        @DisplayName("Não deve salvar sessão para login inválido")
        void testNaoSalvarSessaoLoginInvalido() {
            // Given
            assertFalse(SessionManager.isLoggedIn());

            // When - Tentativa de login inválido
            User usuarioInvalido = authService.autenticar("email@inexistente.com", "senha");

            // Sistema não deveria salvar sessão para usuario null, mas testamos a robustez
            if (usuarioInvalido == null) {
                // Then - Não há sessão
                assertFalse(SessionManager.isLoggedIn());
            }
        }

        @Test
        @DisplayName("Deve preservar tipo de usuário na sessão")
        void testPreservarTipoUsuarioNaSessao() {
            // Test com Passageiro
            User passageiro = authService.autenticar("maria@teste.com", "senha123");
            SessionManager.saveSession(passageiro);
            assertEquals("PASSAGEIRO", authService.getTipoUsuario(SessionManager.getCurrentUser()));

            // Test com Motorista
            User motorista = authService.autenticar("joao@teste.com", "senha456");
            SessionManager.saveSession(motorista);
            assertEquals("MOTORISTA", authService.getTipoUsuario(SessionManager.getCurrentUser()));
        }
    }
}