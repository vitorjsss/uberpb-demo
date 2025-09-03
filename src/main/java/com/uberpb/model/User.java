package com.uberpb.model;

public class User {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private String tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    // ID é gerenciado internamente pelo repositório
    // Não deve ser exposto publicamente por questões de segurança
    void setId(Long id) {
        this.id = id;
    }
}
