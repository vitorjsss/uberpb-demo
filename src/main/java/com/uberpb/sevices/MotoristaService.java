package com.uberpb.sevices;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.uberpb.model.Motorista;

public class MotoristaService {

    private List<Motorista> motoristas = new ArrayList<>();

    // Padrão para validação de email - verifica formato com @ e domínio válido
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Cadastrar novo motorista
    public void cadastrar(Motorista m) throws Exception {
        validarCamposObrigatorios(m);
        validarFormatoEmail(m.getEmail());

        if (buscarPorEmail(m.getEmail()) != null) {
            throw new Exception("Já existe um motorista com este e-mail!");
        }

        motoristas.add(m);
    }

    // Buscar motorista por e-mail
    public Motorista buscarPorEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("E-mail não pode ser nulo ou vazio!");
        }

        validarFormatoEmail(email);

        return motoristas.stream()
                .filter(m -> m.getEmail() != null && m.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElse(null);
    }

    // Buscar motorista por ID
    public Motorista buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID deve ser um valor positivo!");
        }

        return motoristas.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Atualizar dados do motorista
    public void atualizar(Motorista motoristaAtualizado) throws Exception {
        if (motoristaAtualizado == null) {
            throw new Exception("Motorista não pode ser nulo!");
        }

        Motorista existente = buscarPorId(motoristaAtualizado.getId());
        if (existente == null) {
            throw new Exception("Motorista não encontrado!");
        }

        // Valida campos que podem ser atualizados se não forem nulos
        if (motoristaAtualizado.getCnh() != null && motoristaAtualizado.getCnh().trim().isEmpty()) {
            throw new Exception("CNH não pode ser vazia!");
        }

        // Atualiza apenas campos editáveis
        existente.setAtivo(motoristaAtualizado.isAtivo());
        existente.setDisponivel(motoristaAtualizado.isDisponivel());
        existente.setLocalizacaoAtual(motoristaAtualizado.getLocalizacaoAtual());
        existente.setAvaliacaoMedia(motoristaAtualizado.getAvaliacaoMedia());
        existente.setTotalAvaliacoes(motoristaAtualizado.getTotalAvaliacoes());

        if (motoristaAtualizado.getCnh() != null) {
            existente.setCnh(motoristaAtualizado.getCnh());
        }
        if (motoristaAtualizado.getValidadeCnh() != null) {
            existente.setValidadeCnh(motoristaAtualizado.getValidadeCnh());
        }
    }

    // Remover motorista
    public void remover(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID deve ser um valor positivo!");
        }

        Motorista motorista = buscarPorId(id);
        if (motorista == null) {
            throw new Exception("Motorista não encontrado!");
        }
        motoristas.remove(motorista);
    }

    // Listar todos os motoristas
    public List<Motorista> listar() {
        return new ArrayList<>(motoristas);
    }

    // Métodos privados de validação

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     */
    private void validarCamposObrigatorios(Motorista m) throws Exception {
        if (m == null) {
            throw new Exception("Motorista não pode ser nulo!");
        }

        if (m.getNome() == null || m.getNome().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório!");
        }

        if (m.getEmail() == null || m.getEmail().trim().isEmpty()) {
            throw new Exception("E-mail é obrigatório!");
        }

        if (m.getTelefone() == null || m.getTelefone().trim().isEmpty()) {
            throw new Exception("Telefone é obrigatório!");
        }

        // CPF validation removed - field not available in Motorista class

        if (m.getCnh() == null || m.getCnh().trim().isEmpty()) {
            throw new Exception("CNH é obrigatória!");
        }

        if (m.getValidadeCnh() == null) {
            throw new Exception("Data de validade da CNH é obrigatória!");
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