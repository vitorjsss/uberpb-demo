package com.uberpb.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uberpb.sevices.MotoristaService;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class MotoristaServiceTest {
    private MotoristaService service;
    private Motorista motorista;

    @BeforeEach
    void setUp() {
        service = new MotoristaService();
        motorista = new Motorista(1, true, "12345678900", "2026-12-31", 4.5, 10, true, "Localização X");
        motorista.setNome("Carlos");
        motorista.setEmail("carlos@email.com");
        motorista.setTelefone("11999999999");
        motorista.setDataCadastro(LocalDateTime.now().toString());
    }

    @Test
    void cadastrarMotoristaValido() throws Exception {
        service.cadastrar(motorista);
        assertEquals(1, service.listar().size());
    }

    @Test
    void cadastrarEmailInvalido() {
        motorista.setEmail("emailinvalido");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(motorista));
        assertTrue(e.getMessage().contains("Formato de e-mail inválido"));
    }

    @Test
    void cadastrarNomeVazio() {
        motorista.setNome("");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(motorista));
        assertTrue(e.getMessage().contains("Nome é obrigatório"));
    }

    @Test
    void cadastrarTelefoneVazio() {
        motorista.setTelefone("");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(motorista));
        assertTrue(e.getMessage().contains("Telefone é obrigatório"));
    }

    @Test
    void cadastrarCnhVazia() {
        motorista.setCnh("");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(motorista));
        assertTrue(e.getMessage().contains("CNH é obrigatória"));
    }

    @Test
    void cadastrarValidadeCnhNula() {
        motorista.setValidadeCnh(null);
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(motorista));
        assertTrue(e.getMessage().contains("Data de validade da CNH é obrigatória"));
    }
}
