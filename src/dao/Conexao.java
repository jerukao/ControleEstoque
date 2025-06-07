package com.estoque.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Conexao {
    // Configure aqui os dados do seu banco MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/db_controleestoque"; // Altere para o nome do seu banco
    private static final String USUARIO = "root"; // Altere para seu usuário do MySQL
    private static final String SENHA = "admin"; // Altere para sua senha do MySQL

    // Bloco estático para carregar o driver JDBC ao iniciar a classe
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC do MySQL não encontrado!", e);
        }
    }

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados!", e);
        }
    }

    public static void fecharConexao(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public static void fecharConexao(Connection conn, Statement stmt) {
        fecharConexao(conn);
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o Statement: " + e.getMessage());
            }
        }
    }

    public static void fecharConexao(Connection conn, Statement stmt, ResultSet rs) {
        fecharConexao(conn, stmt);
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
            }
        }
    }
}