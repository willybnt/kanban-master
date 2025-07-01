package com.consumption.kaban.ui;

import com.consumption.kaban.controller.ProjetoController;
import com.consumption.kaban.model.Projeto;
import com.consumption.kaban.ui.ProjetoView;

import javax.swing.*;
import java.awt.*;

public class CadastroProjetoView extends JFrame {
    private JTextField nomeField;
    private JTextArea descricaoArea;
    private ProjetoController projetoController;
    private ProjetoView projetoView;

    public CadastroProjetoView(ProjetoView projetoView, ProjetoController projetoController) {
        super("Novo Projeto");

        this.projetoView = projetoView;
        this.projetoController = projetoController;

        setSize(350, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(4, 1));
        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descricaoArea));

        add(formPanel, BorderLayout.CENTER);

        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> salvarProjeto());
        add(salvarBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void salvarProjeto() {
        String nome = nomeField.getText().trim();
        String desc = descricaoArea.getText().trim();

        if (nome.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        Projeto projeto = new Projeto(nome, desc);
        projetoController.adicionarProjeto(projeto);
        JOptionPane.showMessageDialog(this, "Projeto criado com sucesso!");
        projetoView.carregarProjetos();
        dispose(); // fecha janela
    }
}
