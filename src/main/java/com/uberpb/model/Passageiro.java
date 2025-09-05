package com.uberpb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Passageiro {
    private static int contadorId = 1;

    private final int id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private double avaliacaoMedia;
    private List<String> historicoCorridas;

    public Passageiro(String nome, String email, String telefone, String senha) {
        this.id = contadorId++;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.avaliacaoMedia = 0.0;
        this.historicoCorridas = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public double getAvaliacaoMedia() { return avaliacaoMedia; }
    public void setAvaliacaoMedia(double avaliacaoMedia) { this.avaliacaoMedia = avaliacaoMedia; }

    public List<String> getHistoricoCorridas() { return historicoCorridas; }
    public void adicionarCorrida(String corrida) {
        this.historicoCorridas.add(corrida);
    }

    @Override
    public String toString() {
        return "Passageiro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", avaliacaoMedia=" + avaliacaoMedia +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passageiro)) return false;
        Passageiro that = (Passageiro) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
