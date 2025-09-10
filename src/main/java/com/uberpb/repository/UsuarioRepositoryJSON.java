package com.uberpb.repository;

import com.uberpb.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de usuários que delega operações para o DatabaseManager
 * Mantém compatibilidade com a interface UsuarioRepository existente
 */
public class UsuarioRepositoryJSON implements UsuarioRepository {
    
    private final DatabaseManager databaseManager;
    
    public UsuarioRepositoryJSON() {
        this.databaseManager = new DatabaseManager();
    }
    
    @Override
    public User save(User user) {
        return databaseManager.saveUser(user);
    }
    
    @Override
    public Optional<User> findByCpf(String cpf) {
        // Como o modelo User não tem CPF, vamos buscar por email ou ID
        // Este método pode ser adaptado conforme necessário
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return databaseManager.findUserByEmail(email);
    }
    
    @Override
    public List<User> findAll() {
        return databaseManager.findAllUsers();
    }
    
    @Override
    public User update(User user) {
        return databaseManager.updateUser(user);
    }
    
    @Override
    public boolean deleteByCpf(String cpf) {
        // Como o modelo User não tem CPF, este método pode ser adaptado
        return false;
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        // Como o modelo User não tem CPF, este método pode ser adaptado
        return false;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return databaseManager.findUserByEmail(email).isPresent();
    }
    
    /**
     * Limpa todos os dados do repositório
     * Útil para testes
     */
    public void clear() {
        databaseManager.clearAllData();
    }
    
    /**
     * Retorna estatísticas do banco de dados
     */
    public String getDatabaseStats() {
        return databaseManager.getDatabaseStats();
    }
}
