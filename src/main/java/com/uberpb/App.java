package com.uberpb;

import com.uberpb.model.User;
import com.uberpb.repository.UsuarioRepository;
import com.uberpb.repository.UsuarioRepositoryJSON;

import java.util.List;
import java.util.Optional;

/**
 * Demonstração do uso do UsuarioRepositoryJSON
 */
public class App {
    public static void main(String[] args) {
        System.out.println("=== Demonstração do UsuarioRepositoryJSON ===\n");
        
        // Criar instância do repositório
        UsuarioRepository repository = new UsuarioRepositoryJSON();
        
        // Criar alguns usuários de exemplo
        User passageiro = new User();
        passageiro.setNome("João Silva");
        passageiro.setEmail("joao@email.com");
        passageiro.setCpf("123.456.789-00");
        passageiro.setSenha("senha123");
        passageiro.setTipo("passageiro");
        
        User motorista = new User();
        motorista.setNome("Maria Santos");
        motorista.setEmail("maria@email.com");
        motorista.setCpf("987.654.321-00");
        motorista.setSenha("senha456");
        motorista.setTipo("motorista");
        
        try {
            // Salvar usuários
            System.out.println("Salvando usuários...");
            repository.save(passageiro);
            repository.save(motorista);
            System.out.println("✓ Usuários salvos com sucesso!\n");
            
            // Buscar usuário por CPF
            System.out.println("Buscando usuário por CPF...");
            Optional<User> userFound = repository.findByCpf("123.456.789-00");
            if (userFound.isPresent()) {
                User user = userFound.get();
                System.out.println("✓ Usuário encontrado: " + user.getNome() + " (" + user.getTipo() + ")");
            }
            System.out.println();
            
            // Buscar usuário por email
            System.out.println("Buscando usuário por email...");
            Optional<User> userByEmail = repository.findByEmail("maria@email.com");
            if (userByEmail.isPresent()) {
                User user = userByEmail.get();
                System.out.println("✓ Usuário encontrado: " + user.getNome() + " (" + user.getTipo() + ")");
            }
            System.out.println();
            
            // Listar todos os usuários
            System.out.println("Listando todos os usuários:");
            List<User> allUsers = repository.findAll();
            for (User user : allUsers) {
                System.out.println("  - " + user.getNome() + " | " + user.getEmail() + " | " + user.getTipo());
            }
            System.out.println();
            
            // Atualizar usuário
            System.out.println("Atualizando usuário...");
            passageiro.setNome("João Silva Santos");
            repository.update(passageiro);
            System.out.println("✓ Usuário atualizado!\n");
            
            // Verificar se usuário existe
            System.out.println("Verificando existência de usuários...");
            System.out.println("  - CPF 123.456.789-00 existe: " + repository.existsByCpf("123.456.789-00"));
            System.out.println("  - Email inexistente@email.com existe: " + repository.existsByEmail("inexistente@email.com"));
            System.out.println();
            
            // Mostrar localização do arquivo de dados
            if (repository instanceof UsuarioRepositoryJSON) {
                UsuarioRepositoryJSON jsonRepo = (UsuarioRepositoryJSON) repository;
                System.out.println("Arquivo de dados: " + jsonRepo.getDataFilePath());
            }
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
