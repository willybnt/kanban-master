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

    // Adiciona uma nova tarefa a um projeto
    public void adicionarTarefa(Tarefa tarefa, int idProjeto) {
        try {
            tarefaDAO.salvar(tarefa, idProjeto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar tarefa: " + e.getMessage());
        }
    }

    // Retorna lista de tarefas de um projeto
    public List<Tarefa> listarTarefasPorProjeto(int idProjeto) {
        try {
            return tarefaDAO.buscarPorProjeto(idProjeto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar tarefas: " + e.getMessage());
        }
    }

    // Atualiza dados de uma tarefa
    public void atualizarTarefa(Tarefa tarefa) {
        try {
            tarefaDAO.atualizar(tarefa);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    // Remove uma tarefa pelo ID
    public void removerTarefa(int idTarefa) {
        try {
            tarefaDAO.excluir(idTarefa);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover tarefa: " + e.getMessage());
        }
    }
}
