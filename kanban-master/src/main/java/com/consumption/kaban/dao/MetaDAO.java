package com.consumption.kaban.dao;

import com.consumption.kaban.model.Meta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaDAO {
    private final Connection conn;

    public MetaDAO(Connection conn) {
        this.conn = conn;
    }

    // Salvar nova meta vinculada a um projeto
    public void salvar(Meta meta, int projetoId) throws SQLException {
        String sql = "INSERT INTO meta (descricao, concluida, prazo, projetoId, comPrazo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, meta.getDescricao());
        stmt.setBoolean(2, meta.isConcluida());

        trataMetaComPrazo(meta, stmt); // Define prazo (index 3) e comPrazo (index 5)

        stmt.setInt(4, projetoId);

        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            meta.setId(generatedKeys.getInt(1));
        }
    }

    public void trataMetaComPrazo(Meta meta, PreparedStatement stmt) throws SQLException {
        if (meta.getComPrazo() && meta.getPrazo() != null) {
            stmt.setDate(3, new java.sql.Date(meta.getPrazo().getTime()));
            stmt.setBoolean(5, true);
        } else {
            stmt.setNull(3, Types.DATE);
            stmt.setBoolean(5, false);
        }
    }

    // Buscar todas as metas de um projeto
    public List<Meta> buscarPorProjeto(int projetoId) throws SQLException {
        List<Meta> metas = new ArrayList<>();
        String sql = "SELECT * FROM meta WHERE projetoId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, projetoId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Meta meta = new Meta(
                    rs.getString("descricao"),
                    rs.getDate("prazo"),
                    rs.getBoolean("concluida")
            );
            meta.setId(rs.getInt("id"));
            meta.setProjetoId(projetoId);
            meta.setComPrazo(rs.getBoolean("comPrazo"));
            metas.add(meta);
        }

        return metas;
    }

    // Buscar uma meta por ID
    public Meta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM meta WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Meta meta = new Meta(
                    rs.getString("descricao"),
                    rs.getDate("prazo"),
                    rs.getBoolean("concluida")
            );
            meta.setId(rs.getInt("id"));
            meta.setProjetoId(rs.getInt("projetoId"));
            meta.setComPrazo(rs.getBoolean("comPrazo"));
            return meta;
        }

        return null;
    }

    // Atualizar uma meta
    public void atualizar(Meta meta) throws SQLException {
        String sql = "UPDATE meta SET descricao = ?, concluida = ?, prazo = ?, comPrazo = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, meta.getDescricao());
        stmt.setBoolean(2, meta.isConcluida());

        if (meta.getComPrazo() && meta.getPrazo() != null) {
            stmt.setDate(3, new java.sql.Date(meta.getPrazo().getTime()));
        } else {
            stmt.setNull(3, Types.DATE);
        }

        stmt.setBoolean(4, meta.getComPrazo());
        stmt.setInt(5, meta.getId());
        stmt.executeUpdate();
    }

    // Remover meta
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM meta WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
