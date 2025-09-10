# Sistema de Persistência - UberPB

O sistema agora possui **persistência completa** de dados usando arquivos JSON organizados. Todos os dados são salvos automaticamente e persistem entre execuções do programa.

## 🎯 O que foi implementado

### ✅ **Persistência Completa**
- **Cadastro de usuário** → Salvo em `database/users/users.json`
- **Login** → Carrega dados de `database/users/users.json`
- **Cadastro de passageiro** → Salvo em `database/users/users.json` + `database/passageiros/passageiros.json`
- **Cadastro de motorista** → Salvo em `database/users/users.json` + `database/motoristas/motoristas.json`
- **Atualizações** → Persistem automaticamente nos arquivos JSON

### ✅ **Funcionalidades dos Menus**
- **Menu Passageiro**: Cadastrar métodos de pagamento, ver informações, atualizar localização
- **Menu Motorista**: Ver status, avaliações, CNH, atualizar localização
- **Estatísticas**: Ver contadores de usuários, passageiros, motoristas

## 🚀 Como usar

### 1. **Executar o Sistema**
```bash
# Compilar (se necessário)
javac -cp "lib/*" src/main/java/com/uberpb/cli/MainCLI.java

# Executar
java -cp "src/main/java:lib/*" com.uberpb.cli.MainCLI
```

### 2. **Fluxo de Uso**

#### **Cadastro e Login**
1. Escolha "1 - Cadastrar novo usuario"
2. Preencha os dados (validações automáticas)
3. Usuário é salvo em `database/users/users.json`
4. Faça login com email e senha
5. Dados são carregados do arquivo JSON

#### **Cadastro de Perfil**
1. Após login, escolha "1 - Cadastrar perfil de Passageiro" ou "2 - Cadastrar perfil de Motorista"
2. Preencha os dados específicos
3. Dados são salvos em múltiplos arquivos JSON

#### **Usar Funcionalidades**
1. Acesse "3 - Menu Passageiro" ou "4 - Menu Motorista"
2. Todas as operações persistem automaticamente
3. Use "5 - Ver estatísticas do banco de dados" para ver contadores

## 📁 Estrutura dos Arquivos

```
database/
├── users/
│   └── users.json          # Dados básicos de TODOS os usuários
├── passageiros/
│   └── passageiros.json    # Dados específicos dos passageiros
├── motoristas/
│   └── motoristas.json     # Dados específicos dos motoristas
├── veiculos/
│   └── veiculos.json       # Dados dos veículos
└── id_counter.json         # Contadores de ID para cada entidade
```

## 🔄 Exemplo de Fluxo Completo

### **1. Cadastrar Usuário**
```
=== Bem-vindo ao UberPB ===
1 - Cadastrar novo usuario
2 - Login
0 - Sair
Escolha: 1

--- Cadastro de Usuario ---
Username: joao123
✓ Username válido!
Senha: MinhaSenh@123
✓ Senha válida!
Nome: João
✓ Nome válido!
Sobrenome: Silva
✓ Sobrenome válido!
Email: joao@email.com
✓ Email válido!
Telefone: (11) 99999-9999
✓ Telefone válido!

🎉 Usuario cadastrado com sucesso!
ID do usuário: 1
Dados salvos em: database/users/users.json
```

### **2. Login**
```
=== Bem-vindo ao UberPB ===
1 - Cadastrar novo usuario
2 - Login
0 - Sair
Escolha: 2

--- Login ---
Email: joao@email.com
Senha: MinhaSenh@123
Login bem-sucedido! Bem-vindo, João
Dados carregados de: database/users/users.json
```

### **3. Cadastrar Perfil de Passageiro**
```
=== Menu Principal ===
Usuario logado: João (joao@email.com)
1 - Cadastrar perfil de Passageiro
2 - Cadastrar perfil de Motorista
3 - Menu Passageiro
4 - Menu Motorista
5 - Ver estatísticas do banco de dados
9 - Logout
Escolha: 1

--- Cadastro de Perfil Passageiro ---
Idade: 25
✓ Idade válida!

🎉 Perfil de passageiro cadastrado com sucesso!
ID do passageiro: 1
Dados salvos em: database/users/users.json
Dados salvos em: database/passageiros/passageiros.json
```

### **4. Usar Menu Passageiro**
```
=== Menu Passageiro ===
1 - Cadastrar metodo de pagamento
2 - Realizar corrida
3 - Ver avaliacao media
4 - Ver historico de corridas
5 - Ver localizacao atual
6 - Ver status (em corrida ou nao)
7 - Atualizar localizacao
8 - Ver informacoes do perfil
9 - Voltar
Escolha: 1

--- Cadastrar Método de Pagamento ---
Digite o método de pagamento: Cartão de Crédito
✓ Método de pagamento cadastrado com sucesso!
Dados atualizados em: database/passageiros/passageiros.json
```

## 📊 Verificar Dados Persistidos

### **Arquivo users.json**
```json
[
  {
    "id": 1,
    "nome": "João",
    "sobrenome": "Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "tipo": "usuario",
    "dataCadastro": "2024-01-15T10:30:00"
  }
]
```

### **Arquivo passageiros.json**
```json
[
  {
    "id": 1,
    "nome": "João",
    "sobrenome": "Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "tipo": "passageiro",
    "dataCadastro": "2024-01-15T10:30:00",
    "avaliacaoMedia": 0.0,
    "historicoCorridas": [],
    "localizacaoAtual": "Nao definida",
    "emCorrida": false,
    "metodosPagamento": ["Cartão de Crédito"],
    "idade": 25
  }
]
```

## 🧪 Testar o Sistema

Execute o exemplo de teste:
```bash
java -cp "src/main/java:lib/*" com.uberpb.examples.TestePersistencia
```

Este exemplo:
1. Cria um usuário, passageiro e motorista
2. Verifica se os dados foram persistidos
3. Testa atualizações
4. Mostra estatísticas do banco

## ✨ Vantagens da Persistência

1. **Dados Persistem**: Não perde dados ao fechar o programa
2. **Organização**: Cada tipo de entidade tem seu arquivo
3. **Consistência**: Dados básicos sempre em `users.json`
4. **Transparência**: Mostra onde os dados são salvos
5. **Flexibilidade**: Fácil de adicionar novos tipos de entidade
6. **Backup**: Arquivos JSON podem ser facilmente copiados/restaurados

## 🔧 Manutenção

- **Limpar dados**: Delete os arquivos JSON na pasta `database/`
- **Backup**: Copie a pasta `database/` inteira
- **Restaurar**: Cole a pasta `database/` de volta
- **Verificar integridade**: Use a opção "5 - Ver estatísticas do banco de dados"

O sistema agora está **100% funcional** com persistência completa! 🎉
