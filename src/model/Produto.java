package com.estoque.model;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;
    private Categoria categoria; // Referência ao objeto Categoria

    // Construtor completo
    public Produto(int id, String nome, String descricao, BigDecimal preco, int quantidade, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    // Construtor para criar novo produto (ID será gerado pelo banco)
    public Produto(String nome, String descricao, BigDecimal preco, int quantidade, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "ID: " + id +
               ", Nome: " + nome +
               ", Descrição: " + descricao +
               ", Preço: R$" + String.format("%.2f", preco) +
               ", Quantidade: " + quantidade +
               ", Categoria: " + (categoria != null ? categoria.getNome() : "N/A");
    }
}