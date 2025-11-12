package com.uberpb.test;

import com.uberpb.services.EstimativaService;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Estimativa de Corrida")
public class EstimativaServiceTest {

    private EstimativaService estimativaService;

    @BeforeEach
    void setUp() {
        estimativaService = new EstimativaService();
    }

    @Nested
    @DisplayName("Testes de Estimativa de Preço")
    class TestesPreco {

        @Test
        @DisplayName("Deve calcular preço para UBER_X corretamente")
        void testPrecoUberX() {
            double preco = estimativaService.estimarPreco("Aeroporto", "Shopping", "UBER_X");
            assertTrue(preco > 0);
        }

        @Test
        @DisplayName("Deve calcular preço para BLACK corretamente")
        void testPrecoBlack() {
            double preco = estimativaService.estimarPreco("Centro", "Hospital", "BLACK");
            assertTrue(preco > 0);
            assertNotEquals(
                    estimativaService.estimarPreco("Centro", "Hospital", "UBER_X"),
                    preco); // Diferentes categorias devem dar preços diferentes
        }

        @Test
        @DisplayName("Deve aceitar categoria pelo nome")
        void testPrecoPorNome() {
            double preco = estimativaService.estimarPreco("Centro", "Shopping", "COMFORT");
            assertTrue(preco > 0);
        }

        @Test
        @DisplayName("Deve lançar exceção se categoria for inválida")
        void testCategoriaInvalida() {
            assertThrows(IllegalArgumentException.class,
                    () -> estimativaService.estimarPreco("Centro", "Shopping", "categoria_invalida"));
        }
    }

    @Nested
    @DisplayName("Testes de Estimativa de Tempo")
    class TestesTempo {

        @Test
        @DisplayName("Deve calcular tempo proporcional à distância")
        void testTempoEstimado() {
            int tempo1 = estimativaService.estimarTempoMinutos("Aeroporto", "Hospital"); // 20 unidades = 2 km
            int tempo2 = estimativaService.estimarTempoMinutos("Aeroporto", "Praia"); // Distância maior

            assertTrue(tempo2 > tempo1);
            assertTrue(tempo1 > 0);
            assertTrue(tempo2 > 0);
        }
    }
}
