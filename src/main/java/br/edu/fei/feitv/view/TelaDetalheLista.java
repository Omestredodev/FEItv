package br.edu.fei.feitv.view;

import br.edu.fei.feitv.controller.ListaReproducaoController;
import br.edu.fei.feitv.model.Video;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Tela dedicada a exibir e gerenciar os vídeos dentro de uma lista específica.
 * Foi separada da TelaFavoritos para reduzir poluição visual e melhorar a experiência do usuário.
 */
public class TelaDetalheLista extends JFrame {

    private final int idLista;

    private JTextField txtIdVideo;
    private JTextArea txtAreaVideos;

    private JButton btnRemoverVideo;
    private JButton btnAtualizar;
    private JButton btnVoltar;

    public TelaDetalheLista(int idLista) {
        this.idLista = idLista;

        configurarJanela();
        criarComponentes();
        carregarVideosDaLista();
    }

    private void configurarJanela() {
        setTitle("FEItv - Detalhes da Lista");
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

        JLabel lblTitulo = new JLabel("Detalhes da Lista");
        lblTitulo.setForeground(new Color(255, 40, 40));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel lblLista = new JLabel("ID da Lista: " + idLista);
        lblLista.setForeground(Color.WHITE);
        lblLista.setFont(new Font("Arial", Font.PLAIN, 14));

        painel.add(lblTitulo, BorderLayout.WEST);
        painel.add(lblLista, BorderLayout.EAST);

        return painel;
    }

    private JPanel criarCentro() {
        JPanel painel = criarPainelEscuro("Vídeos da lista");

        txtAreaVideos = new JTextArea();
        txtAreaVideos.setEditable(false);
        txtAreaVideos.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtAreaVideos.setBackground(new Color(245, 245, 245));
        txtAreaVideos.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(txtAreaVideos);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAcoes.setBackground(new Color(24, 24, 24));

        painelAcoes.add(criarLabel("ID do Vídeo para remover:"));

        txtIdVideo = new JTextField(8);
        painelAcoes.add(txtIdVideo);

        btnRemoverVideo = new JButton("Remover Vídeo");
        configurarBotaoPrincipal(btnRemoverVideo);
        btnRemoverVideo.addActionListener(e -> removerVideo());

        btnAtualizar = new JButton("Atualizar");
        configurarBotaoSecundario(btnAtualizar);
        btnAtualizar.addActionListener(e -> carregarVideosDaLista());

        painelAcoes.add(btnRemoverVideo);
        painelAcoes.add(btnAtualizar);

        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelAcoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(new Color(15, 15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnVoltar = new JButton("Voltar para Favoritos");
        configurarBotaoSecundario(btnVoltar);
        btnVoltar.addActionListener(e -> voltarParaFavoritos());

        painel.add(btnVoltar);

        return painel;
    }

    private void carregarVideosDaLista() {
        ListaReproducaoController controller = new ListaReproducaoController();

        ArrayList<Video> videos = controller.listarVideosDaLista(idLista);

        txtAreaVideos.setText("");

        if (videos.isEmpty()) {
            txtAreaVideos.setText("Nenhum vídeo encontrado nesta lista.");
            return;
        }

        for (Video video : videos) {
            txtAreaVideos.append("ID: " + video.getIdVideo() + "\n");
            txtAreaVideos.append("Título: " + video.getTitulo() + "\n");
            txtAreaVideos.append("Gênero: " + video.getGenero() + "\n");
            txtAreaVideos.append("Ano: " + video.getAnoLancamento() + "\n");
            txtAreaVideos.append("Descrição: " + video.getDescricao() + "\n");
            txtAreaVideos.append("---------------------------------------------\n");
        }
    }

    private void removerVideo() {
        int idVideo = lerIdVideo();

        if (idVideo == -1) {
            return;
        }

        ListaReproducaoController controller = new ListaReproducaoController();

        controller.removerVideoDaLista(idLista, idVideo);

        JOptionPane.showMessageDialog(this, "Vídeo removido da lista!");

        carregarVideosDaLista();
    }

    private int lerIdVideo() {
        try {
            return Integer.parseInt(txtIdVideo.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um ID de vídeo válido.");
            return -1;
        }
    }

    private void voltarParaFavoritos() {
        this.dispose();
        new TelaFavoritos().setVisible(true);
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