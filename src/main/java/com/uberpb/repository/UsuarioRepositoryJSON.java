package com.uberpb.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.uberpb.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UsuarioRepositoryJSON implements UsuarioRepository {
    
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = "users.json";
    private final Path dataPath;
    private final Path usersFilePath;
    private final ObjectMapper objectMapper;
    private final ReadWriteLock lock;
    
    public UsuarioRepositoryJSON() {
        this.dataPath = Paths.get(DATA_DIR);
        this.usersFilePath = dataPath.resolve(USERS_FILE);
        this.objectMapper = new ObjectMapper();
        this.lock = new ReentrantReadWriteLock();
        
        // Configurar o ObjectMapper para formatação legível
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Criar diretório de dados se não existir
        initializeDataDirectory();
    }
    
    private void initializeDataDirectory() {
        try {
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
            
            // Criar arquivo de usuários se não existir
            if (!Files.exists(usersFilePath)) {
                Files.write(usersFilePath, "[]".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar diretório de dados", e);
        }
    }
    
    @Override
    public User save(User user) {
        lock.writeLock().lock();
        try {
            List<User> users = loadUsers();
            
            // Verificar se usuário já existe
            if (user.getCpf() != null && existsByCpf(user.getCpf())) {
                throw new IllegalArgumentException("Usuário com CPF " + user.getCpf() + " já existe");
            }
            
            if (user.getEmail() != null && existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Usuário com email " + user.getEmail() + " já existe");
            }
            
            users.add(user);
            saveUsers(users);
            
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<User> findByCpf(String cpf) {
        lock.readLock().lock();
        try {
            List<User> users = loadUsers();
            return users.stream()
                    .filter(user -> cpf.equals(user.getCpf()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        lock.readLock().lock();
        try {
            List<User> users = loadUsers();
            return users.stream()
                    .filter(user -> email.equals(user.getEmail()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<User> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(loadUsers());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public User update(User user) {
        lock.writeLock().lock();
        try {
            List<User> users = loadUsers();
            
            for (int i = 0; i < users.size(); i++) {
                if (user.getCpf() != null && user.getCpf().equals(users.get(i).getCpf())) {
                    users.set(i, user);
                    saveUsers(users);
                    return user;
                }
            }
            
            throw new IllegalArgumentException("Usuário com CPF " + user.getCpf() + " não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean deleteByCpf(String cpf) {
        lock.writeLock().lock();
        try {
            List<User> users = loadUsers();
            boolean removed = users.removeIf(user -> cpf.equals(user.getCpf()));
            
            if (removed) {
                saveUsers(users);
            }
            
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        lock.readLock().lock();
        try {
            List<User> users = loadUsers();
            return users.stream().anyMatch(user -> cpf.equals(user.getCpf()));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        lock.readLock().lock();
        try {
            List<User> users = loadUsers();
            return users.stream().anyMatch(user -> email.equals(user.getEmail()));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private List<User> loadUsers() {
        try {
            if (!Files.exists(usersFilePath) || Files.size(usersFilePath) == 0) {
                return new ArrayList<>();
            }
            
            String content = Files.readString(usersFilePath);
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(content, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuários do arquivo", e);
        }
    }
    
    private void saveUsers(List<User> users) {
        try {
            String jsonContent = objectMapper.writeValueAsString(users);
            Files.write(usersFilePath, jsonContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuários no arquivo", e);
        }
    }
    
    /**
     * Limpa todos os dados do repositório
     * Útil para testes
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            saveUsers(new ArrayList<>());
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Retorna o caminho do arquivo de dados
     * Útil para testes e debug
     */
    public Path getDataFilePath() {
        return usersFilePath;
    }
}
