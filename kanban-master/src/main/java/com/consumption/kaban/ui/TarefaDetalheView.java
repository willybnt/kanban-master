package com.consumption.kaban.ui;

import com.consumption.kaban.controller.TarefaController;
import com.consumption.kaban.enums.TarefaStatusEnum;
import com.consumption.kaban.model.Tarefa;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Painel com os detalhes
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painel.add(new JLabel("Título:"));
        JTextField tituloField = new JTextField(tarefa.getTitulo());
        tituloField.setPreferredSize(new Dimension(200, 30)); // largura 200, altura 30
        painel.add(tituloField);
        
        painel.add(new JLabel("Descrição:"));
        JTextArea descricaoArea = new JTextArea(tarefa.getDescricao());
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        painel.add(new JScrollPane(descricaoArea)); // com scroll automático se necessário
            
        painel.add(new JLabel("Prazo:"));
        JTextField prazoField = new JTextField();
        if (tarefa.getComPrazo() && tarefa.getPrazo() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
             prazoField.setText(sdf.format(tarefa.getPrazo()));
            painel.add(prazoField);
        } else {
            painel.add(new JLabel("Sem prazo definido"));}

        // ComboBox para mudar status
        painel.add(new JLabel("Status:"));
        JComboBox<TarefaStatusEnum> statusCombo = new JComboBox<>(TarefaStatusEnum.values());
        statusCombo.setSelectedItem(tarefa.getStatus());
        painel.add(statusCombo);

        // Botões
        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> {
            try {
                tarefa.setTitulo((tituloField.getText()));
                tarefa.setDescricao(descricaoArea.getText());
                tarefa.setStatus((TarefaStatusEnum) statusCombo.getSelectedItem());
                
                if (prazoField != null && !prazoField.getText().isBlank()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    
                    Date dataConvertida = sdf.parse(prazoField.getText());

            LocalDate hoje = LocalDate.now();
            LocalDate prazoDigitado = dataConvertida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!prazoDigitado.isAfter(hoje)) {
                JOptionPane.showMessageDialog(this, "O prazo deve ser uma data futura!", "Data inválida", JOptionPane.WARNING_MESSAGE);
                return; 
            }
                    tarefa.setPrazo(new java.sql.Date(dataConvertida.getTime()));
                    tarefa.setComPrazo(true);
                } else {
                    tarefa.setPrazo(null);
                    tarefa.setComPrazo(false);
                }
        
                tarefaController.atualizarTarefa(tarefa);
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
                aoAtualizar.run();
                dispose();
        
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao atualizar tarefa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
