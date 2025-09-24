package com.uberpb.test;

import com.uberpb.model.*;
import com.uberpb.repository.CorridaRepository;
import com.uberpb.sevices.CorridaService;
import com.uberpb.sevices.EstimativaService;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes de CorridaService")
public class CorridaServiceTest {

    @Mock
    private CorridaRepository corridaRepository;

    private EstimativaService estimativaService;
    private CorridaService corridaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        estimativaService = new EstimativaService();
        corridaService = new CorridaService(corridaRepository, estimativaService);
    }

    @Nested
    @DisplayName("Testes de Criação e Recuperação de Corrida")
    class TestesCriacao {

        @Test
        @DisplayName("Deve criar corrida com preço estimado")
        void testCriarCorrida() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.UBER_X,
                    1, 2, 3, 10.0);

            corrida.setPrecoEstimado(estimativaService.estimarPreco(10.0, Categoria.UBER_X));

            when(corridaRepository.save(any(Corrida.class))).thenReturn(corrida);

            Corrida criada = corridaService.criarCorrida("A", "B", Categoria.UBER_X, 1, 2, 3, 10.0);

            assertNotNull(criada);
            assertEquals("A", criada.getOrigem());
            assertEquals("B", criada.getDestino());
            assertTrue(criada.getPrecoEstimado() > 0);
        }

        @Test
        @DisplayName("Deve retornar corrida por ID")
        void testGetCorridaById() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.BLACK,
                    1, 2, 3, 5.0);
            when(corridaRepository.findById(1)).thenReturn(Optional.of(corrida));

            Optional<Corrida> encontrada = corridaService.getCorridaById(1);

            assertTrue(encontrada.isPresent());
            assertEquals(1, encontrada.get().getId());
        }
    }

    @Nested
    @DisplayName("Testes de Alteração de Status")
    class TestesStatus {

        @Test
        @DisplayName("Deve iniciar corrida existente")
        void testIniciarCorrida() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.UBER_X, 1, 2, 3, 10.0);
            when(corridaRepository.findById(1)).thenReturn(Optional.of(corrida));

            corridaService.iniciarCorrida(1);

            assertEquals(CorridaStatus.EM_ANDAMENTO, corrida.getStatus());
            verify(corridaRepository).update(corrida);
        }

        @Test
        @DisplayName("Deve finalizar corrida existente")
        void testFinalizarCorrida() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.UBER_X, 1, 2, 3, 10.0);
            corrida.iniciarCorrida();
            when(corridaRepository.findById(1)).thenReturn(Optional.of(corrida));

            corridaService.finalizarCorrida(1);

            assertEquals(CorridaStatus.FINALIZADA, corrida.getStatus());
            verify(corridaRepository).update(corrida);
        }

        @Test
        @DisplayName("Deve cancelar corrida existente")
        void testCancelarCorrida() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.UBER_X, 1, 2, 3, 10.0);
            when(corridaRepository.findById(1)).thenReturn(Optional.of(corrida));

            corridaService.cancelarCorrida(1);

            assertEquals(CorridaStatus.CANCELADA, corrida.getStatus());
            verify(corridaRepository).update(corrida);
        }
    }

    @Nested
    @DisplayName("Testes de Listagem e Estatísticas")
    class TestesListagem {

        @Test
        @DisplayName("Deve listar corridas por passageiro")
        void testListarPorPassageiro() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.UBER_X, 99, 2, 3, 10.0);
            when(corridaRepository.findByPassageiroId(99)).thenReturn(List.of(corrida));

            List<Corrida> corridas = corridaService.listarCorridasPorPassageiro(99);

            assertEquals(1, corridas.size());
            assertEquals(99, corridas.get(0).getPassageiroId());
        }

        @Test
        @DisplayName("Deve calcular receita de motorista")
        void testReceitaMotorista() {
            when(corridaRepository.getTotalReceitaByMotorista(10)).thenReturn(150.0);

            double receita = corridaService.calcularReceitaMotorista(10);

            assertEquals(150.0, receita);
        }

        @Test
        @DisplayName("Deve contar corridas por status")
        void testContarPorStatus() {
            when(corridaRepository.countByStatus(CorridaStatus.FINALIZADA)).thenReturn(3L);

            long count = corridaService.contarCorridasPorStatus(CorridaStatus.FINALIZADA);

            assertEquals(3, count);
        }
    }

    @Nested
    @DisplayName("Testes de Recalcular Preço")
    class TestesPreco {

        @Test
        @DisplayName("Deve recalcular preço de corrida existente")
        void testRecalcularPreco() {
            Corrida corrida = new Corrida(1, "A", "B", Categoria.COMFORT, 1, 2, 3, 20.0);
            corrida.setPrecoEstimado(50.0);

            when(corridaRepository.findById(1)).thenReturn(Optional.of(corrida));

            corridaService.recalcularPreco(1);

            assertTrue(corrida.getPrecoEstimado() > 50.0 || corrida.getPrecoEstimado() < 50.0);
            verify(corridaRepository).update(corrida);
        }
    }
}
