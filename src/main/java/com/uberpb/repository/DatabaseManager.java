package com.uberpb.repository;

import com.uberpb.model.Motorista;
import com.uberpb.model.Passageiro;
import com.uberpb.model.User;
import com.uberpb.model.Veiculo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Gerenciador principal do banco de dados JSON
 * Coordena operações entre todos os repositórios específicos
 */
public class DatabaseManager {
    
    private final UserRepository userRepository;
    private final PassageiroRepository passageiroRepository;
    private final MotoristaRepository motoristaRepository;
    private final VeiculoRepository veiculoRepository;
    
    public DatabaseManager() {
        this.userRepository = new UserRepositoryJSON();
        this.passageiroRepository = new PassageiroRepositoryJSON();
        this.motoristaRepository = new MotoristaRepositoryJSON();
        this.veiculoRepository = new VeiculoRepositoryJSON();
    }
    
    // ===== OPERAÇÕES DE USUÁRIO =====
    
    public User saveUser(User user) {
        // Definir data de cadastro se não estiver definida
        if (user.getDataCadastro() == null) {
            user.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        
        return userRepository.save(user);
    }
    
    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUser(User user) {
        return userRepository.update(user);
    }
    
    public boolean deleteUser(int id) {
        return userRepository.deleteById(id);
    }
    
    // ===== OPERAÇÕES DE PASSAGEIRO =====
    
    public Passageiro savePassageiro(Passageiro passageiro) {
        // Se o passageiro já tem ID, significa que o usuário já existe
        if (passageiro.getId() > 0) {
            // Apenas salvar como passageiro
            return passageiroRepository.save(passageiro);
        } else {
            // Primeiro salvar como usuário
            User user = new User();
            user.setNome(passageiro.getNome());
            user.setSobrenome(passageiro.getSobrenome());
            user.setEmail(passageiro.getEmail());
            user.setTelefone(passageiro.getTelefone());
            user.setTipo("passageiro");
            user.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
            User savedUser = saveUser(user);
            passageiro.setId(savedUser.getId());
            
            // Depois salvar como passageiro
            return passageiroRepository.save(passageiro);
        }
    }
    
    public Optional<Passageiro> findPassageiroById(int id) {
        return passageiroRepository.findById(id);
    }
    
    public List<Passageiro> findAllPassageiros() {
        return passageiroRepository.findAll();
    }
    
    public Passageiro updatePassageiro(Passageiro passageiro) {
        // Atualizar dados do usuário também
        Optional<User> userOpt = findUserById(passageiro.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setNome(passageiro.getNome());
            user.setSobrenome(passageiro.getSobrenome());
            user.setEmail(passageiro.getEmail());
            user.setTelefone(passageiro.getTelefone());
            updateUser(user);
        }
        
        return passageiroRepository.update(passageiro);
    }
    
    public boolean deletePassageiro(int id) {
        // Deletar do repositório de passageiros
        boolean deleted = passageiroRepository.deleteById(id);
        
        // Deletar do repositório de usuários também
        if (deleted) {
            deleteUser(id);
        }
        
        return deleted;
    }
    
    public List<Passageiro> findPassageirosByLocalizacao(String localizacao) {
        return passageiroRepository.findByLocalizacao(localizacao);
    }
    
    public List<Passageiro> findPassageirosEmCorrida() {
        return passageiroRepository.findEmCorrida();
    }
    
    // ===== OPERAÇÕES DE MOTORISTA =====
    
    public Motorista saveMotorista(Motorista motorista) {
        // Se o motorista já tem ID, significa que o usuário já existe
        if (motorista.getId() > 0) {
            // Apenas salvar como motorista
            return motoristaRepository.save(motorista);
        } else {
            // Primeiro salvar como usuário
            User user = new User();
            user.setNome(motorista.getNome());
            user.setSobrenome(motorista.getSobrenome());
            user.setEmail(motorista.getEmail());
            user.setTelefone(motorista.getTelefone());
            user.setTipo("motorista");
            user.setDataCadastro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
            User savedUser = saveUser(user);
            motorista.setId(savedUser.getId());
            
            // Depois salvar como motorista
            return motoristaRepository.save(motorista);
        }
    }
    
    public Optional<Motorista> findMotoristaById(int id) {
        return motoristaRepository.findById(id);
    }
    
    public List<Motorista> findAllMotoristas() {
        return motoristaRepository.findAll();
    }
    
    public Motorista updateMotorista(Motorista motorista) {
        // Atualizar dados do usuário também
        Optional<User> userOpt = findUserById(motorista.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setNome(motorista.getNome());
            user.setSobrenome(motorista.getSobrenome());
            user.setEmail(motorista.getEmail());
            user.setTelefone(motorista.getTelefone());
            updateUser(user);
        }
        
        return motoristaRepository.update(motorista);
    }
    
    public boolean deleteMotorista(int id) {
        // Deletar do repositório de motoristas
        boolean deleted = motoristaRepository.deleteById(id);
        
        // Deletar do repositório de usuários também
        if (deleted) {
            deleteUser(id);
        }
        
        return deleted;
    }
    
    public List<Motorista> findMotoristasByLocalizacao(String localizacao) {
        return motoristaRepository.findByLocalizacao(localizacao);
    }
    
    public List<Motorista> findMotoristasDisponiveis() {
        return motoristaRepository.findDisponiveis();
    }
    
    public List<Motorista> findMotoristasAtivos() {
        return motoristaRepository.findAtivos();
    }
    
    public Optional<Motorista> findMotoristaByCnh(String cnh) {
        return motoristaRepository.findByCnh(cnh);
    }
    
    // ===== OPERAÇÕES DE VEÍCULO =====
    
    public Veiculo saveVeiculo(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }
    
    public Optional<Veiculo> findVeiculoById(int id) {
        return veiculoRepository.findById(id);
    }
    
    public List<Veiculo> findAllVeiculos() {
        return veiculoRepository.findAll();
    }
    
    public Veiculo updateVeiculo(Veiculo veiculo) {
        return veiculoRepository.update(veiculo);
    }
    
    public boolean deleteVeiculo(int id) {
        return veiculoRepository.deleteById(id);
    }
    
    public Optional<Veiculo> findVeiculoByPlaca(String placa) {
        return veiculoRepository.findByPlaca(placa);
    }
    
    public List<Veiculo> findVeiculosByTipo(String tipo) {
        return veiculoRepository.findByTipo(tipo);
    }
    
    public List<Veiculo> findVeiculosByCategoria(String categoria) {
        return veiculoRepository.findByCategoria(categoria);
    }
    
    public List<Veiculo> findVeiculosByMarca(String marca) {
        return veiculoRepository.findByMarca(marca);
    }
    
    // ===== MÉTODOS DE UTILIDADE =====
    
    /**
     * Limpa todos os dados do banco
     * Útil para testes
     */
    public void clearAllData() {
        // Implementar limpeza se necessário
        System.out.println("Limpeza de dados não implementada ainda");
    }
    
    /**
     * Retorna estatísticas do banco de dados
     */
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
