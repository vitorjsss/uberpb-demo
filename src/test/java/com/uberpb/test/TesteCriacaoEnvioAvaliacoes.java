package com.uberpb.test;

import com.uberpb.model.Avaliacao;
import com.uberpb.model.Motorista;
import com.uberpb.repository.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * T19.1: Teste de Criação e Envio de Avaliações
 * Baseado na estrutura do PagamentoUnitTest
 */
public class TesteCriacaoEnvioAvaliacoes {
    
    private DatabaseManager db;
    private int avaliacaoId1;
    private int avaliacaoId2;
    private int avaliacaoId3;
    
    @BeforeEach
    void setUp() {
        db = new DatabaseManager();
    }
    
    @AfterEach
    void tearDown() {
        // Limpar avaliações de teste criadas
        if (avaliacaoId1 > 0) db.deleteAvaliacaoById(avaliacaoId1);
        if (avaliacaoId2 > 0) db.deleteAvaliacaoById(avaliacaoId2);
        if (avaliacaoId3 > 0) db.deleteAvaliacaoById(avaliacaoId3);
    }
    
    @Test
    void deveCriarAvaliacaoPassageiroParaMotorista() {
        // Criar avaliação de passageiro para motorista
        Avaliacao avaliacao = new Avaliacao(
            100,          // corridaId
            1,            // avaliadorId (passageiro)
            2,            // avaliadoId (motorista)
            "passageiro",
            "motorista",
            5,            // nota máxima
            "Excelente motorista! Muito educado e pontual."
        );
        
        // Verificar campos
        assertEquals(100, avaliacao.getCorridaId());
        assertEquals(1, avaliacao.getAvaliadorId());
        assertEquals(2, avaliacao.getAvaliadoId());
        assertEquals("passageiro", avaliacao.getTipoAvaliador());
        assertEquals("motorista", avaliacao.getTipoAvaliado());
        assertEquals(5, avaliacao.getNota());
        assertEquals("Excelente motorista! Muito educado e pontual.", avaliacao.getComentario());
        assertNotNull(avaliacao.getCriadoEm());
    }
    
    @Test
    void deveEnviarAvaliacaoParaBancoDeDados() {
        // Criar e enviar avaliação
        Avaliacao avaliacao = new Avaliacao(
            101, 1, 2, "passageiro", "motorista", 5,
            "Ótimo serviço!"
        );
        
        Avaliacao avaliacaoSalva = db.saveAvaliacao(avaliacao);
        avaliacaoId1 = avaliacaoSalva.getId();
        
        // Verificar que foi salva com ID válido
        assertTrue(avaliacaoSalva.getId() > 0);
        assertNotNull(avaliacaoSalva);
    }
    
    @Test
    void deveRecuperarAvaliacaoEnviadaDoBanco() {
        // Salvar avaliação
        Avaliacao avaliacao = new Avaliacao(
            102, 1, 2, "passageiro", "motorista", 4,
            "Bom motorista"
        );
        Avaliacao salva = db.saveAvaliacao(avaliacao);
        avaliacaoId1 = salva.getId();
        
        // Recuperar do banco
        Optional<Avaliacao> recuperada = db.findAvaliacaoById(salva.getId());
        
        assertTrue(recuperada.isPresent());
        assertEquals(4, recuperada.get().getNota());
        assertEquals("Bom motorista", recuperada.get().getComentario());
    }
    
    @Test
    void deveCriarAvaliacaoMotoristaParaPassageiro() {
        // Avaliação reversa: motorista → passageiro
        Avaliacao avaliacao = new Avaliacao(
            103, 2, 1, "motorista", "passageiro", 4,
            "Passageiro educado e pontual!"
        );
        
        assertEquals("motorista", avaliacao.getTipoAvaliador());
        assertEquals("passageiro", avaliacao.getTipoAvaliado());
        assertEquals(2, avaliacao.getAvaliadorId());
        assertEquals(1, avaliacao.getAvaliadoId());
    }
    
    @Test
    void deveBuscarAvaliacoesPorCorrida() {
        // Criar duas avaliações para a mesma corrida
        Avaliacao aval1 = new Avaliacao(104, 1, 2, "passageiro", "motorista", 5, "Ótimo!");
        Avaliacao aval2 = new Avaliacao(104, 2, 1, "motorista", "passageiro", 4, "Bom!");
        
        avaliacaoId1 = db.saveAvaliacao(aval1).getId();
        avaliacaoId2 = db.saveAvaliacao(aval2).getId();
        
        // Buscar por corrida
        List<Avaliacao> avaliacoes = db.findAvaliacoesByCorridaId(104);
        
        assertTrue(avaliacoes.size() >= 2);
    }
    
    @Test
    void deveValidarAvaliacaoDuplicada() {
        // Salvar primeira avaliação
        Avaliacao avaliacao = new Avaliacao(105, 1, 2, "passageiro", "motorista", 5, null);
        avaliacaoId1 = db.saveAvaliacao(avaliacao).getId();
        
        // Verificar que já foi avaliado (usar maiúsculas conforme esperado pelo método)
        boolean jaAvaliou = db.corridaJaAvaliadaPor(105, 1, "passageiro");
        
        assertTrue(jaAvaliou);
    }
    
    @Test
    void deveCalcularMediaAposEnvio() {
        // Enviar avaliação
        Avaliacao avaliacao = new Avaliacao(106, 1, 2, "passageiro", "motorista", 5, null);
        avaliacaoId1 = db.saveAvaliacao(avaliacao).getId();
        
        // Calcular média do motorista ID 2
        double media = db.calcularMediaAvaliacoes(2);
        int total = db.contarAvaliacoes(2);
        
        assertTrue(media > 0);
        assertTrue(total > 0);
    }
    
    @Test
    void deveAtualizarMediaAutomaticamente() {
        // Enviar avaliação
        Avaliacao avaliacao = new Avaliacao(107, 3, 2, "passageiro", "motorista", 5, null);
        avaliacaoId1 = db.saveAvaliacao(avaliacao).getId();
        
        // Verificar que modelo motorista foi atualizado (T16.4)
        Optional<Motorista> motoristaOpt = db.findMotoristaById(2);
        
        assertTrue(motoristaOpt.isPresent());
        assertTrue(motoristaOpt.get().getAvaliacaoMedia() > 0);
    }
    
    @Test
    void deveCriarAvaliacaoSemComentario() {
        // Comentário é opcional (null)
        Avaliacao avaliacao = new Avaliacao(108, 1, 2, "passageiro", "motorista", 4, null);
        
        assertNull(avaliacao.getComentario());
        
        // Deve salvar normalmente
        avaliacaoId1 = db.saveAvaliacao(avaliacao).getId();
        assertTrue(avaliacaoId1 > 0);
    }
    
    @Test
    void deveGerarToStringComCamposValidos() {
        Avaliacao avaliacao = new Avaliacao(109, 1, 2, "passageiro", "motorista", 5, "Ótimo!");
        String texto = avaliacao.toString();
        
        assertNotNull(texto);
        assertTrue(texto.contains("Avaliacao"));
    }
}
