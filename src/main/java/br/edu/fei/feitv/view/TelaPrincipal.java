package br.edu.fei.feitv.view;

import br.edu.fei.feitv.controller.CurtidaController;
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
 * Responsável pela busca de vídeos, acesso aos módulos principais,
 * adição rápida de vídeos às playlists e interação de curtidas.
 */
public class TelaPrincipal extends JFrame {

    private JTextField txtBusca;
    private JTextField txtIdLista;
    private JTextField txtIdVideoPlaylist;
    private JTextField txtIdVideoInteracao;

    private JTextArea txtResultado;

    private JButton btnBuscar;
    private JButton btnLimpar;
    private JButton btnAdicionarPlaylist;
    private JButton btnCurtir;
    private JButton btnDescurtir;
    private JButton btnVerCurtidas;
    private JButton btnPlaylists;
    private JButton btnHistorico;
    private JButton btnSair;

    public TelaPrincipal() {
        configurarJanela();
        criarComponentes();
    }

    private void configurarJanela() {
        setTitle("FEItv - Catálogo");
        setSize(1100, 760);
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

    private JScrollPane criarConteudo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(24, 24, 24));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        painel.add(criarPainelBusca());
        painel.add(Box.createVerticalStrut(15));
        painel.add(criarPainelAdicionarPlaylist());
        painel.add(Box.createVerticalStrut(15));
        painel.add(criarPainelInteracoesVideo());
        painel.add(Box.createVerticalStrut(15));
        painel.add(criarPainelResultados());

        JScrollPane scroll = new JScrollPane(painel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    /**
     * Painel de busca separado em campo e botões.
     * Essa estrutura evita que o campo de busca fique pequeno em janelas menores.
     */
    private JPanel criarPainelBusca() {
        JPanel painel = criarPainelEscuro("Buscar vídeos");

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(new Color(24, 24, 24));

        JLabel lblBusca = criarLabel("Digite o nome do vídeo:");

        txtBusca = new JTextField();
        txtBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtBusca.setPreferredSize(new Dimension(700, 34));

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

        btnPlaylists = new JButton("Playlists");
        configurarBotaoPrincipal(btnPlaylists);
        btnPlaylists.addActionListener(e -> abrirPlaylists());

        btnHistorico = new JButton("Histórico");
        configurarBotaoSecundario(btnHistorico);
        btnHistorico.addActionListener(e -> abrirHistorico());

        JPanel linhaBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaBotoes.setBackground(new Color(24, 24, 24));
        linhaBotoes.add(btnBuscar);
        linhaBotoes.add(btnLimpar);
        linhaBotoes.add(btnPlaylists);
        linhaBotoes.add(btnHistorico);

        conteudo.add(linhaCampo);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(linhaBotoes);

        painel.add(conteudo, BorderLayout.CENTER);

        return painel;
    }

    /**
     * Painel para adicionar rapidamente um vídeo pesquisado em uma playlist.
     */
    private JPanel criarPainelAdicionarPlaylist() {
        JPanel painel = criarPainelEscuro("Adicionar vídeo à playlist");

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha.setBackground(new Color(24, 24, 24));

        linha.add(criarLabel("ID da Playlist:"));
        txtIdLista = new JTextField(8);
        linha.add(txtIdLista);

        linha.add(criarLabel("ID do Vídeo:"));
        txtIdVideoPlaylist = new JTextField(8);
        linha.add(txtIdVideoPlaylist);

        btnAdicionarPlaylist = new JButton("Adicionar à Playlist");
        configurarBotaoPrincipal(btnAdicionarPlaylist);
        btnAdicionarPlaylist.addActionListener(e -> adicionarVideoNaPlaylist());

        linha.add(btnAdicionarPlaylist);

        JLabel dica = criarLabel("Consulte os IDs das playlists na tela Playlists.");
        linha.add(dica);

        painel.add(linha, BorderLayout.CENTER);

        return painel;
    }

    /**
     * Painel de ações relacionadas ao vídeo selecionado por ID.
     */
    private JPanel criarPainelInteracoesVideo() {
        JPanel painel = criarPainelEscuro("Interações com vídeo");

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha.setBackground(new Color(24, 24, 24));

        linha.add(criarLabel("ID do Vídeo:"));

        txtIdVideoInteracao = new JTextField(8);
        linha.add(txtIdVideoInteracao);

        btnCurtir = new JButton("Curtir");
        configurarBotaoPrincipal(btnCurtir);
        btnCurtir.addActionListener(e -> curtirVideo());

        btnDescurtir = new JButton("Descurtir");
        configurarBotaoSecundario(btnDescurtir);
        btnDescurtir.addActionListener(e -> descurtirVideo());

        btnVerCurtidas = new JButton("Ver Curtidas");
        configurarBotaoSecundario(btnVerCurtidas);
        btnVerCurtidas.addActionListener(e -> verCurtidas());

        linha.add(btnCurtir);
        linha.add(btnDescurtir);
        linha.add(btnVerCurtidas);

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

    private void adicionarVideoNaPlaylist() {
        int idLista = lerInteiro(
                txtIdLista,
                "Informe um ID de playlist válido."
        );

        int idVideo = lerInteiro(
                txtIdVideoPlaylist,
                "Informe um ID de vídeo válido."
        );

        if (idLista == -1 || idVideo == -1) {
            return;
        }

        ListaReproducaoController controller = new ListaReproducaoController();
        controller.adicionarVideoNaLista(idLista, idVideo);

        JOptionPane.showMessageDialog(
                this,
                "Vídeo adicionado à playlist com sucesso!"
        );
    }

    private void curtirVideo() {
        int idVideo = lerInteiro(
                txtIdVideoInteracao,
                "Informe um ID de vídeo válido para curtir."
        );

        if (idVideo == -1) {
            return;
        }

        CurtidaController controller = new CurtidaController();
        controller.curtirVideo(idVideo);

        JOptionPane.showMessageDialog(
                this,
                "Vídeo curtido com sucesso!"
        );
    }

    private void descurtirVideo() {
        int idVideo = lerInteiro(
                txtIdVideoInteracao,
                "Informe um ID de vídeo válido para descurtir."
        );

        if (idVideo == -1) {
            return;
        }

        CurtidaController controller = new CurtidaController();
        controller.descurtirVideo(idVideo);

        JOptionPane.showMessageDialog(
                this,
                "Curtida removida com sucesso!"
        );
    }

    private void verCurtidas() {
        int idVideo = lerInteiro(
                txtIdVideoInteracao,
                "Informe um ID de vídeo válido para consultar curtidas."
        );

        if (idVideo == -1) {
            return;
        }

        CurtidaController controller = new CurtidaController();
        int total = controller.contarCurtidas(idVideo);

        JOptionPane.showMessageDialog(
                this,
                "Total de curtidas do vídeo " + idVideo + ": " + total
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

    private void abrirPlaylists() {
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