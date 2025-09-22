# Persistência de Sessão

## Como Funciona

O sistema de persistência de sessão foi implementado usando arquivos JSON para manter o usuário logado entre execuções do programa.

### Componentes

1. **SessionData.java** - Classe que representa os dados da sessão
   - userId: ID do usuário logado
   - email: Email do usuário
   - loginTime: Horário do login
   - lastActivity: Última atividade registrada

2. **SessionManager.java** - Gerenciador da sessão
   - `saveSession(User user)` - Salva a sessão atual
   - `getCurrentUser()` - Recupera o usuário atual
   - `isLoggedIn()` - Verifica se há usuário logado
   - `logout()` - Faz logout e limpa a sessão
   - `updateLastActivity()` - Atualiza a última atividade
   - `trackActivity(String action)` - Registra uma atividade

### Arquivo de Sessão

- **Localização**: `database/session/current_session.json`
- **Validade**: 24 horas desde a última atividade
- **Formato**:
```json
{
  "userId": 1,
  "email": "usuario@email.com",
  "loginTime": "2025-09-22T10:30:00",
  "lastActivity": "2025-09-22T15:45:30"
}
```

### Integração

O **MenuInicialCLI** foi modificado para:
1. Verificar sessão ativa ao iniciar
2. Salvar sessão no login
3. Atualizar atividade durante o uso
4. Limpar sessão no logout

### Vantagens

- ✅ Usuário não precisa fazer login a cada execução
- ✅ Sessão expira automaticamente (segurança)
- ✅ Registro de atividades do usuário
- ✅ Recuperação automática em caso de fechamento inesperado

### Como Usar

```java
// Verificar se há usuário logado
if (SessionManager.isLoggedIn()) {
    User user = SessionManager.getCurrentUser();
    // ... usar o usuário
}

// Registrar atividade
SessionManager.trackActivity("Cadastrou veículo");

// Fazer logout
SessionManager.logout();
```

### Exemplo de Fluxo

1. **Primeira execução**: Usuário faz login → sessão é salva
2. **Segunda execução**: Sistema detecta sessão → usuário entra automaticamente
3. **Após 24h**: Sessão expira → usuário precisa fazer login novamente