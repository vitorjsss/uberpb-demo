package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PassageiroService {

    private List<Passageiro> passageiros = new ArrayList<>();
    
    // Padrão para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    // Cadastrar novo passageiro
    public void cadastrar(Passageiro p) throws Exception {
        validarCamposObrigatorios(p);
        validarFormatoEmail(p.getEmail());
        
        if (buscarPorEmail(p.getEmail()) != null) {
            throw new Exception("Já existe um passageiro com este e-mail!");
        }
        
        passageiros.add(p);
    }

    // Buscar passageiro por e-mail
    public Passageiro buscarPorEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("E-mail não pode ser nulo ou vazio!");
        }
        
        validarFormatoEmail(email);
        
        return passageiros.stream()
                .filter(p -> p.getEmail() != null && p.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElse(null);
    }

    // Buscar passageiro por ID
    public Passageiro buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID deve ser um valor positivo!");
        }
        
        return passageiros.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Atualizar dados do passageiro
    public void atualizar(Passageiro passageiroAtualizado) throws Exception {
        if (passageiroAtualizado == null) {
            throw new Exception("Passageiro não pode ser nulo!");
        }
        
        Passageiro existente = buscarPorId(passageiroAtualizado.getId());
        if (existente == null) {
            throw new Exception("Passageiro não encontrado!");
        }

        // Validar campos opcionais se não forem nulos
        if (passageiroAtualizado.getMetodosPagamento() != null && 
            passageiroAtualizado.getMetodosPagamento().isEmpty()) {
            throw new Exception("Lista de métodos de pagamento não pode ser vazia se fornecida!");
        }
        
        // Validar avaliação média se fornecida
        if (passageiroAtualizado.getAvaliacaoMedia() < 0 || passageiroAtualizado.getAvaliacaoMedia() > 5) {
            throw new Exception("Avaliação média deve estar entre 0 e 5!");
        }

        // Atualiza apenas campos editáveis
        existente.setLocalizacaoAtual(passageiroAtualizado.getLocalizacaoAtual());
        existente.setEmCorrida(passageiroAtualizado.isEmCorrida());
        
        if (passageiroAtualizado.getMetodosPagamento() != null) {
            existente.setMetodosPagamento(passageiroAtualizado.getMetodosPagamento());
        }
        
        existente.setAvaliacaoMedia(passageiroAtualizado.getAvaliacaoMedia());
    }

    // Remover passageiro
    public void remover(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID deve ser um valor positivo!");
        }
        
        Passageiro passageiro = buscarPorId(id);
        if (passageiro == null) {
            throw new Exception("Passageiro não encontrado!");
        }
        passageiros.remove(passageiro);
    }

    // Listar todos os passageiros
    public List<Passageiro> listar() {
        return new ArrayList<>(passageiros);
    }
    
    // Métodos privados de validação
    
    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     */
    private void validarCamposObrigatorios(Passageiro p) throws Exception {
        if (p == null) {
            throw new Exception("Passageiro não pode ser nulo!");
        }
        
        if (p.getNome() == null || p.getNome().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório!");
        }
        
        if (p.getEmail() == null || p.getEmail().trim().isEmpty()) {
            throw new Exception("E-mail é obrigatório!");
        }
        
        if (p.getTelefone() == null || p.getTelefone().trim().isEmpty()) {
            throw new Exception("Telefone é obrigatório!");
        }
        
        // Validar idade se o campo existir (assumindo que idade é int, não Integer)
        if (p.getIdade() < 0) {
            throw new Exception("Idade não pode ser negativa!");
        }
        
        if (p.getIdade() > 120) {
            throw new Exception("Idade não pode ser maior que 120 anos!");
        }
    }
    
    /**
     * Valida se o formato do e-mail está correto
     */
    private void validarFormatoEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("E-mail não pode ser nulo ou vazio!");
        }
        
        String emailLimpo = email.trim();
        if (!EMAIL_PATTERN.matcher(emailLimpo).matches()) {
            throw new Exception("Formato de e-mail inválido!");
        }
    }
}