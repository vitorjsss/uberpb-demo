package com.uberpb.repository;

import com.uberpb.model.User;

import java.util.List;
import java.util.Optional;

public class UserRepositoryJSON extends BaseRepository<User> implements UserRepository {
    
    public UserRepositoryJSON() {
        super("users");
    }
    
    @Override
    public User save(User user) {
        lock.writeLock().lock();
        try {
            // Verificar se usuário já existe
            if (user.getEmail() != null && existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Usuário com email " + user.getEmail() + " já existe");
            }
            
            // Gerar ID único se não tiver
            if (user.getId() == 0) {
                user.setId(generateNextId());
            }
            
            // Carregar usuários existentes
            List<User> users = loadAll();
            
            // Adicionar novo usuário
            users.add(user);
            
            // Salvar lista atualizada
            saveAll(users);
            
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<User> findById(int id) {
        lock.readLock().lock();
        try {
            List<User> users = loadAll();
            return users.stream()
                    .filter(user -> user.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        lock.readLock().lock();
        try {
            List<User> users = loadAll();
            return users.stream()
                    .filter(user -> email.equals(user.getEmail()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<User> findAll() {
        return loadAll();
    }
    
    @Override
    public User update(User user) {
        lock.writeLock().lock();
        try {
            List<User> users = loadAll();
            
            // Encontrar usuário existente
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == user.getId()) {
                    users.set(i, user);
                    saveAll(users);
                    return user;
                }
            }
            
            throw new IllegalArgumentException("Usuário com ID " + user.getId() + " não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        lock.writeLock().lock();
        try {
            List<User> users = loadAll();
            
            // Remover usuário se existir
            boolean removed = users.removeIf(user -> user.getId() == id);
            
            if (removed) {
                saveAll(users);
            }
            
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        lock.readLock().lock();
        try {
            List<User> users = loadAll();
            return users.stream().anyMatch(user -> email.equals(user.getEmail()));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    protected int getId(User entity) {
        return entity.getId();
    }
    
    @Override
    protected void setId(User entity, int id) {
        entity.setId(id);
    }
}
