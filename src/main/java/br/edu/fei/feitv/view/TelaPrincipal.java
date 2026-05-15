package br.edu.fei.feitv.view;

import br.edu.fei.feitv.controller.HistoricoController;
import br.edu.fei.feitv.controller.ListaReproducaoController;
import br.edu.fei.feitv.controller.VideoController;
import br.edu.fei.feitv.model.Video;
import br.edu.fei.feitv.session.SessaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Tela principal do sistema FEItv.
 * Responsável pela busca de vídeos, acesso aos módulos e adição rápida de vídeos às listas.
 */
public class TelaPrincipal extends JFrame {

    private JTextField txtBusca;
    private JTextField txtIdLista;
    private JTextField txtIdVideo;
    private JTextArea txtResultado;

    private JButton btnBuscar;
    private JButton btnLimpar;
    private JButton btnAdicionarLista;
    private JButton btnFavoritos;
    private JButton btnHistorico;
    private JButton btnSair;

    public TelaPrincipal() {
        configurarJanela();
        criarComponentes();
    }

    private void configurarJanela() {
        setTitle("FEItv - Catálogo");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(24, 24, 24));

        painelPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        painelPrincipal.add(criarConteudo(), BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("FEItv");
        lblTitulo.setForeground(new Color(255, 40, 40));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel lblUsuario = new JLabel();
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));

        if (SessaoUsuario.getUsuarioLogado() != null) {
            lblUsuario.setText("Usuário: " + SessaoUsuario.getUsuarioLogado().getNome());
        } else {
            lblUsuario.setText("Usuário não identificado");
        }

        painel.add(lblTitulo, BorderLayout.WEST);
        painel.add(lblUsuario, BorderLayout.EAST);

        return painel;
    }

    private JPanel criarConteudo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(24, 24, 24));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        painel.add(criarPainelBusca());
        painel.add(Box.createVerticalStrut(15));
        painel.add(criarPainelAdicionarLista());
        painel.add(Box.createVerticalStrut(15));
        painel.add(criarPainelResultados());

        return painel;
    }

    /**
     * Painel de busca separado em duas linhas.
     * Isso evita que o campo de busca fique pequeno ao abrir a tela em tamanho reduzido.
     */
    private JPanel criarPainelBusca() {
        JPanel painel = criarPainelEscuro("Buscar vídeos");

        JPanel painelBusca = new JPanel();
        painelBusca.setLayout(new BoxLayout(painelBusca, BoxLayout.Y_AXIS));
        painelBusca.setBackground(new Color(24, 24, 24));

        JLabel lblBusca = criarLabel("Digite o nome do vídeo:");

        txtBusca = new JTextField();
        txtBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtBusca.setPreferredSize(new Dimension(600, 34));

        JPanel linhaCampo = new JPanel(new BorderLayout(10, 10));
        linhaCampo.setBackground(new Color(24, 24, 24));
        linhaCampo.add(lblBusca, BorderLayout.NORTH);
        linhaCampo.add(txtBusca, BorderLayout.CENTER);

        btnBuscar = new JButton("Buscar");
        configurarBotaoPrincipal(btnBuscar);
        btnBuscar.addActionListener(e -> buscarVideos());

        btnLimpar = new JButton("Limpar");
        configurarBotaoSecundario(btnLimpar);
        btnLimpar.addActionListener(e -> limparBusca());

        btnFavoritos = new JButton("Favoritos / Listas");
        configurarBotaoPrincipal(btnFavoritos);
        btnFavoritos.addActionListener(e -> abrirFavoritos());

        btnHistorico = new JButton("Histórico");
        configurarBotaoSecundario(btnHistorico);
        btnHistorico.addActionListener(e -> abrirHistorico());

        JPanel linhaBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaBotoes.setBackground(new Color(24, 24, 24));
        linhaBotoes.add(btnBuscar);
        linhaBotoes.add(btnLimpar);
        linhaBotoes.add(btnFavoritos);
        linhaBotoes.add(btnHistorico);

        painelBusca.add(linhaCampo);
        painelBusca.add(Box.createVerticalStrut(10));
        painelBusca.add(linhaBotoes);

        painel.add(painelBusca, BorderLayout.CENTER);

        return painel;
    }

    /**
     * Painel para adicionar rapidamente um vídeo pesquisado em uma lista.
     * O usuário visualiza os IDs nos resultados e nas listas.
     */
    private JPanel criarPainelAdicionarLista() {
        JPanel painel = criarPainelEscuro("Adicionar vídeo a uma lista");

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha.setBackground(new Color(24, 24, 24));

        linha.add(criarLabel("ID da Lista:"));
        txtIdLista = new JTextField(8);
        linha.add(txtIdLista);

        linha.add(criarLabel("ID do Vídeo:"));
        txtIdVideo = new JTextField(8);
        linha.add(txtIdVideo);

        btnAdicionarLista = new JButton("Adicionar à Lista");
        configurarBotaoPrincipal(btnAdicionarLista);
        btnAdicionarLista.addActionListener(e -> adicionarVideoNaLista());

        linha.add(btnAdicionarLista);

        JLabel dica = criarLabel("Consulte os IDs das listas em Favoritos / Listas.");
        linha.add(dica);

        painel.add(linha, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelResultados() {
        JPanel painel = criarPainelEscuro("Resultados da busca");

        txtResultado = new JTextArea(20, 70);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultado.setBackground(new Color(245, 245, 245));
        txtResultado.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(txtResultado);

        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnSair = new JButton("Sair");
        configurarBotaoSecundario(btnSair);
        btnSair.addActionListener(e -> sair());

        painel.add(btnSair);

        return painel;
    }

    private void buscarVideos() {
        String termoBusca = txtBusca.getText();

        HistoricoController historicoController = new HistoricoController();
        historicoController.registrarBusca(termoBusca);

        VideoController controller = new VideoController();

        ArrayList<Video> videos = controller.buscarVideos(termoBusca);

        txtResultado.setText("");

        if (videos.isEmpty()) {
            txtResultado.setText("Nenhum vídeo encontrado.");
            return;
        }

        for (Video video : videos) {
            txtResultado.append("ID: " + video.getIdVideo() + "\n");
            txtResultado.append("Título: " + video.getTitulo() + "\n");
            txtResultado.append("Gênero: " + video.getGenero() + "\n");
            txtResultado.append("Ano: " + video.getAnoLancamento() + "\n");
            txtResultado.append("Descrição: " + video.getDescricao() + "\n");
            txtResultado.append("---------------------------------------------\n");
        }
    }

    private void adicionarVideoNaLista() {
        int idLista = lerInteiro(txtIdLista, "Informe um ID de lista válido.");
        int idVideo = lerInteiro(txtIdVideo, "Informe um ID de vídeo válido.");

        if (idLista == -1 || idVideo == -1) {
            return;
        }

        ListaReproducaoController controller = new ListaReproducaoController();

        controller.adicionarVideoNaLista(idLista, idVideo);

        JOptionPane.showMessageDialog(
                this,
                "Vídeo adicionado à lista com sucesso!"
        );
    }

    private int lerInteiro(JTextField campo, String mensagemErro) {
        try {
            return Integer.parseInt(campo.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, mensagemErro);
            return -1;
        }
    }

    private void limparBusca() {
        txtBusca.setText("");
        txtResultado.setText("");
    }

    private void abrirFavoritos() {
        this.dispose();
        new TelaFavoritos().setVisible(true);
    }

    private void abrirHistorico() {
        this.dispose();
        new TelaHistorico().setVisible(true);
    }

    private void sair() {
        this.dispose();
        new TelaLogin().setVisible(true);
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