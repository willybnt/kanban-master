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

        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Lista de projetos
        projetoListModel = new DefaultListModel<>();
        projetoList = new JList<>(projetoListModel);
        JScrollPane scrollPane = new JScrollPane(projetoList);
        add(scrollPane, BorderLayout.CENTER);

        // Ação de duplo clique para abrir o quadro Kanban do projeto
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
                                JOptionPane.showMessageDialog(null, "Projeto não encontrado.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erro ao abrir projeto.");
                        }
                    }
                }
            }
        });

        // Botão de novo projeto
        JButton novoProjetoBtn = new JButton("Novo Projeto");
        novoProjetoBtn.addActionListener(e -> {
            new CadastroProjetoView(this, projetoController);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(novoProjetoBtn);
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
