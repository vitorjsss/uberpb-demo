package com.uberpb.examples;

import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.model.User;
import com.uberpb.model.Veiculo;
import com.uberpb.repository.DatabaseManager;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Exemplo de uso do sistema de banco de dados JSON organizado
 */
public class DatabaseExample {
    
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        
        System.out.println("=== EXEMPLO DE USO DO BANCO DE DADOS JSON ===\n");
        
        // 1. Criar um usuário básico
        System.out.println("1. Criando usuário básico...");
        User user = new User();
        user.setNome("João");
        user.setSobrenome("Silva");
        user.setEmail("joao.silva@email.com");
        user.setTelefone("(11) 99999-9999");
        user.setTipo("usuario");
        user.setDataCadastro(LocalDateTime.now());
        
        User savedUser = db.saveUser(user);
        System.out.println("Usuário criado com ID: " + savedUser.getId());
        
        // 2. Criar um passageiro
        System.out.println("\n2. Criando passageiro...");
        Passageiro passageiro = new Passageiro(0, "São Paulo - Centro", false);
        passageiro.setNome("Maria");
        passageiro.setSobrenome("Santos");
        passageiro.setEmail("maria.santos@email.com");
        passageiro.setTelefone("(11) 88888-8888");
        passageiro.setIdade(25);
        passageiro.getMetodosPagamento().add("Cartão de Crédito");
        passageiro.getMetodosPagamento().add("PIX");
        
        Passageiro savedPassageiro = db.savePassageiro(passageiro);
        System.out.println("Passageiro criado com ID: " + savedPassageiro.getId());
        
        // 3. Criar um motorista
        System.out.println("\n3. Criando motorista...");
        Motorista motorista = new Motorista(0, true, "12345678901", "2025-12-31", 4.8, 150, true, "São Paulo - Zona Sul");
        motorista.setNome("Carlos");
        motorista.setSobrenome("Oliveira");
        motorista.setEmail("carlos.oliveira@email.com");
        motorista.setTelefone("(11) 77777-7777");
        
        Motorista savedMotorista = db.saveMotorista(motorista);
        System.out.println("Motorista criado com ID: " + savedMotorista.getId());
        
        // 4. Criar um veículo
        System.out.println("\n4. Criando veículo...");
        Veiculo veiculo = new Veiculo(0, "Civic", "Honda", "2020", "Prata", "ABC-1234", "Carro", "Econômico");
        
        Veiculo savedVeiculo = db.saveVeiculo(veiculo);
        System.out.println("Veículo criado com ID: " + savedVeiculo.getId());
        
        // 5. Listar todos os dados
        System.out.println("\n5. Listando todos os dados...");
        System.out.println("\n--- USUÁRIOS ---");
        List<User> users = db.findAllUsers();
        for (User u : users) {
            System.out.println("ID: " + u.getId() + ", Nome: " + u.getNome() + " " + u.getSobrenome() + 
                             ", Email: " + u.getEmail() + ", Tipo: " + u.getTipo());
        }
        
        System.out.println("\n--- PASSAGEIROS ---");
        List<Passageiro> passageiros = db.findAllPassageiros();
        for (Passageiro p : passageiros) {
            System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome() + " " + p.getSobrenome() + 
                             ", Localização: " + p.getLocalizacaoAtual() + ", Em Corrida: " + p.isEmCorrida());
        }
        
        System.out.println("\n--- MOTORISTAS ---");
        List<Motorista> motoristas = db.findAllMotoristas();
        for (Motorista m : motoristas) {
            System.out.println("ID: " + m.getId() + ", Nome: " + m.getNome() + " " + m.getSobrenome() + 
                             ", CNH: " + m.getCnh() + ", Disponível: " + m.isDisponivel());
        }
        
        System.out.println("\n--- VEÍCULOS ---");
        List<Veiculo> veiculos = db.findAllVeiculos();
        for (Veiculo v : veiculos) {
            System.out.println("ID: " + v.getId() + ", Modelo: " + v.getModelo() + " " + v.getMarca() + 
                             ", Placa: " + v.getPlaca() + ", Tipo: " + v.getTipo());
        }
        
        // 6. Mostrar estatísticas
        System.out.println("\n6. Estatísticas do banco de dados:");
        System.out.println(db.getDatabaseStats());
        
        // 7. Buscar motoristas disponíveis
        System.out.println("\n7. Motoristas disponíveis:");
        List<Motorista> motoristasDisponiveis = db.findMotoristasDisponiveis();
        for (Motorista m : motoristasDisponiveis) {
            System.out.println("- " + m.getNome() + " " + m.getSobrenome() + " (CNH: " + m.getCnh() + ")");
        }
        
        // 8. Buscar veículos por tipo
        System.out.println("\n8. Veículos do tipo 'Carro':");
        List<Veiculo> carros = db.findVeiculosByTipo("Carro");
        for (Veiculo v : carros) {
            System.out.println("- " + v.getMarca() + " " + v.getModelo() + " (" + v.getPlaca() + ")");
        }
        
        System.out.println("\n=== EXEMPLO CONCLUÍDO ===");
        System.out.println("Verifique os arquivos JSON na pasta 'database' para ver os dados salvos!");
    }
}
