# Sistema de Avaliações - UberPB

## Visão Geral

O sistema de avaliações foi implementado para armazenar tanto a **nota** quanto o **comentário** das avaliações feitas por passageiros e motoristas no banco de dados JSON, seguindo o mesmo padrão das outras entidades do projeto.

## Arquivos Criados/Modificados

### 📁 Novos Arquivos

1. **`src/main/java/com/uberpb/model/Avaliacao.java`**
   - Modelo da entidade Avaliacao com todos os campos necessários
   - Inclui validações e métodos auxiliares

2. **`src/main/java/com/uberpb/repository/AvaliacaoRepository.java`**
   - Repository para operações CRUD das avaliações
   - Métodos especializados para buscar por corrida, avaliador, etc.

3. **`database/avaliacoes/avaliacoes.json`**
   - Arquivo JSON para persistir as avaliações
   - Inicializado como array vazio

### 📝 Arquivos Modificados

1. **`src/main/java/com/uberpb/repository/BaseRepository.java`**
   - Adicionado suporte ao tipo `Avaliacao` no método `loadAll()`

2. **`src/main/java/com/uberpb/repository/DatabaseManager.java`**
   - Adicionado `AvaliacaoRepository` como dependência
   - Criados métodos públicos para acessar funcionalidades das avaliações
   - Atualizado método `getDatabaseStats()` para incluir contagem de avaliações

3. **`src/main/java/com/uberpb/cli/forms/AvaliarMotoristaCLI.java`**
   - Adicionado campo para comentário opcional
   - Implementado salvamento da avaliação completa no banco
   - Verificação de avaliação duplicada

4. **`src/main/java/com/uberpb/cli/forms/AvaliarPassageiroCLI.java`**
   - Adicionado campo para comentário opcional
   - Implementado salvamento da avaliação completa no banco
   - Verificação de avaliação duplicada

## Estrutura da Entidade Avaliacao

```java
public class Avaliacao {
    private int id;                // ID único da avaliação
    private int corridaId;         // ID da corrida avaliada
    private int avaliadorId;       // ID de quem fez a avaliação
    private int avaliadoId;        // ID de quem foi avaliado
    private String tipoAvaliador;  // "PASSAGEIRO" ou "MOTORISTA"
    private String tipoAvaliado;   // "MOTORISTA" ou "PASSAGEIRO"
    private float nota;            // Nota de 1 a 5
    private String comentario;     // Comentário opcional
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
```

## Funcionalidades Implementadas

### 🔍 Busca e Consulta
- `findById(int id)` - Buscar avaliação por ID
- `findByCorridaId(int corridaId)` - Buscar avaliações de uma corrida
- `findByAvaliadorId(int avaliadorId)` - Buscar avaliações feitas por um usuário
- `findByAvaliadoId(int avaliadoId)` - Buscar avaliações recebidas por um usuário
- `findAvaliacoesDeMotoristas()` - Buscar todas as avaliações de motoristas
- `findAvaliacoesDePassageiros()` - Buscar todas as avaliações de passageiros

### 📊 Estatísticas
- `calcularMediaAvaliacoes(int avaliadoId)` - Calcular média de um usuário
- `contarAvaliacoes(int avaliadoId)` - Contar total de avaliações de um usuário
- `corridaJaAvaliadaPor(...)` - Verificar se corrida já foi avaliada

### ✅ Validações
- Verificação de nota entre 1 e 5
- Prevenção de avaliações duplicadas
- Validação de campos obrigatórios

## Como Usar

### 1. Avaliar um Motorista
Quando um passageiro avalia um motorista:
```java
// A CLI solicita nota (1-5) e comentário opcional
// Salva no banco de dados automaticamente
// Mantém compatibilidade com sistema anterior
```

### 2. Avaliar um Passageiro
Quando um motorista avalia um passageiro:
```java
// A CLI solicita nota (1-5) e comentário opcional
// Salva no banco de dados automaticamente
// Mantém compatibilidade com sistema anterior
```

### 3. Consultar Avaliações
```java
DatabaseManager db = new DatabaseManager();

// Buscar todas as avaliações de uma corrida
List<Avaliacao> avaliacoes = db.findAvaliacoesByCorridaId(1);

// Calcular média de um motorista
double media = db.calcularMediaAvaliacoes(motoristaId);

// Verificar se corrida já foi avaliada
boolean jaAvaliou = db.corridaJaAvaliadaPor(corridaId, passageiroId, "PASSAGEIRO");
```

## Exemplo de Dados Salvos

```json
[
  {
    "id": 1,
    "corridaId": 1,
    "avaliadorId": 1,
    "avaliadoId": 2,
    "tipoAvaliador": "PASSAGEIRO",
    "tipoAvaliado": "MOTORISTA",
    "nota": 4.5,
    "comentario": "Motorista muito educado e dirigiu com segurança!",
    "criadoEm": "2025-10-16T10:30:15.123456",
    "atualizadoEm": "2025-10-16T10:30:15.123456"
  }
]
```

## Compatibilidade

✅ **Mantida compatibilidade total** com o sistema anterior:
- Continua salvando notas nos objetos `Motorista` e `Passageiro`
- Continua atualizando médias automaticamente  
- Não quebra funcionalidades existentes

✨ **Novo**: Agora também salva **nota + comentário** detalhados no banco!

## Benefícios

1. **Histórico Completo**: Todas as avaliações ficam registradas com detalhes
2. **Comentários**: Possibilidade de feedback textual além da nota
3. **Rastreabilidade**: Saber quem avaliou quem e quando
4. **Análises**: Possibilidade de relatórios e análises detalhadas
5. **Auditoria**: Prevenção de avaliações duplicadas
6. **Consistência**: Mesmo padrão de outras entidades (corridas, pagamentos, etc.)