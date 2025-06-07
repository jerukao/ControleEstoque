package com.estoque.dao;

import com.estoque.model.Produto;
import com.estoque.model.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProdutoDAO {
    private CategoriaDAO categoriaDAO; // Para buscar informações da categoria

    public ProdutoDAO() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public Produto adicionar(Produto produto) {
        String sql = "INSERT INTO produtos (nome_produto, descricao_produto, preco_produto, quantidade_produto, id_categoria) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setInt(5, produto.getCategoria().getId());
            pstmt.executeUpdate();

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                produto.setId(generatedKeys.getInt(1));
            }
            System.out.println("Produto '" + produto.getNome() + "' adicionado com sucesso!");
            return produto;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
            return null;
        } finally {
            Conexao.fecharConexao(conn, pstmt, generatedKeys);
        }
    }

    public List<Produto> listar() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id_produto, p.nome_produto, p.descricao_produto, p.preco_produto, p.quantidade_produto, " +
                     "c.id_categoria, c.nome_categoria " +
                     "FROM produtos p " +
                     "JOIN categorias c ON p.id_categoria = c.id_categoria " +
                     "ORDER BY p.nome_produto";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"));
                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome_produto"),
                        rs.getString("descricao_produto"),
                        rs.getBigDecimal("preco_produto"),
                        rs.getInt("quantidade_produto"),
                        categoria
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn, pstmt, rs);
        }
        return produtos;
    }
    
    public List<Produto> listarPorCategoriaId(int idCategoria) {
        List<Produto> produtos = new ArrayList<>();
         String sql = "SELECT p.id_produto, p.nome_produto, p.descricao_produto, p.preco_produto, p.quantidade_produto, " +
                     "c.id_categoria, c.nome_categoria " +
                     "FROM produtos p " +
                     "JOIN categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.id_categoria = ? " +
                     "ORDER BY p.nome_produto";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idCategoria);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"));
                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome_produto"),
                        rs.getString("descricao_produto"),
                        rs.getBigDecimal("preco_produto"),
                        rs.getInt("quantidade_produto"),
                        categoria
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos por categoria: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn, pstmt, rs);
        }
        return produtos;
    }


    public Produto buscarPorId(int id) {
        String sql = "SELECT p.id_produto, p.nome_produto, p.descricao_produto, p.preco_produto, p.quantidade_produto, " +
                     "c.id_categoria, c.nome_categoria " +
                     "FROM produtos p " +
                     "JOIN categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.id_produto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"));
                return new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome_produto"),
                        rs.getString("descricao_produto"),
                        rs.getBigDecimal("preco_produto"),
                        rs.getInt("quantidade_produto"),
                        categoria
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn, pstmt, rs);
        }
        return null;
    }

    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome_produto = ?, descricao_produto = ?, preco_produto = ?, quantidade_produto = ?, id_categoria = ? " +
                     "WHERE id_produto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setInt(5, produto.getCategoria().getId());
            pstmt.setInt(6, produto.getId());
            int afetados = pstmt.executeUpdate();
            if (afetados > 0) {
                 System.out.println("Produto '" + produto.getNome() + "' atualizado com sucesso!");
                 return true;
            } else {
                System.out.println("Nenhum produto encontrado com o ID " + produto.getId() + " para atualizar.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        } finally {
            Conexao.fecharConexao(conn, pstmt);
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int afetados = pstmt.executeUpdate();
            if (afetados > 0) {
                 System.out.println("Produto com ID " + id + " excluído com sucesso!");
                 return true;
            } else {
                System.out.println("Nenhum produto encontrado com o ID " + id + " para excluir.");
                return false;
            }
        } catch (SQLException e) {
            // Tratar erro de chave estrangeira se houver, embora neste caso, produtos não restrinjam outras tabelas
            System.err.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        } finally {
            Conexao.fecharConexao(conn, pstmt);
        }
    }
}