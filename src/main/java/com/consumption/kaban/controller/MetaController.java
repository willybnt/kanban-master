package com.consumption.kaban.controller;

import com.consumption.kaban.dao.DAOFactory;
import com.consumption.kaban.dao.MetaDAO;
import com.consumption.kaban.model.Meta;

import java.sql.SQLException;
import java.util.List;

public class MetaController {
    private final MetaDAO metaDAO;

    public MetaController(DAOFactory daoFactory) {
        this.metaDAO = daoFactory.getMetaDAO();
    }

    // Salvar nova meta em um projeto
    public void adicionarMeta(Meta meta, int projetoId) {
        try {
            metaDAO.salvar(meta, projetoId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar meta: " + e.getMessage());
        }
    }

    // Listar todas as metas de um projeto
    public List<Meta> listarMetasPorProjeto(int projetoId) {
        try {
            return metaDAO.buscarPorProjeto(projetoId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar metas: " + e.getMessage());
        }
    }

    // Buscar uma meta pelo ID
    public Meta buscarMetaPorId(int id) {
        try {
            return metaDAO.buscarPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar meta: " + e.getMessage());
        }
    }

    // Atualizar dados de uma meta
    public void atualizarMeta(Meta meta) {
        try {
            metaDAO.atualizar(meta);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar meta: " + e.getMessage());
        }
    }

    // Remover meta por ID
    public void removerMeta(int id) {
        try {
            metaDAO.remover(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover meta: " + e.getMessage());
        }
    }
}
