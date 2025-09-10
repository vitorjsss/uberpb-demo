package com.uberpb.examples;

import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.model.Veiculo;
import com.uberpb.repository.DatabaseManager;

/**
 * Exemplo simples demonstrando o cadastro em múltiplos JSONs
 */
public class SimpleExample {
    
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        
        System.out.println("=== CADASTRANDO USUÁRIOS EM MÚLTIPLOS JSONs ===\n");
        
        // Cadastrar um passageiro
        System.out.println("1. Cadastrando passageiro...");
        Passageiro passageiro = new Passageiro(0, "São Paulo - Centro", false);
        passageiro.setNome("Ana");
        passageiro.setSobrenome("Costa");
        passageiro.setEmail("ana.costa@email.com");
        passageiro.setTelefone("(11) 99999-1111");
        passageiro.setIdade(28);
        
        Passageiro savedPassageiro = db.savePassageiro(passageiro);
        System.out.println("✓ Passageiro cadastrado com ID: " + savedPassageiro.getId());
        System.out.println("  → Salvo em: database/users/users.json");
        System.out.println("  → Salvo em: database/passageiros/passageiros.json");
        
        // Cadastrar um motorista
        System.out.println("\n2. Cadastrando motorista...");
        Motorista motorista = new Motorista(0, true, "98765432100", "2026-06-30", 4.9, 200, true, "São Paulo - Zona Norte");
        motorista.setNome("Pedro");
        motorista.setSobrenome("Ferreira");
        motorista.setEmail("pedro.ferreira@email.com");
        motorista.setTelefone("(11) 88888-2222");
        
        Motorista savedMotorista = db.saveMotorista(motorista);
        System.out.println("✓ Motorista cadastrado com ID: " + savedMotorista.getId());
        System.out.println("  → Salvo em: database/users/users.json");
        System.out.println("  → Salvo em: database/motoristas/motoristas.json");
        
        // Cadastrar um veículo
        System.out.println("\n3. Cadastrando veículo...");
        Veiculo veiculo = new Veiculo(0, "Corolla", "Toyota", "2021", "Branco", "XYZ-9876", "Carro", "Confort");
        
        Veiculo savedVeiculo = db.saveVeiculo(veiculo);
        System.out.println("✓ Veículo cadastrado com ID: " + savedVeiculo.getId());
        System.out.println("  → Salvo em: database/veiculos/veiculos.json");
        
        // Mostrar estatísticas
        System.out.println("\n4. Estatísticas do banco:");
        System.out.println(db.getDatabaseStats());
        
        System.out.println("\n=== CADASTRO CONCLUÍDO ===");
        System.out.println("Verifique os arquivos JSON na pasta 'database' para ver os dados salvos!");
        System.out.println("\nEstrutura criada:");
        System.out.println("database/");
        System.out.println("├── users/users.json (contém dados básicos de todos os usuários)");
        System.out.println("├── passageiros/passageiros.json (contém dados específicos dos passageiros)");
        System.out.println("├── motoristas/motoristas.json (contém dados específicos dos motoristas)");
        System.out.println("├── veiculos/veiculos.json (contém dados dos veículos)");
        System.out.println("└── id_counter.json (contém contadores de ID para cada entidade)");
    }
}
