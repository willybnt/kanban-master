package com.consumption.kaban.controller;

import com.consumption.kaban.dao.DAOFactory;
import com.consumption.kaban.dao.TarefaDAO;
import com.consumption.kaban.model.Tarefa;

import java.sql.SQLException;
import java.util.List;

public class TarefaController {
    private final TarefaDAO tarefaDAO;

    public TarefaController(DAOFactory daoFactory) {
        this.tarefaDAO = daoFactory.getTarefaDAO();
    }

    public void adicionarTarefa(Tarefa tarefa, int idProjeto) {
        try {
            tarefaDAO.salvar(tarefa, idProjeto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar tarefa: " + e.getMessage());
        }
    }

    public List<Tarefa> listarTarefasPorProjeto(int idProjeto) {
        try {
            return tarefaDAO.buscarPorProjeto(idProjeto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar tarefas: " + e.getMessage());
        }
    }

    public void atualizarTarefa(Tarefa tarefa) {
        try {
            tarefaDAO.atualizar(tarefa);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    public void removerTarefa(int idTarefa) {
        try {
            tarefaDAO.excluir(idTarefa);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover tarefa: " + e.getMessage());
        }
    }
}
