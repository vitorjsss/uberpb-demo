package com.uberpb.test;

import com.uberpb.enums.Categoria;
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
            double preco = estimativaService.estimarPreco(10.0, Categoria.UBER_X);
            assertTrue(preco > 0);
        }

        @Test
        @DisplayName("Deve calcular preço para BLACK corretamente")
        void testPrecoBlack() {
            double preco = estimativaService.estimarPreco(5.0, Categoria.BLACK);
            assertTrue(preco > 0);
            assertNotEquals(
                    estimativaService.estimarPreco(5.0, Categoria.UBER_X),
                    preco); // Diferentes categorias devem dar preços diferentes
        }

        @Test
        @DisplayName("Deve aceitar categoria pelo nome")
        void testPrecoPorNome() {
            double preco = estimativaService.estimarPreco(8.0, "comfort");
            assertTrue(preco > 0);
        }

        @Test
        @DisplayName("Deve lançar exceção se categoria for inválida")
        void testCategoriaInvalida() {
            assertThrows(IllegalArgumentException.class,
                    () -> estimativaService.estimarPreco(10.0, "categoria_invalida"));
        }
    }

    @Nested
    @DisplayName("Testes de Estimativa de Tempo")
    class TestesTempo {

        @Test
        @DisplayName("Deve calcular tempo proporcional à distância")
        void testTempoEstimado() {
            int tempo1 = estimativaService.estimarTempoMinutos(2.0); // 2 km
            int tempo2 = estimativaService.estimarTempoMinutos(10.0); // 10 km

            assertTrue(tempo2 > tempo1);
            assertEquals(6, tempo1); // 2 km * 3 min/km
            assertEquals(30, tempo2); // 10 km * 3 min/km
        }
    }
}
