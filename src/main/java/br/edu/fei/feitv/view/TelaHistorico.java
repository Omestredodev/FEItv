package br.edu.fei.feitv.view;

import br.edu.fei.feitv.controller.HistoricoController;
import br.edu.fei.feitv.session.SessaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Tela responsável por exibir e limpar o histórico de buscas do usuário.
 */
public class TelaHistorico extends JFrame {

    private JTextArea txtAreaHistorico;

    private JButton btnAtualizar;
    private JButton btnLimpar;
    private JButton btnVoltar;

    public TelaHistorico() {
        configurarJanela();
        criarComponentes();
        carregarHistorico();
    }

    private void configurarJanela() {
        setTitle("FEItv - Histórico");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(new Color(24, 24, 24));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painelPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        painelPrincipal.add(criarCentro(), BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Histórico de Buscas");
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

    private JPanel criarCentro() {

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(24, 24, 24));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                "Pesquisas realizadas",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));

        txtAreaHistorico = new JTextArea();
        txtAreaHistorico.setEditable(false);
        txtAreaHistorico.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtAreaHistorico.setBackground(new Color(245, 245, 245));
        txtAreaHistorico.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(txtAreaHistorico);

        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarRodape() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnAtualizar = new JButton("Atualizar");
        configurarBotaoSecundario(btnAtualizar);
        btnAtualizar.addActionListener(e -> carregarHistorico());

        btnLimpar = new JButton("Limpar Histórico");
        configurarBotaoPrincipal(btnLimpar);
        btnLimpar.addActionListener(e -> limparHistorico());

        btnVoltar = new JButton("Voltar");
        configurarBotaoSecundario(btnVoltar);
        btnVoltar.addActionListener(e -> voltar());

        painel.add(btnAtualizar);
        painel.add(btnLimpar);
        painel.add(btnVoltar);

        return painel;
    }

    private void carregarHistorico() {

        HistoricoController controller = new HistoricoController();

        ArrayList<String> historico = controller.listarHistorico();

        txtAreaHistorico.setText("");

        if (historico.isEmpty()) {
            txtAreaHistorico.setText("Nenhuma busca registrada.");
            return;
        }

        for (String item : historico) {
            txtAreaHistorico.append(item + "\n");
            txtAreaHistorico.append("---------------------------------------------\n");
        }
    }

    private void limparHistorico() {

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente limpar todo o histórico?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        HistoricoController controller = new HistoricoController();

        controller.limparHistorico();

        JOptionPane.showMessageDialog(
                this,
                "Histórico limpo com sucesso!"
        );

        carregarHistorico();
    }

    private void voltar() {
        this.dispose();
        new TelaPrincipal().setVisible(true);
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