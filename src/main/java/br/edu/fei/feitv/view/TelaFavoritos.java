package br.edu.fei.feitv.view;

import br.edu.fei.feitv.controller.ListaReproducaoController;
import br.edu.fei.feitv.model.ListaReproducao;
import br.edu.fei.feitv.session.SessaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Tela responsável pelo gerenciamento das listas do usuário.
 * Mantém apenas as operações de lista para reduzir poluição visual.
 */
public class TelaFavoritos extends JFrame {

    private JTextField txtNomeLista;
    private JTextField txtDescricao;
    private JTextField txtIdLista;

    private JTextArea txtAreaListas;

    private JButton btnCriarLista;
    private JButton btnEditarLista;
    private JButton btnExcluirLista;
    private JButton btnAbrirLista;
    private JButton btnAtualizar;
    private JButton btnVoltar;

    public TelaFavoritos() {
        configurarJanela();
        criarComponentes();
        carregarListas();
    }

    private void configurarJanela() {
        setTitle("FEItv - Favoritos / Listas");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(new Color(24, 24, 24));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painelPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        painelPrincipal.add(criarConteudo(), BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Favoritos / Listas");
        lblTitulo.setForeground(new Color(255, 40, 40));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel lblUsuario = new JLabel();

        if (SessaoUsuario.getUsuarioLogado() != null) {
            lblUsuario.setText("Usuário: " + SessaoUsuario.getUsuarioLogado().getNome());
        } else {
            lblUsuario.setText("Usuário não identificado");
        }

        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));

        painel.add(lblTitulo, BorderLayout.WEST);
        painel.add(lblUsuario, BorderLayout.EAST);

        return painel;
    }

    private JPanel criarConteudo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(24, 24, 24));

        painel.add(criarPainelFormulario());
        painel.add(Box.createVerticalStrut(15));
        painel.add(criarPainelListas());

        return painel;
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = criarPainelEscuro("Criar ou editar lista");
        painel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNome = criarLabel("Nome da Lista:");
        JLabel lblDescricao = criarLabel("Descrição:");

        txtNomeLista = new JTextField(25);
        txtDescricao = new JTextField(25);

        btnCriarLista = new JButton("Criar Lista");
        configurarBotaoPrincipal(btnCriarLista);
        btnCriarLista.addActionListener(e -> criarLista());

        btnEditarLista = new JButton("Editar Lista");
        configurarBotaoSecundario(btnEditarLista);
        btnEditarLista.addActionListener(e -> editarLista());

        btnExcluirLista = new JButton("Excluir Lista");
        configurarBotaoPrincipal(btnExcluirLista);
        btnExcluirLista.addActionListener(e -> excluirLista());

        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(lblNome, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painel.add(txtNomeLista, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(lblDescricao, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painel.add(txtDescricao, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.setBackground(new Color(24, 24, 24));
        painelBotoes.add(btnCriarLista);
        painelBotoes.add(btnEditarLista);
        painelBotoes.add(btnExcluirLista);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        painel.add(painelBotoes, gbc);

        return painel;
    }

    private JPanel criarPainelListas() {
        JPanel painel = criarPainelEscuro("Minhas listas");

        txtAreaListas = new JTextArea(16, 70);
        txtAreaListas.setEditable(false);
        txtAreaListas.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtAreaListas.setBackground(new Color(245, 245, 245));
        txtAreaListas.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(txtAreaListas);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAcoes.setBackground(new Color(24, 24, 24));

        painelAcoes.add(criarLabel("ID da Lista:"));

        txtIdLista = new JTextField(8);
        painelAcoes.add(txtIdLista);

        btnAbrirLista = new JButton("Abrir Lista");
        configurarBotaoPrincipal(btnAbrirLista);
        btnAbrirLista.addActionListener(e -> abrirLista());

        btnAtualizar = new JButton("Atualizar");
        configurarBotaoSecundario(btnAtualizar);
        btnAtualizar.addActionListener(e -> carregarListas());

        painelAcoes.add(btnAbrirLista);
        painelAcoes.add(btnAtualizar);

        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelAcoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnVoltar = new JButton("Voltar");
        configurarBotaoSecundario(btnVoltar);
        btnVoltar.addActionListener(e -> voltarParaTelaPrincipal());

        painel.add(btnVoltar);

        return painel;
    }

    private void carregarListas() {
        ListaReproducaoController controller = new ListaReproducaoController();

        ArrayList<ListaReproducao> listas = controller.listarListasDoUsuario();

        txtAreaListas.setText("");

        if (listas.isEmpty()) {
            txtAreaListas.setText("Nenhuma lista encontrada.");
            return;
        }

        txtAreaListas.append("Use o ID da lista para editar, excluir ou abrir.\n");
        txtAreaListas.append("---------------------------------------------\n");

        for (ListaReproducao lista : listas) {
            txtAreaListas.append("ID: " + lista.getIdLista());
            txtAreaListas.append(" | Nome: " + lista.getNome());
            txtAreaListas.append(" | Descrição: " + lista.getDescricao());
            txtAreaListas.append("\n");
        }
    }

    private void criarLista() {
        String nomeLista = txtNomeLista.getText();
        String descricao = txtDescricao.getText();

        if (nomeLista.isBlank()) {
            JOptionPane.showMessageDialog(this, "Informe o nome da lista.");
            return;
        }

        ListaReproducaoController controller = new ListaReproducaoController();

        controller.criarLista(nomeLista, descricao);

        JOptionPane.showMessageDialog(this, "Lista criada com sucesso!");

        txtNomeLista.setText("");
        txtDescricao.setText("");

        carregarListas();
    }

    private void editarLista() {
        int idLista = lerIdLista();

        if (idLista == -1) {
            return;
        }

        String nomeLista = txtNomeLista.getText();
        String descricao = txtDescricao.getText();

        if (nomeLista.isBlank()) {
            JOptionPane.showMessageDialog(this, "Informe o novo nome da lista.");
            return;
        }

        ListaReproducaoController controller = new ListaReproducaoController();

        controller.editarLista(idLista, nomeLista, descricao);

        JOptionPane.showMessageDialog(this, "Lista editada com sucesso!");

        carregarListas();
    }

    private void excluirLista() {
        int idLista = lerIdLista();

        if (idLista == -1) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir esta lista?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        ListaReproducaoController controller = new ListaReproducaoController();

        controller.excluirLista(idLista);

        JOptionPane.showMessageDialog(this, "Lista excluída com sucesso!");

        carregarListas();
    }

    private void abrirLista() {
        int idLista = lerIdLista();

        if (idLista == -1) {
            return;
        }

        this.dispose();
        new TelaDetalheLista(idLista).setVisible(true);
    }

    private int lerIdLista() {
        try {
            return Integer.parseInt(txtIdLista.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um ID de lista válido.");
            return -1;
        }
    }

    private void voltarParaTelaPrincipal() {
        this.dispose();
        new TelaPrincipal().setVisible(true);
    }

    private JPanel criarPainelEscuro(String titulo) {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBackground(new Color(24, 24, 24));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                titulo,
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));

        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private void configurarBotaoPrincipal(JButton botao) {
        botao.setBackground(new Color(220, 0, 0));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarBotaoSecundario(JButton botao) {
        botao.setBackground(new Color(70, 70, 70));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}