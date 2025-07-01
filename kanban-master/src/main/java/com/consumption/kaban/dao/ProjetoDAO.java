package com.consumption.kaban.dao;

import com.consumption.kaban.model.Projeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {
    private Connection conn;

    public ProjetoDAO(Connection conn) {
        this.conn = conn;
    }

    // Salva novo projeto e recupera o ID gerado
    public void salvar(Projeto projeto) throws SQLException {
        String sql = "INSERT INTO projeto (nome, descricao) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, projeto.getNome());
        stmt.setString(2, projeto.getDescricao());
        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            projeto.setId(generatedKeys.getInt(1));
        }
    }

    // Lista todos os projetos
    public List<Projeto> listarTodos() throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM projeto";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Projeto projeto = new Projeto(
                    rs.getString("nome"),
                    rs.getString("descricao")
            );
            projeto.setId(rs.getInt("id"));
            projetos.add(projeto);
        }

        return projetos;
    }

    // Buscar projeto pelo ID
    public Projeto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM projeto WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Projeto projeto = new Projeto(
                    rs.getString("nome"),
                    rs.getString("descricao")
            );
            projeto.setId(rs.getInt("id"));
            return projeto;
        }

        return null; // não encontrado
    }

    // Atualizar nome e descrição
    public void atualizar(Projeto projeto) throws SQLException {
        String sql = "UPDATE projeto SET nome = ?, descricao = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, projeto.getNome());
        stmt.setString(2, projeto.getDescricao());
        stmt.setInt(3, projeto.getId());
        stmt.executeUpdate();
    }

    // Remover projeto por ID
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM projeto WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
