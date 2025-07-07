package com.consumption.kaban.ui;

import com.consumption.kaban.controller.ProjetoController;
import com.consumption.kaban.model.Projeto;

import javax.swing.*;
import java.awt.*;

public class CadastroProjetoView extends JDialog {
    private JTextField nomeField;
    private JTextArea descricaoArea;
    private ProjetoController controller;
    private Projeto projetoEditavel;
    private ProjetoView projetoView;

    public CadastroProjetoView(ProjetoView parent, ProjetoController controller) {
        this(parent, controller, null);
    }

    public CadastroProjetoView(ProjetoView parent, ProjetoController controller, Projeto projeto) {
        super(parent, true);
        this.controller = controller;
        this.projetoEditavel = projeto;
        this.projetoView = parent;

        setTitle(projeto == null ? "Novo Projeto" : "Editar Projeto");
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        nomeField = new JTextField();
        descricaoArea = new JTextArea(5, 20);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);

        if (projeto != null) {
            nomeField.setText(projeto.getNome());
            descricaoArea.setText(projeto.getDescricao());
        }

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Nome do Projeto:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(new JScrollPane(descricaoArea));
        add(formPanel, BorderLayout.CENTER);

        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String descricao = descricaoArea.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do projeto não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (projetoEditavel == null) {
                Projeto novo = new Projeto(nome, descricao);
                controller.adicionarProjeto(novo);
            } else {
                projetoEditavel.setNome(nome);
                projetoEditavel.setDescricao(descricao);
                controller.atualizarProjeto(projetoEditavel);
            }

            projetoView.carregarProjetos();
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(salvarBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
