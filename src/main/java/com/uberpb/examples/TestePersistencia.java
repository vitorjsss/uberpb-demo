package com.uberpb.examples;

import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.model.User;
import com.uberpb.repository.DatabaseManager;

import java.time.LocalDateTime;

/**
 * Exemplo de teste da persistência do sistema
 */
public class TestePersistencia {
    
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        
        System.out.println("=== TESTE DE PERSISTÊNCIA DO SISTEMA ===\n");
        
        // 1. Criar um usuário
        System.out.println("1. Criando usuário...");
        User user = new User();
        user.setNome("João");
        user.setSobrenome("Silva");
        user.setEmail("joao.silva@teste.com");
        user.setTelefone("(11) 99999-9999");
        user.setTipo("usuario");
        user.setDataCadastro(LocalDateTime.now());
        
        User savedUser = db.saveUser(user);
        System.out.println("✓ Usuário criado com ID: " + savedUser.getId());
        
        // 2. Criar um passageiro
        System.out.println("\n2. Criando passageiro...");
        Passageiro passageiro = new Passageiro(savedUser.getId(), "São Paulo - Centro", false);
        passageiro.setNome(savedUser.getNome());
        passageiro.setSobrenome(savedUser.getSobrenome());
        passageiro.setEmail(savedUser.getEmail());
        passageiro.setTelefone(savedUser.getTelefone());
        passageiro.setTipo("passageiro");
        passageiro.setDataCadastro(savedUser.getDataCadastro());
        passageiro.setIdade(25);
        
        Passageiro savedPassageiro = db.savePassageiro(passageiro);
        System.out.println("✓ Passageiro criado com ID: " + savedPassageiro.getId());
        
        // 3. Criar um motorista
        System.out.println("\n3. Criando motorista...");
        Motorista motorista = new Motorista(0, true, "12345678901", "2025-12-31", 4.8, 150, true, "São Paulo - Zona Sul");
        motorista.setNome("Carlos");
        motorista.setSobrenome("Oliveira");
        motorista.setEmail("carlos.oliveira@teste.com");
        motorista.setTelefone("(11) 88888-8888");
        motorista.setTipo("motorista");
        motorista.setDataCadastro(LocalDateTime.now());
        
        Motorista savedMotorista = db.saveMotorista(motorista);
        System.out.println("✓ Motorista criado com ID: " + savedMotorista.getId());
        
        // 4. Verificar se os dados foram persistidos
        System.out.println("\n4. Verificando persistência...");
        
        // Buscar usuário por email
        var userFound = db.findUserByEmail("joao.silva@teste.com");
        if (userFound.isPresent()) {
            System.out.println("✓ Usuário encontrado: " + userFound.get().getNome() + " " + userFound.get().getSobrenome());
        } else {
            System.out.println("✗ Usuário não encontrado!");
        }
        
        // Buscar passageiro por ID
        var passageiroFound = db.findPassageiroById(savedPassageiro.getId());
        if (passageiroFound.isPresent()) {
            System.out.println("✓ Passageiro encontrado: " + passageiroFound.get().getNome() + " (Idade: " + passageiroFound.get().getIdade() + ")");
        } else {
            System.out.println("✗ Passageiro não encontrado!");
        }
        
        // Buscar motorista por ID
        var motoristaFound = db.findMotoristaById(savedMotorista.getId());
        if (motoristaFound.isPresent()) {
            System.out.println("✓ Motorista encontrado: " + motoristaFound.get().getNome() + " (CNH: " + motoristaFound.get().getCnh() + ")");
        } else {
            System.out.println("✗ Motorista não encontrado!");
        }
        
        // 5. Mostrar estatísticas
        System.out.println("\n5. Estatísticas do banco de dados:");
        System.out.println(db.getDatabaseStats());
        
        // 6. Testar atualização
        System.out.println("\n6. Testando atualização...");
        passageiro.setLocalizacaoAtual("São Paulo - Zona Norte");
        db.updatePassageiro(passageiro);
        System.out.println("✓ Localização do passageiro atualizada");
        
        // Verificar se a atualização foi persistida
        var passageiroAtualizado = db.findPassageiroById(passageiro.getId());
        if (passageiroAtualizado.isPresent()) {
            System.out.println("✓ Nova localização: " + passageiroAtualizado.get().getLocalizacaoAtual());
        }
        
        System.out.println("\n=== TESTE CONCLUÍDO ===");
        System.out.println("Todos os dados foram persistidos nos arquivos JSON!");
        System.out.println("Verifique a pasta 'database' para ver os arquivos criados.");
    }
}
