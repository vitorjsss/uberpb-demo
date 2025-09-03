# UberPB Demo - Repositório de Usuários

Este projeto demonstra a implementação de um repositório de usuários com persistência local em JSON.

## Estrutura do Projeto

```
src/
├── main/java/com/uberpb/
│   ├── model/
│   │   └── User.java                    # Modelo de usuário
│   ├── repository/
│   │   ├── UsuarioRepository.java       # Interface do repositório
│   │   └── UsuarioRepositoryJSON.java   # Implementação com persistência JSON
│   └── App.java                         # Classe principal com demonstração
└── test/java/com/uberpb/
    └── repository/
        └── UsuarioRepositoryJSONTest.java # Testes do repositório
```

## Funcionalidades

O `UsuarioRepositoryJSON` oferece as seguintes operações:

- **save(User user)**: Salva um novo usuário
- **findByCpf(String cpf)**: Busca usuário por CPF
- **findByEmail(String email)**: Busca usuário por email
- **findAll()**: Lista todos os usuários
- **update(User user)**: Atualiza um usuário existente
- **deleteByCpf(String cpf)**: Remove um usuário pelo CPF
- **existsByCpf(String cpf)**: Verifica se um CPF já existe
- **existsByEmail(String email)**: Verifica se um email já existe

## Características

- **Persistência Local**: Dados armazenados em estrutura de diretórios organizada
- **IDs Únicos**: Geração automática de IDs sequenciais para cada usuário (não expostos publicamente)
- **Organização por Tipo**: Usuários separados em pastas por tipo (passageiro/motorista)
- **Thread-Safe**: Utiliza locks de leitura/escrita para operações concorrentes
- **Validação**: Impede duplicação de CPF e email
- **Tratamento de Erros**: Exceções apropriadas para operações inválidas
- **Inicialização Automática**: Cria estrutura de diretórios automaticamente
- **Segurança**: IDs internos não são expostos através de getters públicos

## Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6 ou superior

### Compilar e Executar
```bash
# Compilar o projeto
mvn compile

# Executar a demonstração
mvn exec:java

# Executar os testes
mvn test
```

### Executar com JAR
```bash
# Criar JAR executável
mvn package

# Executar o JAR
java -jar target/demo-1.0.jar
```

## Estrutura de Dados

Os dados são persistidos em uma estrutura de diretórios que simula um banco de dados:

```
database/
├── id_counter.txt           # Contador de IDs únicos
└── users/
    ├── passageiros/         # Usuários do tipo "passageiro"
    │   ├── user_1.json
    │   ├── user_3.json
    │   └── ...
    └── motoristas/          # Usuários do tipo "motorista"
        ├── user_2.json
        ├── user_4.json
        └── ...
```

Cada usuário é salvo em um arquivo JSON separado com nome baseado no ID único.

## Exemplo de Uso

```java
// Criar repositório
UsuarioRepository repository = new UsuarioRepositoryJSON();

// Criar usuário
User user = new User();
user.setNome("João Silva");
user.setEmail("joao@email.com");
user.setCpf("123.456.789-00");
user.setSenha("senha123");
user.setTipo("passageiro");

// Salvar usuário (ID será gerado automaticamente internamente)
User savedUser = repository.save(user);
System.out.println("Usuário salvo com sucesso: " + savedUser.getNome());

// Buscar por CPF
Optional<User> found = repository.findByCpf("123.456.789-00");
if (found.isPresent()) {
    User foundUser = found.get();
    System.out.println("Usuário encontrado: " + foundUser.getNome() + " (" + foundUser.getTipo() + ")");
}
```

## Testes

O projeto inclui testes abrangentes que cobrem:
- Operações CRUD básicas
- Validação de duplicatas
- Tratamento de erros
- Operações de busca
- Verificação de existência

Execute os testes com:
```bash
mvn test
```

## Dependências

- **Jackson**: Para serialização/deserialização JSON
- **JUnit**: Para testes unitários
- **Java NIO**: Para operações de arquivo

## Segurança

- **IDs Internos**: Os IDs únicos são gerenciados internamente pelo repositório e não são expostos através de getters públicos
- **Validação de Dados**: Implementa validação para evitar duplicação de CPF e email
- **Thread-Safety**: Operações concorrentes são protegidas por locks de leitura/escrita
- **Isolamento de Dados**: Cada usuário é armazenado em arquivo separado, limitando o acesso a dados de outros usuários

## Licença

Este projeto é apenas para fins educacionais e de demonstração.
