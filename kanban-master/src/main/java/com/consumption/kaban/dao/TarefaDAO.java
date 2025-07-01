package com.consumption.kaban.dao;

import com.consumption.kaban.enums.TarefaStatusEnum;
import com.consumption.kaban.model.Tarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private Connection conn;

    public TarefaDAO(Connection conn) {
        this.conn = conn;
    }

    // Salvar nova tarefa vinculada a um projeto
    public void salvar(Tarefa tarefa, int projetoId) throws SQLException {
        String sql = "INSERT INTO tarefa (titulo, descricao, status, prazo, projetoId, comPrazo) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
    
        stmt.setString(1, tarefa.getTitulo());
        stmt.setString(2, tarefa.getDescricao());
        stmt.setString(3, tarefa.getStatus().name());
    
        trataTarefaComPrazo(tarefa, stmt);
    
        stmt.setInt(5, projetoId);
    
        stmt.executeUpdate();
    }

    public void trataTarefaComPrazo(Tarefa tarefa, PreparedStatement stmt) throws SQLException {
        if (tarefa.getComPrazo() && tarefa.getPrazo() != null) {
            stmt.setDate(4, new java.sql.Date(tarefa.getPrazo().getTime()));
            stmt.setBoolean(6, true);
        } else {
            stmt.setNull(4, Types.DATE);
            stmt.setBoolean(6, false);
        }
    }
    
    // Listar tarefas de um projeto
    public List<Tarefa> buscarPorProjeto(int projetoId) throws SQLException {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE projetoId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, projetoId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Tarefa tarefa = new Tarefa(
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    TarefaStatusEnum.valueOf(rs.getString("status")),
                    rs.getDate("prazo") // pode ser null aqui
            );
            tarefa.setId(rs.getInt("id"));
            tarefa.setProjetoId(projetoId);
            tarefa.setComPrazo(rs.getBoolean("comPrazo"));
            tarefas.add(tarefa);
        }

        return tarefas;
    }

    // Atualizar estado e descrição da tarefa
    public void atualizar(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE tarefa SET titulo = ?, descricao = ?, status = ?, prazo = ?, comPrazo = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, tarefa.getTitulo());
        stmt.setString(2, tarefa.getDescricao());
        stmt.setString(3, tarefa.getStatus().name());

        if (tarefa.getComPrazo()) {
            stmt.setDate(4, tarefa.getPrazo());
        } else {
            stmt.setNull(4, Types.DATE);
        }

        stmt.setBoolean(5, tarefa.getComPrazo());
        stmt.setInt(6, tarefa.getId());
        stmt.executeUpdate();
    }

    // Excluir tarefa
    public void excluir(int idTarefa) throws SQLException {
        String sql = "DELETE FROM tarefa WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idTarefa);
        stmt.executeUpdate();
    }
}
