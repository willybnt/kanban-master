package com.consumption.kaban.ui;

import com.consumption.kaban.controller.MetaController;
import com.consumption.kaban.controller.TarefaController;
import com.consumption.kaban.dao.DAOFactory;
import com.consumption.kaban.enums.TarefaStatusEnum;
import com.consumption.kaban.model.Projeto;
import com.consumption.kaban.model.Tarefa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjetoDetalheView extends JFrame {
    private final Projeto projeto;
    private final TarefaController tarefaController;
    private final MetaController metaController;

    private DefaultListModel<Tarefa> fazerModel = new DefaultListModel<>();
    private DefaultListModel<Tarefa> fazendoModel = new DefaultListModel<>();
    private DefaultListModel<Tarefa> feitoModel = new DefaultListModel<>();

    public ProjetoDetalheView(Projeto projeto, DAOFactory daoFactory) throws SQLException {
        this.projeto = projeto;
        this.tarefaController = new TarefaController(daoFactory);
        this.metaController = new MetaController(daoFactory);

        setTitle("Projeto: " + projeto.getNome());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Projeto: " + projeto.getNome() + " - " + projeto.getDescricao());
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titulo, BorderLayout.WEST);

        JButton metasBtn = new JButton("📌 Metas");
        metasBtn.addActionListener(e -> {
            new MetaDetalheView(this.projeto, metaController);
        });
        headerPanel.add(metasBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel kanbanPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        kanbanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        kanbanPanel.add(criarColuna("Fazer", fazerModel, TarefaStatusEnum.FAZER));
        kanbanPanel.add(criarColuna("Fazendo", fazendoModel, TarefaStatusEnum.FAZENDO));
        kanbanPanel.add(criarColuna("Feito", feitoModel, TarefaStatusEnum.FEITO));

        add(kanbanPanel, BorderLayout.CENTER);
        carregarTarefas();
        setVisible(true);
    }

    private JPanel criarColuna(String titulo, DefaultListModel<Tarefa> model, TarefaStatusEnum status) {
        JPanel coluna = new JPanel(new BorderLayout());
        coluna.setBorder(BorderFactory.createTitledBorder(titulo));

        JList<Tarefa> lista = new JList<>(model);
        lista.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(((Tarefa) value).getTitulo());
                return label;
            }
        });

        lista.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Tarefa selecionada = lista.getSelectedValue();
                    if (selecionada != null) {
                        new TarefaDetalheView(
                                ProjetoDetalheView.this,
                                selecionada,
                                tarefaController,
                                ProjetoDetalheView.this::carregarTarefas
                        ).setVisible(true);
                    }
                }
            }
        });

        coluna.add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton adicionarBtn = new JButton("+ Tarefa");
        adicionarBtn.addActionListener(e -> adicionarTarefa(status));
        coluna.add(adicionarBtn, BorderLayout.SOUTH);

        return coluna;
    }

    private void adicionarTarefa(TarefaStatusEnum status) {
        String titulo = JOptionPane.showInputDialog(this, "Título da Tarefa:");
        String descricao = JOptionPane.showInputDialog(this, "Descrição:");

        int temPrazo = JOptionPane.showConfirmDialog(this, "Essa tarefa tem prazo?", "Prazo", JOptionPane.YES_NO_OPTION);
        boolean comPrazo = (temPrazo == JOptionPane.YES_OPTION);

        Date prazo = null;
        if (comPrazo) {
            String prazoStr = JOptionPane.showInputDialog(this, "Digite o prazo (dd/MM/yyyy):");
            if (prazoStr != null && !prazoStr.trim().isEmpty()) {
                try {
                    prazo = new SimpleDateFormat("dd/MM/yyyy").parse(prazoStr);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data inválida. A tarefa será criada sem prazo.");
                    comPrazo = false;
                }
            } else {
                comPrazo = false;
            }
        }

        if (titulo != null && descricao != null) {
            Tarefa tarefa = new Tarefa(titulo, descricao, status);
            tarefa.setProjetoId(projeto.getId());
            tarefa.setStatus(status);
            tarefa.setComPrazo(comPrazo);
            tarefa.setPrazo(prazo != null ? new java.sql.Date(prazo.getTime()) : null);

            try {
                tarefaController.adicionarTarefa(tarefa, projeto.getId());
                carregarTarefas();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao adicionar tarefa.");
            }
        }
    }

    public void carregarTarefas() {
        fazerModel.clear();
        fazendoModel.clear();
        feitoModel.clear();

        List<Tarefa> tarefas = tarefaController.listarTarefasPorProjeto(projeto.getId());
        for (Tarefa t : tarefas) {
            if (t.getStatus() == TarefaStatusEnum.FAZER) {
                fazerModel.addElement(t);
            } else if (t.getStatus() == TarefaStatusEnum.FAZENDO) {
                fazendoModel.addElement(t);
            } else if (t.getStatus() == TarefaStatusEnum.FEITO) {
                feitoModel.addElement(t);
            }
        }
    }
}
