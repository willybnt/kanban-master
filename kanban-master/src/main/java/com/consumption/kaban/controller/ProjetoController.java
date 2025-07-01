package com.consumption.kaban.controller;

import com.consumption.kaban.dao.DAOFactory;
import com.consumption.kaban.dao.ProjetoDAO;
import com.consumption.kaban.model.Projeto;

import java.sql.SQLException;
import java.util.List;

public class ProjetoController {
    private final ProjetoDAO projetoDAO;

    public ProjetoController(DAOFactory daoFactory) {
        this.projetoDAO = daoFactory.getProjetoDAO();
    }

    // Adiciona um novo projeto
    public void adicionarProjeto(Projeto projeto) {
        try {
            projetoDAO.salvar(projeto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar projeto: " + e.getMessage());
        }
    }

    // Lista todos os projetos do banco
    public List<Projeto> listarProjetos() {
        try {
            return projetoDAO.listarTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar projetos: " + e.getMessage());
        }
    }

    // Buscar projeto por ID
    public Projeto buscarProjetoPorId(int id) {
        try {
            return projetoDAO.buscarPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar projeto por ID: " + e.getMessage());
        }
    }

    // Atualizar um projeto
    public void atualizarProjeto(Projeto projeto) {
        try {
            projetoDAO.atualizar(projeto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    // Remover um projeto
    public void removerProjeto(int id) {
        try {
            projetoDAO.remover(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover projeto: " + e.getMessage());
        }
    }
}
