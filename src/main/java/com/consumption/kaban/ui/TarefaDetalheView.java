package com.consumption.kaban.ui;

import com.consumption.kaban.controller.TarefaController;
import com.consumption.kaban.enums.TarefaStatusEnum;
import com.consumption.kaban.model.Tarefa;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TarefaDetalheView extends JDialog {
    private final Tarefa tarefa;
    private final TarefaController tarefaController;
    private final Runnable aoAtualizar;

    public TarefaDetalheView(JFrame parent, Tarefa tarefa, TarefaController tarefaController, Runnable aoAtualizar) {
        super(parent, "Detalhes da Tarefa", true);
        this.tarefa = tarefa;
        this.tarefaController = tarefaController;
        this.aoAtualizar = aoAtualizar;

        setLayout(new BorderLayout());
        setSize(400, 250);
        setLocationRelativeTo(parent);

        // Painel com os detalhes
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painel.add(new JLabel("Título: " + tarefa.getTitulo()));
        painel.add(new JLabel("Descrição: " + tarefa.getDescricao()));

        if (tarefa.getComPrazo() && tarefa.getPrazo() != null) {
            painel.add(new JLabel("Prazo: " + tarefa.getPrazo()));
        } else {
            painel.add(new JLabel("Sem prazo definido"));
        }

        // ComboBox para mudar status
        painel.add(new JLabel("Status:"));
        JComboBox<TarefaStatusEnum> statusCombo = new JComboBox<>(TarefaStatusEnum.values());
        statusCombo.setSelectedItem(tarefa.getStatus());
        painel.add(statusCombo);

        // Botões
        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> {
            TarefaStatusEnum novoStatus = (TarefaStatusEnum) statusCombo.getSelectedItem();
            if (novoStatus != null && novoStatus != tarefa.getStatus()) {
                tarefa.setStatus(novoStatus);
                tarefaController.atualizarTarefa(tarefa);
                JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
                aoAtualizar.run();  // Atualiza visualmente no painel kanban
                dispose();
            } else {
                dispose();
            }
        });

        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(salvarBtn);
        botoes.add(cancelarBtn);

        add(painel, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
        setVisible(true);
    }
}
