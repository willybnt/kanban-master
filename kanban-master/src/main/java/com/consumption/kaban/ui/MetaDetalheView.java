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
    private final JButton editarBtn     = new JButton("Editar");
    private final JButton excluirBtn    = new JButton("Excluir");

    public MetaDetalheView(Projeto projeto, MetaController metaController) {
        this.projeto         = projeto;
        this.metaController  = metaController;

        setTitle("Metas do Projeto: " + projeto.getNome());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ------ Topo (novo) ------------------------------------------------ */
        JButton adicionarBtn = new JButton("+ Nova Meta");
        adicionarBtn.addActionListener(
                e -> new MetaFormularioDialog(this, projeto, null,
                        metaController, this::carregarMetas));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(adicionarBtn);
        add(topo, BorderLayout.NORTH);

        /* ------ Lista com renderer customizado ----------------------------- */
        metaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        metaList.setCellRenderer(new MetaCellRenderer());
        metaList.addListSelectionListener(e -> atualizarBotoes());

        add(new JScrollPane(metaList), BorderLayout.CENTER);

        /* ------ Botões inferiores ------------------------------------------ */
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        concluirBtn.addActionListener(e -> alternarConclusao());
        editarBtn   .addActionListener(e -> editarSelecionada());
        excluirBtn  .addActionListener(e -> excluirSelecionada());

        botoes.add(concluirBtn);
        botoes.add(editarBtn);
        botoes.add(excluirBtn);
        add(botoes, BorderLayout.SOUTH);

        /* ------ Carrega tudo ---------------------------------------------- */
        carregarMetas();
        atualizarBotoes();
        setVisible(true);
    }

    /* ====================================================================== */
    /*  RENDERER  ************************************************************ */
    /* ====================================================================== */
    private static class MetaCellRenderer extends DefaultListCellRenderer {
        private static final Color BG_CONCLUIDA     = new Color(210, 240, 210);
        private static final Color BG_CONCLUIDA_SEL = new Color(180, 220, 180);
        private static final SimpleDateFormat SDF   = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            Meta meta = (Meta) value;

            // Deixe o LAF fazer o trabalho pesado primeiro
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list,                    // a própria lista
                    "",                      // texto será definido já já
                    index, isSelected, cellHasFocus);

            /* ------ Monta texto em HTML ----------------------------------- */
            boolean concluida = meta.isConcluida();
            StringBuilder sb  = new StringBuilder("<html>");

            if (concluida) sb.append("<strike>");
            sb.append("<b>").append(meta.getDescricao()).append("</b>");

            if (meta.getComPrazo() && meta.getPrazo() != null) {
                sb.append("<br>")
                        .append(concluida ? "<strike>" : "")
                        .append("Prazo: ").append(SDF.format(meta.getPrazo()));
                if (concluida) sb.append("</strike>");
            }
            if (concluida) sb.append("</strike>");
            sb.append("</html>");
            label.setText(sb.toString());

            /* ------ Cores -------------------------------------------------- */
            if (concluida) {
                label.setBackground(isSelected ? BG_CONCLUIDA_SEL : BG_CONCLUIDA);
                label.setForeground(Color.GRAY);
            } else {
                // já está tudo certo para itens não concluídos: a cor de fundo/
                // texto foi aplicada pelo super acima (respeitando seleção/LAF)
            }

            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            label.setOpaque(true);
            return label;
        }
    }

    /* ====================================================================== */
    /*  CRUD & utilitários *************************************************** */
    /* ====================================================================== */

    private void carregarMetas() {
        metasModel.clear();
        List<Meta> metas = metaController.listarMetasPorProjeto(projeto.getId());
        metas.forEach(metasModel::addElement);
        metaList.repaint();         // força redesenho
        atualizarBotoes();
    }

    private void atualizarBotoes() {
        Meta selecionada = metaList.getSelectedValue();
        boolean ativa = selecionada != null;
        concluirBtn.setEnabled(ativa);
        editarBtn   .setEnabled(ativa);
        excluirBtn  .setEnabled(ativa);

        concluirBtn.setText(ativa && selecionada.isConcluida()
                ? "Desconcluir" : "Concluir");
    }

    private void alternarConclusao() {
        Meta meta = metaList.getSelectedValue();
        if (meta == null) return;

        meta.setConcluida(!meta.isConcluida());
        metaController.atualizarMeta(meta);
        carregarMetas();            // recarrega e repinta
    }

    private void editarSelecionada() {
        Meta meta = metaList.getSelectedValue();
        if (meta != null) {
            new MetaFormularioDialog(this, projeto, meta,
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
