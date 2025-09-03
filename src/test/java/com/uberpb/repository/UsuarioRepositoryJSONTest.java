package com.uberpb.repository;

import com.uberpb.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryJSONTest {
    
    private UsuarioRepositoryJSON repository;
    private Path testDataPath;
    
    @Before
    public void setUp() {
        // Usar diretório de teste separado
        testDataPath = Path.of("test-data");
        repository = new UsuarioRepositoryJSON();
        
        // Limpar dados de teste anteriores
        repository.clear();
    }
    
    @After
    public void tearDown() {
        // Limpar dados de teste
        repository.clear();
        
        // Remover diretório de teste se existir
        try {
            if (Files.exists(testDataPath)) {
                Files.walk(testDataPath)
                     .sorted((a, b) -> b.compareTo(a))
                     .forEach(path -> {
                         try {
                             Files.deleteIfExists(path);
                         } catch (Exception e) {
                             // Ignorar erros de limpeza
                         }
                     });
            }
        } catch (Exception e) {
            // Ignorar erros de limpeza
        }
    }
    
    @Test
    public void testSaveAndFindByCpf() {
        // Arrange
        User user = new User();
        user.setNome("João Silva");
        user.setEmail("joao@email.com");
        user.setCpf("123.456.789-00");
        user.setSenha("senha123");
        user.setTipo("passageiro");
        
        // Act
        User savedUser = repository.save(user);
        Optional<User> foundUser = repository.findByCpf("123.456.789-00");
        
        // Assert
        assertNotNull(savedUser);
        assertTrue(foundUser.isPresent());
        assertEquals("João Silva", foundUser.get().getNome());
        assertEquals("joao@email.com", foundUser.get().getEmail());
        assertEquals("123.456.789-00", foundUser.get().getCpf());
    }
    
    @Test
    public void testSaveAndFindByEmail() {
        // Arrange
        User user = new User();
        user.setNome("Maria Santos");
        user.setEmail("maria@email.com");
        user.setCpf("987.654.321-00");
        user.setSenha("senha456");
        user.setTipo("motorista");
        
        // Act
        User savedUser = repository.save(user);
        Optional<User> foundUser = repository.findByEmail("maria@email.com");
        
        // Assert
        assertNotNull(savedUser);
        assertTrue(foundUser.isPresent());
        assertEquals("Maria Santos", foundUser.get().getNome());
        assertEquals("motorista", foundUser.get().getTipo());
    }
    
    @Test
    public void testFindAll() {
        // Arrange
        User user1 = new User();
        user1.setNome("Usuário 1");
        user1.setEmail("user1@email.com");
        user1.setCpf("111.111.111-11");
        user1.setSenha("senha1");
        user1.setTipo("passageiro");
        
        User user2 = new User();
        user2.setNome("Usuário 2");
        user2.setEmail("user2@email.com");
        user2.setCpf("222.222.222-22");
        user2.setSenha("senha2");
        user2.setTipo("motorista");
        
        // Act
        repository.save(user1);
        repository.save(user2);
        List<User> allUsers = repository.findAll();
        
        // Assert
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> "111.111.111-11".equals(u.getCpf())));
        assertTrue(allUsers.stream().anyMatch(u -> "222.222.222-22".equals(u.getCpf())));
    }
    
    @Test
    public void testUpdate() {
        // Arrange
        User user = new User();
        user.setNome("Nome Original");
        user.setEmail("original@email.com");
        user.setCpf("333.333.333-33");
        user.setSenha("senha123");
        user.setTipo("passageiro");
        
        repository.save(user);
        
        // Act
        user.setNome("Nome Atualizado");
        user.setEmail("atualizado@email.com");
        User updatedUser = repository.update(user);
        
        Optional<User> foundUser = repository.findByCpf("333.333.333-33");
        
        // Assert
        assertNotNull(updatedUser);
        assertTrue(foundUser.isPresent());
        assertEquals("Nome Atualizado", foundUser.get().getNome());
        assertEquals("atualizado@email.com", foundUser.get().getEmail());
    }
    
    @Test
    public void testDeleteByCpf() {
        // Arrange
        User user = new User();
        user.setNome("Usuário para Deletar");
        user.setEmail("deletar@email.com");
        user.setCpf("444.444.444-44");
        user.setSenha("senha123");
        user.setTipo("passageiro");
        
        repository.save(user);
        
        // Act
        boolean deleted = repository.deleteByCpf("444.444.444-44");
        Optional<User> foundUser = repository.findByCpf("444.444.444-44");
        
        // Assert
        assertTrue(deleted);
        assertFalse(foundUser.isPresent());
    }
    
    @Test
    public void testExistsByCpf() {
        // Arrange
        User user = new User();
        user.setNome("Usuário Teste");
        user.setEmail("teste@email.com");
        user.setCpf("555.555.555-55");
        user.setSenha("senha123");
        user.setTipo("passageiro");
        
        // Act & Assert
        assertFalse(repository.existsByCpf("555.555.555-55"));
        
        repository.save(user);
        
        assertTrue(repository.existsByCpf("555.555.555-55"));
    }
    
    @Test
    public void testExistsByEmail() {
        // Arrange
        User user = new User();
        user.setNome("Usuário Teste");
        user.setEmail("teste@email.com");
        user.setCpf("666.666.666-66");
        user.setSenha("senha123");
        user.setTipo("passageiro");
        
        // Act & Assert
        assertFalse(repository.existsByEmail("teste@email.com"));
        
        repository.save(user);
        
        assertTrue(repository.existsByEmail("teste@email.com"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSaveDuplicateCpf() {
        // Arrange
        User user1 = new User();
        user1.setNome("Usuário 1");
        user1.setEmail("user1@email.com");
        user1.setCpf("777.777.777-77");
        user1.setSenha("senha1");
        user1.setTipo("passageiro");
        
        User user2 = new User();
        user2.setNome("Usuário 2");
        user2.setEmail("user2@email.com");
        user2.setCpf("777.777.777-77"); // Mesmo CPF
        user2.setSenha("senha2");
        user2.setTipo("motorista");
        
        // Act
        repository.save(user1);
        repository.save(user2); // Deve lançar exceção
        
        // Assert - não deve chegar aqui
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSaveDuplicateEmail() {
        // Arrange
        User user1 = new User();
        user1.setNome("Usuário 1");
        user1.setEmail("mesmo@email.com");
        user1.setCpf("888.888.888-88");
        user1.setSenha("senha1");
        user1.setTipo("passageiro");
        
        User user2 = new User();
        user2.setNome("Usuário 2");
        user2.setEmail("mesmo@email.com"); // Mesmo email
        user2.setCpf("999.999.999-99");
        user2.setSenha("senha2");
        user2.setTipo("motorista");
        
        // Act
        repository.save(user1);
        repository.save(user2); // Deve lançar exceção
        
        // Assert - não deve chegar aqui
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNonExistentUser() {
        // Arrange
        User user = new User();
        user.setNome("Usuário Inexistente");
        user.setEmail("inexistente@email.com");
        user.setCpf("000.000.000-00");
        user.setSenha("senha123");
        user.setTipo("passageiro");
        
        // Act
        repository.update(user); // Deve lançar exceção
        
        // Assert - não deve chegar aqui
    }
}
