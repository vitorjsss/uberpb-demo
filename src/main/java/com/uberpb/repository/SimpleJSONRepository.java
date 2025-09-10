package com.uberpb.repository;

import com.uberpb.model.User;
import com.uberpb.model.Passageiro;
import com.uberpb.model.Motorista;
import com.uberpb.model.Veiculo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Repositório simples que funciona sem dependências externas
 * Usa manipulação de strings para simular JSON
 */
public class SimpleJSONRepository {
    
    private static final String DATA_DIR = "database";
    private final ReadWriteLock lock;
    private final Path usersPath;
    private final Path passageirosPath;
    private final Path motoristasPath;
    private final Path veiculosPath;
    private final Path idCounterPath;
    
    public SimpleJSONRepository() {
        this.lock = new ReentrantReadWriteLock();
        this.usersPath = Paths.get(DATA_DIR, "users", "users.txt");
        this.passageirosPath = Paths.get(DATA_DIR, "passageiros", "passageiros.txt");
        this.motoristasPath = Paths.get(DATA_DIR, "motoristas", "motoristas.txt");
        this.veiculosPath = Paths.get(DATA_DIR, "veiculos", "veiculos.txt");
        this.idCounterPath = Paths.get(DATA_DIR, "id_counter.txt");
        
        initializeDataFiles();
    }
    
