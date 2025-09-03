package com.uberpb.repository;

import com.uberpb.model.User;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    
    /**
     * Salva um usuário no repositório
     * @param user usuário a ser salvo
     * @return usuário salvo com ID gerado
     */
    User save(User user);
    
    /**
     * Busca um usuário pelo CPF
     * @param cpf CPF do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByCpf(String cpf);
    
    /**
     * Busca um usuário pelo email
     * @param email email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Busca todos os usuários
     * @return lista de todos os usuários
     */
    List<User> findAll();
    
    /**
     * Atualiza um usuário existente
     * @param user usuário com dados atualizados
     * @return usuário atualizado
     */
    User update(User user);
    
    /**
     * Remove um usuário pelo CPF
     * @param cpf CPF do usuário a ser removido
     * @return true se removido com sucesso, false caso contrário
     */
    boolean deleteByCpf(String cpf);
    
    /**
     * Verifica se um usuário existe pelo CPF
     * @param cpf CPF do usuário
     * @return true se existe, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Verifica se um usuário existe pelo email
     * @param email email do usuário
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
}
