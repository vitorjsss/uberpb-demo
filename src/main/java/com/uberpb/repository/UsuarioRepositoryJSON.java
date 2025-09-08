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
    
    private static final String DATA_DIR = "database";
    private static final String USERS_DIR = "users";
    private static final String PASSAGEIROS_DIR = "passageiros";
    private static final String MOTORISTAS_DIR = "motoristas";
    private static final String ID_COUNTER_FILE = "id_counter.txt";
    
    private final Path databasePath;
    private final Path usersPath;
    private final Path passageirosPath;
    private final Path motoristasPath;
    private final Path idCounterPath;
    private final ObjectMapper objectMapper;
    private final ReadWriteLock lock;
    
    public UsuarioRepositoryJSON() {
        this.databasePath = Paths.get(DATA_DIR);
        this.usersPath = databasePath.resolve(USERS_DIR);
        this.passageirosPath = usersPath.resolve(PASSAGEIROS_DIR);
        this.motoristasPath = usersPath.resolve(MOTORISTAS_DIR);
        this.idCounterPath = databasePath.resolve(ID_COUNTER_FILE);
        this.objectMapper = new ObjectMapper();
        this.lock = new ReentrantReadWriteLock();
        
        // Configurar o ObjectMapper para formatação legível
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Criar diretório de dados se não existir
        initializeDataDirectory();
    }
    
    private void initializeDataDirectory() {
        try {
            // Criar estrutura de diretórios
            if (!Files.exists(databasePath)) {
                Files.createDirectories(databasePath);
            }
            if (!Files.exists(usersPath)) {
                Files.createDirectories(usersPath);
            }
            if (!Files.exists(passageirosPath)) {
                Files.createDirectories(passageirosPath);
            }
            if (!Files.exists(motoristasPath)) {
                Files.createDirectories(motoristasPath);
            }
            
            // Criar arquivo de contador de IDs se não existir
            if (!Files.exists(idCounterPath)) {
                Files.write(idCounterPath, "1".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar diretório de dados", e);
        }
    }
    
    @Override
    public User save(User user) {
        lock.writeLock().lock();
        try {
            // Verificar se usuário já existe
            if (user.getCpf() != null && existsByCpf(user.getCpf())) {
                throw new IllegalArgumentException("Usuário com CPF " + user.getCpf() + " já existe");
            }
            
            if (user.getEmail() != null && existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Usuário com email " + user.getEmail() + " já existe");
            }
            
            // Gerar ID único
            Long newId = generateNextId();
            user.setId(newId);
            
            // Salvar usuário em arquivo separado por tipo
            saveUserToFile(user);
            
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<User> findByCpf(String cpf) {
        lock.readLock().lock();
        try {
            List<User> users = loadAllUsers();
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
            List<User> users = loadAllUsers();
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
            return new ArrayList<>(loadAllUsers());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public User update(User user) {
        lock.writeLock().lock();
        try {
            // Verificar se usuário existe
            if (!existsByCpf(user.getCpf())) {
                throw new IllegalArgumentException("Usuário com CPF " + user.getCpf() + " não encontrado");
            }
            
            // Atualizar arquivo do usuário
            saveUserToFile(user);
            
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean deleteByCpf(String cpf) {
        lock.writeLock().lock();
        try {
            Optional<User> userOpt = findByCpf(cpf);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Path userFile = getUserFilePath(user);
                
                if (Files.exists(userFile)) {
                    Files.delete(userFile);
                    return true;
                }
            }
            
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        lock.readLock().lock();
        try {
            List<User> users = loadAllUsers();
            return users.stream().anyMatch(user -> cpf.equals(user.getCpf()));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        lock.readLock().lock();
        try {
            List<User> users = loadAllUsers();
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
    
    private Long generateNextId() {
        try {
            String currentId = Files.readString(idCounterPath).trim();
            Long nextId = Long.parseLong(currentId);
            
            // Incrementar e salvar próximo ID
            Files.write(idCounterPath, String.valueOf(nextId + 1).getBytes());
            
            return nextId;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar ID único", e);
        }
    }
    
    private void saveUserToFile(User user) {
        try {
            // Determinar diretório baseado no tipo
            Path userDir = "passageiro".equals(user.getTipo()) ? passageirosPath : motoristasPath;
            
            // Criar arquivo com nome baseado no ID
            Path userFile = userDir.resolve("user_" + user.getId() + ".json");
            
            // Salvar usuário em arquivo JSON separado
            String jsonContent = objectMapper.writeValueAsString(user);
            Files.write(userFile, jsonContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuário no arquivo", e);
        }
    }
    
    private List<User> loadAllUsers() {
        List<User> allUsers = new ArrayList<>();
        
        try {
            // Carregar passageiros
            if (Files.exists(passageirosPath)) {
                Files.list(passageirosPath)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(path -> {
                         try {
                             String content = Files.readString(path);
                             User user = objectMapper.readValue(content, User.class);
                             allUsers.add(user);
                         } catch (IOException e) {
                             // Ignorar arquivos corrompidos
                         }
                     });
            }
            
            // Carregar motoristas
            if (Files.exists(motoristasPath)) {
                Files.list(motoristasPath)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(path -> {
                         try {
                             String content = Files.readString(path);
                             User user = objectMapper.readValue(content, User.class);
                             allUsers.add(user);
                         } catch (IOException e) {
                             // Ignorar arquivos corrompidos
                         }
                     });
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuários", e);
        }
        
        return allUsers;
    }
    
    private Path getUserFilePath(User user) {
        // Determinar diretório baseado no tipo
        Path userDir = "passageiro".equals(user.getTipo()) ? passageirosPath : motoristasPath;
        
        // Retornar caminho do arquivo
        return userDir.resolve("user_" + user.getId() + ".json");
    }
    
    private void saveUsers(List<User> users) {
        // Este método não é mais usado, mas mantido para compatibilidade
        // Cada usuário é salvo em arquivo separado
    }
    
    /**
     * Limpa todos os dados do repositório
     * Útil para testes
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            // Remover todos os arquivos de usuários
            clearDirectory(passageirosPath);
            clearDirectory(motoristasPath);
            
            // Resetar contador de IDs
            Files.write(idCounterPath, "1".getBytes());
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private void clearDirectory(Path directory) {
        try {
            if (Files.exists(directory)) {
                Files.list(directory)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                         } catch (IOException e) {
                             // Ignorar erros de limpeza
                         }
                     });
            }
        } catch (IOException e) {
            // Ignorar erros de limpeza
        }
    }
    
    /**
     * Retorna o caminho do diretório de dados
     * Útil para testes e debug
     */
    public Path getDataFilePath() {
        return databasePath;
    }
}
