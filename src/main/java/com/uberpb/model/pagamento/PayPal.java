package com.uberpb.model.pagamento;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe simples para PayPal.
 * Como você mencionou incerteza sobre o PayPal, mantive simples.
 */
public class PayPal {
    private int id;
    private int usuarioId;
    private String email;
    private String nomeCompleto;
    private String descricao;
    private boolean ativo;

    // Construtor vazio
    public PayPal() {
        this.ativo = true;
    }

    // Construtor completo
    public PayPal(int usuarioId, String email, String nomeCompleto) {
        this();
        this.usuarioId = usuarioId;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.descricao = "Conta PayPal";
    }

    // Validação simples do PayPal
    @JsonIgnore
    public boolean isValido() {
        return email != null && isEmailValido(email) &&
                nomeCompleto != null && !nomeCompleto.trim().isEmpty() &&
                ativo;
    }

    // Validação básica de email
    private boolean isEmailValido(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Email mascarado para exibição
    @JsonIgnore
    public String getEmailMascarado() {
        if (email == null)
            return "Email inválido";

        String[] partes = email.split("@");
        if (partes.length == 2) {
            String usuario = partes[0];
            String dominio = partes[1];
            return usuario.charAt(0) + "***@" + dominio;
        }
        return email;
    }

    // toString para exibição
    @Override
    public String toString() {
        return String.format("PayPal\nTitular: %s\nEmail: %s\nDescrição: %s",
                nomeCompleto != null ? nomeCompleto : "N/A",
                getEmailMascarado(),
                descricao != null ? descricao : "Conta PayPal");
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}