package com.consumption.kaban.ui;

import com.consumption.kaban.controller.MetaController;
import com.consumption.kaban.model.Meta;
import com.consumption.kaban.model.Projeto;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MetaDetalheView extends JFrame {

    private final Projeto projeto;
    private final MetaController metaController;
    private final DefaultListModel<Meta> metasModel = new DefaultListModel<>();
    private final JList<Meta> metaList = new JList<>(metasModel);

    private final JButton concluirBtn = new JButton("Concluir");
    private final JButton editarBtn = new JButton("Editar");
    private final JButton excluirBtn = new JButton("Excluir");

    public MetaDetalheView(Projeto projeto, MetaController metaController) {
        this.projeto = projeto;
        this.metaController = metaController;

        setTitle("Metas do Projeto: " + projeto.getNome());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton adicionarBtn = new JButton("+ Nova Meta");
        adicionarBtn.addActionListener(
                e -> new MetaFormularioView(this, projeto, null,
                        metaController, this::carregarMetas));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(adicionarBtn);
        add(topo, BorderLayout.NORTH);

        metaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        metaList.setCellRenderer(new MetaCellRenderer());
        metaList.addListSelectionListener(e -> atualizarBotoes());

        add(new JScrollPane(metaList), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        concluirBtn.addActionListener(e -> alternarConclusao());
        editarBtn.addActionListener(e -> editarSelecionada());
        excluirBtn.addActionListener(e -> excluirSelecionada());

        botoes.add(concluirBtn);
        botoes.add(editarBtn);
        botoes.add(excluirBtn);
        add(botoes, BorderLayout.SOUTH);

        carregarMetas();
        atualizarBotoes();
        setVisible(true);
    }

    private static class MetaCellRenderer extends DefaultListCellRenderer {
        private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            Meta meta = (Meta) value;

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list,
                    "",
                    index, isSelected, cellHasFocus);

            // Símbolo check para metas concluídas
            String check = meta.isConcluida() ? "✅ " : "";

            // Construção do texto com prazo se tiver
            StringBuilder sb = new StringBuilder("<html>");
            sb.append(check).append("<b>").append(meta.getDescricao()).append("</b>");
            if (meta.getComPrazo() && meta.getPrazo() != null) {
                sb.append("<br>Prazo: ").append(SDF.format(meta.getPrazo()));
            }
            sb.append("</html>");

            label.setText(sb.toString());

            return label;
        }
    }

    private void carregarMetas() {
        metasModel.clear();
        List<Meta> metas = metaController.listarMetasPorProjeto(projeto.getId());
        metas.forEach(metasModel::addElement);
        metaList.repaint();
        atualizarBotoes();
    }

    private void atualizarBotoes() {
        Meta selecionada = metaList.getSelectedValue();
        boolean ativa = selecionada != null;
        concluirBtn.setEnabled(ativa);
        editarBtn.setEnabled(ativa);
        excluirBtn.setEnabled(ativa);

        concluirBtn.setText(ativa && selecionada.isConcluida()
                ? "Desconcluir" : "Concluir");
    }

    private void alternarConclusao() {
        Meta meta = metaList.getSelectedValue();
        if (meta == null) return;

        meta.setConcluida(!meta.isConcluida());
        metaController.atualizarMeta(meta);
        carregarMetas();
    }

    private void editarSelecionada() {
        Meta meta = metaList.getSelectedValue();
        if (meta != null) {
            new MetaFormularioView(this, projeto, meta,
                    metaController, this::carregarMetas);
        }
    }

    private void excluirSelecionada() {
        Meta meta = metaList.getSelectedValue();
        if (meta != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Excluir esta meta?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                metaController.removerMeta(meta.getId());
                carregarMetas();
            }
        }
    }
}