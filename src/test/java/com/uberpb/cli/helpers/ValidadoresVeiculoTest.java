package com.uberpb.cli.helpers;

import com.uberpb.helpers.ValidadoresVeiculo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidadoresVeiculoTest {
    @Test
    void testValidarModeloValido() {
        assertTrue(ValidadoresVeiculo.validarModelo("Civic"));
    }

    @Test
    void testValidarModeloInvalido() {
        assertFalse(ValidadoresVeiculo.validarModelo(""));
    }

    @Test
    void testValidarModeloNulo() {
        assertFalse(ValidadoresVeiculo.validarModelo(null));
    }

    @Test
    void testValidarMarcaValida() {
        assertTrue(ValidadoresVeiculo.validarMarca("Honda"));
    }

    @Test
    void testValidarMarcaInvalida() {
        assertFalse(ValidadoresVeiculo.validarMarca(""));
    }

    @Test
    void testValidarMarcaNula() {
        assertFalse(ValidadoresVeiculo.validarMarca(null));
    }

    @Test
    void testValidarCorValida() {
        assertTrue(ValidadoresVeiculo.validarCor("Preto"));
    }

    @Test
    void testValidarCorInvalida() {
        assertFalse(ValidadoresVeiculo.validarCor(""));
    }

    @Test
    void testValidarCorNula() {
        assertFalse(ValidadoresVeiculo.validarCor(null));
    }

    @Test
    void testValidarPlacaMercosul() {
        assertTrue(ValidadoresVeiculo.validarPlaca("ABC1D23"));
    }

    @Test
    void testValidarPlacaAntiga() {
        assertTrue(ValidadoresVeiculo.validarPlaca("ABC1234"));
    }

    @Test
    void testValidarPlacaInvalida() {
        assertFalse(ValidadoresVeiculo.validarPlaca("A1B2C3"));
    }

    @Test
    void testValidarPlacaVazia() {
        assertFalse(ValidadoresVeiculo.validarPlaca(""));
    }

    @Test
    void testValidarPlacaNula() {
        assertFalse(ValidadoresVeiculo.validarPlaca(null));
    }

    @Test
    void testValidarAnoAtual() {
        int anoAtual = java.time.LocalDate.now().getYear();
        assertTrue(ValidadoresVeiculo.validarAno(anoAtual));
    }

    @Test
    void testValidarAnoValido() {
        assertTrue(ValidadoresVeiculo.validarAno(2000));
    }

    @Test
    void testValidarAnoMuitoAntigo() {
        assertFalse(ValidadoresVeiculo.validarAno(1800));
    }

    @Test
    void testValidarAnoFuturo() {
        int anoAtual = java.time.LocalDate.now().getYear();
        assertFalse(ValidadoresVeiculo.validarAno(anoAtual + 1));
    }

    @Test
    void testValidarCapacidadeValida() {
        assertTrue(ValidadoresVeiculo.validarCapacidadePortaMalas(450));
    }

    @Test
    void testValidarCapacidadeZero() {
        assertTrue(ValidadoresVeiculo.validarCapacidadePortaMalas(0));
    }

    @Test
    void testValidarCapacidadeNegativa() {
        assertFalse(ValidadoresVeiculo.validarCapacidadePortaMalas(-10));
    }

    @Test
    void testValidarCapacidadeExcedente() {
        assertFalse(ValidadoresVeiculo.validarCapacidadePortaMalas(2000));
    }

    @Test
    void testValidarNumPassageirosValido() {
        assertTrue(ValidadoresVeiculo.validarNumeroPassageiros(4));
    }

    @Test
    void testValidarNumPassageirosMaximo() {
        assertTrue(ValidadoresVeiculo.validarNumeroPassageiros(10));
    }

    @Test
    void testValidarNumPassageirosZero() {
        assertFalse(ValidadoresVeiculo.validarNumeroPassageiros(0));
    }

    @Test
    void testValidarNumPassageirosExcedente() {
        assertFalse(ValidadoresVeiculo.validarNumeroPassageiros(11));
    }
}