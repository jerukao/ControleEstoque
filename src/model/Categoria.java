package com.estoque.model;

public class Categoria {
    private int id;
    private String nome;

    // Construtor para buscar do banco
    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Construtor para criar nova categoria (ID será gerado pelo banco)
    public Categoria(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome;
    }
}