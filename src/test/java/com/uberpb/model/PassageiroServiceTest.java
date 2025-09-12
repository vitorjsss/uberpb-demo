package com.uberpb.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uberpb.sevices.PassageiroService;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class PassageiroServiceTest {
    private PassageiroService service;
    private Passageiro passageiro;

    @BeforeEach
    void setUp() {
        service = new PassageiroService();
        passageiro = new Passageiro(1, "Localização Inicial", false);
        passageiro.setNome("João");
        passageiro.setEmail("joao@email.com");
        passageiro.setTelefone("123456789");
        passageiro.setIdade(30);
        passageiro.setAvaliacaoMedia(4.5);
        passageiro.setMetodosPagamento(Arrays.asList("Cartão", "Dinheiro"));
    }

    @Test
    void cadastrarPassageiroValido() throws Exception {
        service.cadastrar(passageiro);
        assertEquals(1, service.listar().size());
    }

    @Test
    void cadastrarEmailInvalido() {
        passageiro.setEmail("emailinvalido");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(passageiro));
        assertTrue(e.getMessage().contains("Formato de e-mail inválido"));
    }

    @Test
    void cadastrarNomeVazio() {
        passageiro.setNome("");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(passageiro));
        assertTrue(e.getMessage().contains("Nome é obrigatório"));
    }

    @Test
    void cadastrarTelefoneVazio() {
        passageiro.setTelefone("");
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(passageiro));
        assertTrue(e.getMessage().contains("Telefone é obrigatório"));
    }

    @Test
    void cadastrarIdadeNegativa() {
        passageiro.setIdade(-1);
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(passageiro));
        assertTrue(e.getMessage().contains("Idade não pode ser negativa"));
    }

    @Test
    void cadastrarIdadeMaiorQue120() {
        passageiro.setIdade(121);
        Exception e = assertThrows(Exception.class, () -> service.cadastrar(passageiro));
        assertTrue(e.getMessage().contains("Idade não pode ser maior que 120 anos"));
    }
}