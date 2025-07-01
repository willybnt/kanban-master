package com.consumption.kaban.ui;

import com.consumption.kaban.controller.MetaController;
import com.consumption.kaban.model.Meta;
import com.consumption.kaban.model.Projeto;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetaFormularioDialog extends JDialog {
    private final Projeto projeto;
    private final MetaController metaController;
    private final Runnable aoAtualizar;
    private final Meta meta;  // Pode ser null (para novo)

    private JTextField descricaoField;
    private JCheckBox comPrazoCheck;
    private JTextField prazoField;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public MetaFormularioDialog(JFrame parent, Projeto projeto, Meta meta, MetaController metaController, Runnable aoAtualizar) {
        super(parent, meta == null ? "Nova Meta" : "Editar Meta", true);
        this.projeto = projeto;
        this.meta = meta;
        this.metaController = metaController;
        this.aoAtualizar = aoAtualizar;

        initComponents();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        setSize(400, 250);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        descricaoField = new JTextField();
        comPrazoCheck = new JCheckBox("Tem prazo?");
        prazoField = new JTextField();
        prazoField.setEnabled(false);

        comPrazoCheck.addActionListener(e -> prazoField.setEnabled(comPrazoCheck.isSelected()));

        panel.add(new JLabel("Descrição:"));
        panel.add(descricaoField);
        panel.add(comPrazoCheck);
        panel.add(new JLabel("Prazo (dd/MM/yyyy):"));
        panel.add(prazoField);

        if (meta != null) {
            descricaoField.setText(meta.getDescricao());
            comPrazoCheck.setSelected(meta.getComPrazo());
            if (meta.getPrazo() != null) {
                prazoField.setText(sdf.format(meta.getPrazo()));
            }
            prazoField.setEnabled(meta.getComPrazo());
        }

        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> salvarMeta());

        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(salvarBtn);
        botoes.add(cancelarBtn);

        add(panel, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }

    private void salvarMeta() {
        try {
            String descricao = descricaoField.getText().trim();
            boolean comPrazo = comPrazoCheck.isSelected();
            Date prazo = null;

            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição não pode ser vazia!");
                return;
            }

            if (comPrazo) {
                String prazoStr = prazoField.getText().trim();
                if (prazoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Digite o prazo ou desmarque a opção de prazo.");
                    return;
                }
                try {
                    prazo = sdf.parse(prazoStr);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Data de prazo inválida. Use o formato dd/MM/yyyy.");
                    return;
                }
            }

            if (meta == null) {
                Meta novaMeta = new Meta(descricao, prazo, false); // nova meta inicia não concluída
                novaMeta.setComPrazo(comPrazo);
                novaMeta.setPrazo(prazo != null ? new java.sql.Date(prazo.getTime()) : null);
                novaMeta.setProjetoId(projeto.getId());

                metaController.adicionarMeta(novaMeta, projeto.getId());
            } else {
                meta.setDescricao(descricao);
                meta.setComPrazo(comPrazo);
                meta.setPrazo(prazo != null ? new java.sql.Date(prazo.getTime()) : null);

                metaController.atualizarMeta(meta);
            }

            JOptionPane.showMessageDialog(this, "Meta salva com sucesso!");
            aoAtualizar.run();
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar meta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
