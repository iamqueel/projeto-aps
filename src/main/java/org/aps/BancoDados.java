package org.aps;

import java.sql.*;

public class BancoDados {

    private Connection conexao;

    public BancoDados() {
        conectar();
        criarTabela();
    }

    private void conectar() {
        try {
            conexao = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void criarTabela() {
        try {
            Statement stmt = conexao.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (id INT PRIMARY KEY AUTO_INCREMENT, nome_usuario VARCHAR(255), senha VARCHAR(255), data_ultima_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean autenticarUsuario(String nomeUsuario, String senha) {
        try (PreparedStatement ps = conexao.prepareStatement("SELECT * FROM usuarios WHERE nome_usuario = ? AND senha = ?")) {
            ps.setString(1, nomeUsuario);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void adicionarUsuario(String nomeUsuario, String senha) {
        try (PreparedStatement ps = conexao.prepareStatement("INSERT INTO usuarios (nome_usuario, senha) VALUES (?, ?)")) {
            ps.setString(1, nomeUsuario);
            ps.setString(2, senha);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarUsuario(int id, String nomeUsuario, String senha) {
        try (PreparedStatement ps = conexao.prepareStatement("UPDATE usuarios SET nome_usuario = ?, senha = ?, data_ultima_modificacao = CURRENT_TIMESTAMP WHERE id = ?")) {
            ps.setString(1, nomeUsuario);
            ps.setString(2, senha);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarUsuario(int id) {
        try (PreparedStatement ps = conexao.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet listarUsuarios() {
        try {
            Statement stmt = conexao.createStatement();
            return stmt.executeQuery("SELECT * FROM usuarios");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

