package com.consumption.kaban.ui;

import com.consumption.kaban.controller.ProjetoController;
import com.consumption.kaban.dao.DAOFactory;
import com.consumption.kaban.model.Projeto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ProjetoView extends JFrame {
    private DefaultListModel<String> projetoListModel;
    private JList<String> projetoList;
    private ProjetoController projetoController;

    public ProjetoView() throws SQLException {
        super("Projetos");

        DAOFactory daoFactory = new DAOFactory();
        projetoController = new ProjetoController(daoFactory);

        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        projetoListModel = new DefaultListModel<>();
        projetoList = new JList<>(projetoListModel);
        projetoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(projetoList);
        add(scrollPane, BorderLayout.CENTER);

        projetoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selecionado = projetoList.getSelectedValue();
                    if (selecionado != null) {
                        try {
                            int idProjeto = Integer.parseInt(selecionado.split(" - ")[0]);
                            Projeto projeto = projetoController.buscarProjetoPorId(idProjeto);
                            if (projeto != null) {
                                new ProjetoDetalheView(projeto, new DAOFactory());
                            } else {
                                JOptionPane.showMessageDialog(ProjetoView.this, "Projeto não encontrado.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ProjetoView.this, "Erro ao abrir projeto.");
                        }
                    }
                }
            }
        });

        JPanel bottomPanel = new JPanel();

        JButton novoProjetoBtn = new JButton("Novo Projeto");
        novoProjetoBtn.addActionListener(e -> new CadastroProjetoView(this, projetoController));
        bottomPanel.add(novoProjetoBtn);

        JButton editarProjetoBtn = new JButton("Editar Projeto");
        editarProjetoBtn.addActionListener(e -> {
            String selecionado = projetoList.getSelectedValue();
            if (selecionado != null) {
                int idProjeto = Integer.parseInt(selecionado.split(" - ")[0]);
                Projeto projeto = projetoController.buscarProjetoPorId(idProjeto);
                if (projeto != null) {
                    new CadastroProjetoView(this, projetoController, projeto);
                } else {
                    JOptionPane.showMessageDialog(this, "Projeto não encontrado.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para editar.");
            }
        });
        bottomPanel.add(editarProjetoBtn);

        JButton excluirProjetoBtn = new JButton("Excluir Projeto");
        excluirProjetoBtn.addActionListener(e -> {
            String selecionado = projetoList.getSelectedValue();
            if (selecionado != null) {
                int idProjeto = Integer.parseInt(selecionado.split(" - ")[0]);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Deseja realmente excluir este projeto?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    projetoController.removerProjeto(idProjeto);
                    carregarProjetos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para excluir.");
            }
        });
        bottomPanel.add(excluirProjetoBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        carregarProjetos();
        setVisible(true);
    }

    public void carregarProjetos() {
        projetoListModel.clear();
        List<Projeto> projetos = projetoController.listarProjetos();
        for (Projeto p : projetos) {
            projetoListModel.addElement(p.getId() + " - " + p.getNome());
        }
    }
}
