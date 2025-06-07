package com.estoque.dao;

import com.estoque.model.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public Categoria adicionar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nome_categoria) VALUES (?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, categoria.getNome());
            pstmt.executeUpdate();

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                categoria.setId(generatedKeys.getInt(1));
            }
            System.out.println("Categoria '" + categoria.getNome() + "' adicionada com sucesso!");
            return categoria;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar categoria: " + e.getMessage());
            if (e.getSQLState().equals("23000")) { // Código de erro para violação de constraint UNIQUE
                 System.err.println("Detalhe: Já existe uma categoria com o nome '" + categoria.getNome() + "'.");
            }
            return null;
        } finally {
            Conexao.fecharConexao(conn, pstmt, generatedKeys);
        }
    }

    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nome_categoria FROM categorias ORDER BY nome_categoria";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn, pstmt, rs);
        }
        return categorias;
    }

    public Categoria buscarPorId(int id) {
        String sql = "SELECT id_categoria, nome_categoria FROM categorias WHERE id_categoria = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria por ID: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn, pstmt, rs);
        }
        return null;
    }
    
    public Categoria buscarPorNome(String nome) {
        String sql = "SELECT id_categoria, nome_categoria FROM categorias WHERE nome_categoria = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria por nome: " + e.getMessage());
        } finally {
            Conexao.fecharConexao(conn, pstmt, rs);
        }
        return null;
    }

    public boolean atualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nome_categoria = ? WHERE id_categoria = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, categoria.getNome());
            pstmt.setInt(2, categoria.getId());
            int afetados = pstmt.executeUpdate();
            if (afetados > 0) {
                 System.out.println("Categoria '" + categoria.getNome() + "' atualizada com sucesso!");
                 return true;
            } else {
                System.out.println("Nenhuma categoria encontrada com o ID " + categoria.getId() + " para atualizar.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
            if (e.getSQLState().equals("23000")) {
                 System.err.println("Detalhe: Já existe outra categoria com o nome '" + categoria.getNome() + "'.");
            }
            return false;
        } finally {
            Conexao.fecharConexao(conn, pstmt);
        }
    }

    public boolean excluir(int id) {
        // Antes de excluir, verificar se existem produtos associados a esta categoria
        ProdutoDAO produtoDAO = new ProdutoDAO(); // Cuidado com dependência cíclica se ProdutoDAO usar CategoriaDAO no construtor
        if (!produtoDAO.listarPorCategoriaId(id).isEmpty()) {
            System.err.println("Erro ao excluir categoria: Existem produtos associados a esta categoria (ID: " + id + ").");
            System.err.println("Exclua ou reatribua os produtos primeiro.");
            return false;
        }

        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int afetados = pstmt.executeUpdate();
             if (afetados > 0) {
                 System.out.println("Categoria com ID " + id + " excluída com sucesso!");
                 return true;
            } else {
                System.out.println("Nenhuma categoria encontrada com o ID " + id + " para excluir.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        } finally {
            Conexao.fecharConexao(conn, pstmt);
        }
    }
}