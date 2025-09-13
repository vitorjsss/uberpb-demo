package com.uberpb.cli.forms;

import com.uberpb.helpers.ValidadoresVeiculo;
import com.uberpb.model.Veiculo;
import com.uberpb.model.Categoria;
import com.uberpb.model.Motorista;
import com.uberpb.repository.json.VeiculoRepositoryJSON;
import com.uberpb.sevices.CategoriaService;
import com.uberpb.repository.DatabaseManager;

import java.util.Scanner;

public class CadastroVeiculoCLI {

    public static void cadastrarVeiculo(Scanner sc, DatabaseManager bd, Motorista m) {
        System.out.println("--- Cadastro de Veículo ---");

        String modelo = lerString(sc, "Modelo", ValidadoresVeiculo::validarModelo);
        String marca = lerString(sc, "Marca", ValidadoresVeiculo::validarMarca);
        String cor = lerString(sc, "Cor", ValidadoresVeiculo::validarCor);
        String placa = lerString(sc, "Placa", ValidadoresVeiculo::validarPlaca);
        Integer ano = lerInteiro(sc, "Ano", ValidadoresVeiculo::validarAno);
        String tipo = lerString(sc, "Tipo (CARRO ou MOTO)", ValidadoresVeiculo::validarTipo);

        Double capacidadePortaMalas = null;
        Integer numeroPassageiros = null;

        if (tipo.equalsIgnoreCase("CARRO")) {
            capacidadePortaMalas = lerDouble(sc, "Capacidade do porta-malas (litros)",
                    ValidadoresVeiculo::validarCapacidadePortaMalas);
            numeroPassageiros = lerInteiro(sc, "Número de passageiros",
                    ValidadoresVeiculo::validarNumeroPassageiros);
        } else {
            // Para motos, podemos setar valores padrão
            capacidadePortaMalas = 0.0;
            numeroPassageiros = 1;
        }

        // Criar veículo
        Veiculo veiculo = new Veiculo(
                0, modelo, marca, ano, cor, placa,
                placa, capacidadePortaMalas, numeroPassageiros);

        // Classificar categoria automaticamente
        CategoriaService categoriaService = new CategoriaService(new VeiculoRepositoryJSON());
        Categoria categoria = categoriaService.classificarVeiculo(veiculo);
        veiculo.setCategoria(categoria.getNome());

        // Salvar veículo
        VeiculoRepositoryJSON veiculoRepo = new VeiculoRepositoryJSON();
        veiculoRepo.save(veiculo);

        System.out.println("Veículo cadastrado com sucesso!");
        System.out.println("Categoria definida: " + veiculo.getCategoria());
    }

    // --- Métodos auxiliares para ler valores com validação ---
    private static String lerString(Scanner sc, String label, java.util.function.Predicate<String> validador) {
        String valor = null;
        while (valor == null) {
            System.out.print(label + ": ");
            String input = sc.nextLine();
            if (validador.test(input)) {
                valor = input;
            } else {
                System.out.println(label + " inválida!");
            }
        }
        return valor;
    }

    private static Integer lerInteiro(Scanner sc, String label, java.util.function.IntPredicate validador) {
        Integer valor = null;
        while (valor == null) {
            System.out.print(label + ": ");
            String input = sc.nextLine();
            try {
                int num = Integer.parseInt(input);
                if (validador.test(num)) {
                    valor = num;
                } else {
                    System.out.println(label + " inválido!");
                }
            } catch (NumberFormatException e) {
                System.out.println(label + " deve ser um número!");
            }
        }
        return valor;
    }

    private static Double lerDouble(Scanner sc, String label, java.util.function.DoublePredicate validador) {
        Double valor = null;
        while (valor == null) {
            System.out.print(label + ": ");
            String input = sc.nextLine();
            try {
                double num = Double.parseDouble(input);
                if (validador.test(num)) {
                    valor = num;
                } else {
                    System.out.println(label + " inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println(label + " deve ser um número!");
            }
        }
        return valor;
    }
}