    private void initializeDataFiles() {
        try {
            // Criar diretórios
            Files.createDirectories(usersPath.getParent());
            Files.createDirectories(passageirosPath.getParent());
            Files.createDirectories(motoristasPath.getParent());
            Files.createDirectories(veiculosPath.getParent());
            
            // Criar arquivos se não existirem
            if (!Files.exists(usersPath)) {
                Files.write(usersPath, "".getBytes());
            }
            if (!Files.exists(passageirosPath)) {
                Files.write(passageirosPath, "".getBytes());
            }
            if (!Files.exists(motoristasPath)) {
                Files.write(motoristasPath, "".getBytes());
            }
            if (!Files.exists(veiculosPath)) {
                Files.write(veiculosPath, "".getBytes());
            }
            if (!Files.exists(idCounterPath)) {
                Files.write(idCounterPath, "1".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar arquivos de dados", e);
        }
    }
    
    // ===== OPERAÇÕES DE USUÁRIO =====
    
    public User saveUser(User user) {
        lock.writeLock().lock();
        try {
            if (user.getId() == 0) {
                user.setId(generateNextId("users"));
            }
            
            List<User> users = loadUsers();
            users.add(user);
            saveUsers(users);
            
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public Optional<User> findUserByEmail(String email) {
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
    
    public List<User> findAllUsers() {
        return loadUsers();
    }
    
    public User updateUser(User user) {
        lock.writeLock().lock();
        try {
            List<User> users = loadUsers();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == user.getId()) {
                    users.set(i, user);
                    saveUsers(users);
                    return user;
                }
            }
            throw new IllegalArgumentException("Usuário não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // ===== OPERAÇÕES DE PASSAGEIRO =====
    
    public Passageiro savePassageiro(Passageiro passageiro) {
        lock.writeLock().lock();
        try {
            if (passageiro.getId() == 0) {
                passageiro.setId(generateNextId("passageiros"));
            }
            
            List<Passageiro> passageiros = loadPassageiros();
            passageiros.add(passageiro);
            savePassageiros(passageiros);
            
            return passageiro;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public Optional<Passageiro> findPassageiroById(int id) {
        lock.readLock().lock();
        try {
            List<Passageiro> passageiros = loadPassageiros();
            return passageiros.stream()
                    .filter(passageiro -> passageiro.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<Passageiro> findAllPassageiros() {
        return loadPassageiros();
    }
    
    public Passageiro updatePassageiro(Passageiro passageiro) {
        lock.writeLock().lock();
        try {
            List<Passageiro> passageiros = loadPassageiros();
            for (int i = 0; i < passageiros.size(); i++) {
                if (passageiros.get(i).getId() == passageiro.getId()) {
                    passageiros.set(i, passageiro);
                    savePassageiros(passageiros);
                    return passageiro;
                }
            }
            throw new IllegalArgumentException("Passageiro não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // ===== OPERAÇÕES DE MOTORISTA =====
    
    public Motorista saveMotorista(Motorista motorista) {
        lock.writeLock().lock();
        try {
            if (motorista.getId() == 0) {
                motorista.setId(generateNextId("motoristas"));
            }
            
            List<Motorista> motoristas = loadMotoristas();
            motoristas.add(motorista);
            saveMotoristas(motoristas);
            
            return motorista;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public Optional<Motorista> findMotoristaById(int id) {
        lock.readLock().lock();
        try {
            List<Motorista> motoristas = loadMotoristas();
            return motoristas.stream()
                    .filter(motorista -> motorista.getId() == id)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<Motorista> findAllMotoristas() {
        return loadMotoristas();
    }
    
    public Motorista updateMotorista(Motorista motorista) {
        lock.writeLock().lock();
        try {
            List<Motorista> motoristas = loadMotoristas();
            for (int i = 0; i < motoristas.size(); i++) {
                if (motoristas.get(i).getId() == motorista.getId()) {
                    motoristas.set(i, motorista);
                    saveMotoristas(motoristas);
                    return motorista;
                }
            }
            throw new IllegalArgumentException("Motorista não encontrado");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // ===== OPERAÇÕES DE VEÍCULO =====
    
    public Veiculo saveVeiculo(Veiculo veiculo) {
        lock.writeLock().lock();
        try {
            if (veiculo.getId() == 0) {
                veiculo.setId(generateNextId("veiculos"));
            }
            
            List<Veiculo> veiculos = loadVeiculos();
            veiculos.add(veiculo);
            saveVeiculos(veiculos);
            
            return veiculo;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public List<Veiculo> findAllVeiculos() {
        return loadVeiculos();
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    private int generateNextId(String entityType) {
        try {
            String content = Files.readString(idCounterPath);
            int currentId = Integer.parseInt(content.trim());
            int nextId = currentId + 1;
            Files.write(idCounterPath, String.valueOf(nextId).getBytes());
            return currentId;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar ID", e);
        }
    }
    
    private List<User> loadUsers() {
        try {
            if (!Files.exists(usersPath) || Files.size(usersPath) == 0) {
                return new ArrayList<>();
            }
            
            String content = Files.readString(usersPath);
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            List<User> users = new ArrayList<>();
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    users.add(parseUser(line));
                }
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuários", e);
        }
    }
    
    private void saveUsers(List<User> users) {
        try {
            StringBuilder sb = new StringBuilder();
            for (User user : users) {
                sb.append(formatUser(user)).append("\n");
            }
            Files.write(usersPath, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuários", e);
        }
    }
    
    private List<Passageiro> loadPassageiros() {
        try {
            if (!Files.exists(passageirosPath) || Files.size(passageirosPath) == 0) {
                return new ArrayList<>();
            }
            
            String content = Files.readString(passageirosPath);
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            List<Passageiro> passageiros = new ArrayList<>();
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    passageiros.add(parsePassageiro(line));
                }
            }
            return passageiros;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar passageiros", e);
        }
    }
    
    private void savePassageiros(List<Passageiro> passageiros) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Passageiro passageiro : passageiros) {
                sb.append(formatPassageiro(passageiro)).append("\n");
            }
            Files.write(passageirosPath, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar passageiros", e);
        }
    }
    
    private List<Motorista> loadMotoristas() {
        try {
            if (!Files.exists(motoristasPath) || Files.size(motoristasPath) == 0) {
                return new ArrayList<>();
            }
            
            String content = Files.readString(motoristasPath);
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            List<Motorista> motoristas = new ArrayList<>();
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    motoristas.add(parseMotorista(line));
                }
            }
            return motoristas;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar motoristas", e);
        }
    }
    
    private void saveMotoristas(List<Motorista> motoristas) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Motorista motorista : motoristas) {
                sb.append(formatMotorista(motorista)).append("\n");
            }
            Files.write(motoristasPath, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar motoristas", e);
        }
    }
    
    private List<Veiculo> loadVeiculos() {
        try {
            if (!Files.exists(veiculosPath) || Files.size(veiculosPath) == 0) {
                return new ArrayList<>();
            }
            
            String content = Files.readString(veiculosPath);
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            List<Veiculo> veiculos = new ArrayList<>();
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    veiculos.add(parseVeiculo(line));
                }
            }
            return veiculos;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar veículos", e);
        }
    }
    
    private void saveVeiculos(List<Veiculo> veiculos) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Veiculo veiculo : veiculos) {
                sb.append(formatVeiculo(veiculo)).append("\n");
            }
            Files.write(veiculosPath, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar veículos", e);
        }
    }
    
    // ===== MÉTODOS DE PARSING =====
    
    private User parseUser(String line) {
        String[] parts = line.split("\\|");
        User user = new User();
        user.setId(Integer.parseInt(parts[0]));
        user.setUsername(parts[1]);
        user.setSenha(parts[2]);
        user.setNome(parts[3]);
        user.setSobrenome(parts[4]);
        user.setEmail(parts[5]);
        user.setTelefone(parts[6]);
        user.setTipo(parts[7]);
        user.setDataCadastro(parts[8]);
        return user;
    }
    
    private String formatUser(User user) {
        return user.getId() + "|" + user.getUsername() + "|" + user.getSenha() + "|" + 
               user.getNome() + "|" + user.getSobrenome() + "|" + user.getEmail() + "|" + 
               user.getTelefone() + "|" + user.getTipo() + "|" + user.getDataCadastro();
    }
    
    private Passageiro parsePassageiro(String line) {
        String[] parts = line.split("\\|");
        Passageiro passageiro = new Passageiro(Integer.parseInt(parts[0]), parts[10], Boolean.parseBoolean(parts[11]));
        passageiro.setUsername(parts[1]);
        passageiro.setSenha(parts[2]);
        passageiro.setNome(parts[3]);
        passageiro.setSobrenome(parts[4]);
        passageiro.setEmail(parts[5]);
        passageiro.setTelefone(parts[6]);
        passageiro.setTipo(parts[7]);
        passageiro.setDataCadastro(parts[8]);
        passageiro.setIdade(Integer.parseInt(parts[9]));
        passageiro.setAvaliacaoMedia(Double.parseDouble(parts[12]));
        return passageiro;
    }
    
    private String formatPassageiro(Passageiro passageiro) {
        return passageiro.getId() + "|" + passageiro.getUsername() + "|" + passageiro.getSenha() + "|" + 
               passageiro.getNome() + "|" + passageiro.getSobrenome() + "|" + passageiro.getEmail() + "|" + 
               passageiro.getTelefone() + "|" + passageiro.getTipo() + "|" + passageiro.getDataCadastro() + "|" + 
               passageiro.getIdade() + "|" + passageiro.getLocalizacaoAtual() + "|" + passageiro.isEmCorrida() + "|" + 
               passageiro.getAvaliacaoMedia();
    }
    
    private Motorista parseMotorista(String line) {
        String[] parts = line.split("\\|");
        Motorista motorista = new Motorista(Integer.parseInt(parts[0]), Boolean.parseBoolean(parts[9]), 
                                          parts[10], parts[11], Double.parseDouble(parts[12]), 
                                          Integer.parseInt(parts[13]), Boolean.parseBoolean(parts[14]), parts[15]);
        motorista.setUsername(parts[1]);
        motorista.setSenha(parts[2]);
        motorista.setNome(parts[3]);
        motorista.setSobrenome(parts[4]);
        motorista.setEmail(parts[5]);
        motorista.setTelefone(parts[6]);
        motorista.setTipo(parts[7]);
        motorista.setDataCadastro(parts[8]);
        return motorista;
    }
    
    private String formatMotorista(Motorista motorista) {
        return motorista.getId() + "|" + motorista.getUsername() + "|" + motorista.getSenha() + "|" + 
               motorista.getNome() + "|" + motorista.getSobrenome() + "|" + motorista.getEmail() + "|" + 
               motorista.getTelefone() + "|" + motorista.getTipo() + "|" + motorista.getDataCadastro() + "|" + 
               motorista.isAtivo() + "|" + motorista.getCnh() + "|" + motorista.getValidadeCnh() + "|" + 
               motorista.getAvaliacaoMedia() + "|" + motorista.getTotalAvaliacoes() + "|" + 
               motorista.isDisponivel() + "|" + motorista.getLocalizacaoAtual();
    }
    
    private Veiculo parseVeiculo(String line) {
        String[] parts = line.split("\\|");
        return new Veiculo(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], 
                          parts[4], parts[5], parts[6], parts[7]);
    }
    
    private String formatVeiculo(Veiculo veiculo) {
        return veiculo.getId() + "|" + veiculo.getModelo() + "|" + veiculo.getMarca() + "|" + 
               veiculo.getAno() + "|" + veiculo.getCor() + "|" + veiculo.getPlaca() + "|" + 
               veiculo.getTipo() + "|" + veiculo.getCategoria();
    }
    
    // ===== MÉTODOS DE ESTATÍSTICAS =====
    
    public String getDatabaseStats() {
        int totalUsers = findAllUsers().size();
        int totalPassageiros = findAllPassageiros().size();
        int totalMotoristas = findAllMotoristas().size();
        int totalVeiculos = findAllVeiculos().size();
        
        return String.format(
            "=== ESTATÍSTICAS DO BANCO DE DADOS ===\n" +
            "Total de Usuários: %d\n" +
            "Total de Passageiros: %d\n" +
            "Total de Motoristas: %d\n" +
            "Total de Veículos: %d\n" +
            "=====================================",
            totalUsers, totalPassageiros, totalMotoristas, totalVeiculos
        );
    }
}
