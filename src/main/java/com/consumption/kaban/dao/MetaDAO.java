package com.consumption.kaban.dao;

import com.consumption.kaban.model.Meta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaDAO {
    private Connection conn;

    public MetaDAO(Connection conn) {
        this.conn = conn;
    }

    // Salva uma nova meta associada a um projeto (com ou sem prazo)
    public void salvar(Meta meta, int projetoId) throws SQLException {
        if (meta.getComPrazo()) {
            String sql = "INSERT INTO meta (descricao, concluida, prazo, projetoId) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, meta.getDescricao());
            stmt.setBoolean(2, meta.isConcluida());
            stmt.setDate(3, new java.sql.Date(meta.getPrazo().getTime()));
            stmt.setInt(4, projetoId);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                meta.setId(generatedKeys.getInt(1));
            }

        } else {
            String sql = "INSERT INTO meta (descricao, concluida, projetoId) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, meta.getDescricao());
            stmt.setBoolean(2, meta.isConcluida());
            stmt.setInt(3, projetoId);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                meta.setId(generatedKeys.getInt(1));
            }
        }
    }

    // Lista todas as metas de um projeto
    public List<Meta> buscarPorProjeto(int projetoId) throws SQLException {
        List<Meta> metas = new ArrayList<>();
        String sql = "SELECT * FROM meta WHERE projetoId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, projetoId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Meta meta = new Meta(rs.getString("descricao"), rs.getDate("prazo"));
            meta.setId(rs.getInt("id"));
            meta.setConcluida(rs.getBoolean("concluida"));
            metas.add(meta);
        }

        return metas;
    }

    // Buscar uma meta pelo ID
    public Meta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM meta WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Meta meta = new Meta(rs.getString("descricao"), rs.getDate("prazo"));
            meta.setId(rs.getInt("id"));
            meta.setConcluida(rs.getBoolean("concluida"));
            return meta;
        }

        return null;
    }

    // Atualizar uma meta (com ou sem prazo)
    public void atualizar(Meta meta) throws SQLException {
        String sql = "UPDATE meta SET descricao = ?, concluida = ?, prazo = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, meta.getDescricao());
        stmt.setBoolean(2, meta.isConcluida());
        stmt.setDate(3, meta.getPrazo() != null ? new java.sql.Date(meta.getPrazo().getTime()) : null);
        stmt.setInt(4, meta.getId());
        stmt.executeUpdate();
    }

    // Remover uma meta
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM meta WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
