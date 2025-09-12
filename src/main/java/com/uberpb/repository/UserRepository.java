package com.uberpb.repository;

import com.uberpb.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para manipulação da entidade User.
 * Define os métodos principais para persistência e consulta.
 */
public interface UserRepository {

    /**
     * Salva um novo usuário no repositório.
     * 
     * @param user usuário a ser salvo
     * @return usuário salvo com ID gerado
     */
    User save(User user);

    /**
     * Busca um usuário pelo ID (chave primária).
     * 
     * @param id identificador único do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findById(int id);

    /**
     * Busca um usuário pelo email.
     * 
     * @param email email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Retorna todos os usuários cadastrados.
     * 
     * @return lista de usuários
     */
    List<User> findAll();

    /**
     * Atualiza um usuário existente no repositório.
     * 
     * @param user usuário com dados atualizados
     * @return usuário atualizado
     */
    User update(User user);

    /**
     * Remove um usuário pelo ID.
     * 
     * @param id identificador do usuário
     * @return true se o usuário foi removido, false caso contrário
     */
    boolean deleteById(int id);

    /**
     * Verifica se existe um usuário com o email informado.
     * 
     * @param email email a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByEmail(String email);
}
