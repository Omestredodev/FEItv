package br.edu.fei.feitv.main;

import br.edu.fei.feitv.controller.CurtidaController;
import br.edu.fei.feitv.controller.HistoricoController;
import br.edu.fei.feitv.controller.ListaReproducaoController;
import br.edu.fei.feitv.controller.VideoController;
import br.edu.fei.feitv.dao.UsuarioDAO;
import br.edu.fei.feitv.database.ConnectionFactory;
import br.edu.fei.feitv.model.Usuario;
import br.edu.fei.feitv.model.Video;
import br.edu.fei.feitv.session.SessaoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Teste de integração do sistema FEItv.
 *
 * Esta classe testa os principais fluxos do sistema sem depender da interface gráfica.
 *
 * Objetivo:
 * - Validar conexão com PostgreSQL.
 * - Validar cadastro e login.
 * - Validar sessão do usuário.
 * - Validar catálogo de vídeos.
 * - Validar busca de vídeos.
 * - Validar histórico de buscas.
 * - Validar curtida e descurtida.
 * - Validar criação, edição, exclusão e uso de playlists.
 * - Validar relacionamento entre playlist e vídeo.
 *
 * Observação:
 * Este teste cria dados temporários no banco e remove esses dados ao final.
 */
public class TesteIntegracaoFEItv {

    private static int totalTestes = 0;
    private static int totalAprovados = 0;
    private static int totalFalhas = 0;

    private static Usuario usuarioTeste;
    private static Video videoTeste;
    private static int idPlaylistTeste = -1;

    public static void main(String[] args) {

        imprimirCabecalhoGeral();

        try {
            executarTeste("Conexão com PostgreSQL", TesteIntegracaoFEItv::testarConexaoBanco);
            executarTeste("Cadastro de usuário", TesteIntegracaoFEItv::testarCadastroUsuario);
            executarTeste("Login de usuário", TesteIntegracaoFEItv::testarLoginUsuario);
            executarTeste("Sessão de usuário", TesteIntegracaoFEItv::testarSessaoUsuario);
            executarTeste("Catálogo de vídeos", TesteIntegracaoFEItv::testarCatalogoVideos);
            executarTeste("Busca de vídeos por nome", TesteIntegracaoFEItv::testarBuscaVideos);
            executarTeste("Histórico de buscas", TesteIntegracaoFEItv::testarHistoricoBuscas);
            executarTeste("Curtir vídeo", TesteIntegracaoFEItv::testarCurtirVideo);
            executarTeste("Evitar curtida duplicada", TesteIntegracaoFEItv::testarCurtidaDuplicada);
            executarTeste("Descurtir vídeo", TesteIntegracaoFEItv::testarDescurtirVideo);
            executarTeste("Criar playlist", TesteIntegracaoFEItv::testarCriarPlaylist);
            executarTeste("Editar playlist", TesteIntegracaoFEItv::testarEditarPlaylist);
            executarTeste("Adicionar vídeo à playlist", TesteIntegracaoFEItv::testarAdicionarVideoPlaylist);
            executarTeste("Listar vídeos da playlist", TesteIntegracaoFEItv::testarListarVideosPlaylist);
            executarTeste("Remover vídeo da playlist", TesteIntegracaoFEItv::testarRemoverVideoPlaylist);
            executarTeste("Excluir playlist", TesteIntegracaoFEItv::testarExcluirPlaylist);

        } finally {
            executarLimpezaFinal();
            imprimirResumoFinal();
        }
    }

    private static void testarConexaoBanco() throws Exception {

        try (Connection conn = ConnectionFactory.conectar()) {

            validar(
                    conn != null,
                    "A conexão retornou nula."
            );

            validar(
                    !conn.isClosed(),
                    "A conexão foi aberta, mas já está fechada."
            );
        }
    }

    private static void testarCadastroUsuario() throws Exception {

        String identificador = String.valueOf(System.currentTimeMillis());

        usuarioTeste = new Usuario();
        usuarioTeste.setNome("Usuario Teste Integracao");
        usuarioTeste.setEmail("teste_integracao_" + identificador + "@email.com");
        usuarioTeste.setSenha("123456");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.cadastrarUsuario(usuarioTeste);

        Usuario usuarioEncontrado = buscarUsuarioPorEmail(usuarioTeste.getEmail());

        validar(
                usuarioEncontrado != null,
                "O usuário não foi encontrado no banco após o cadastro."
        );

        validar(
                usuarioEncontrado.getIdUsuario() > 0,
                "O usuário foi encontrado, mas não possui ID válido."
        );

        usuarioTeste = usuarioEncontrado;
    }

    private static void testarLoginUsuario() throws Exception {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuarioLogado = usuarioDAO.validarLogin(
                usuarioTeste.getEmail(),
                usuarioTeste.getSenha()
        );

        validar(
                usuarioLogado != null,
                "O login retornou usuário nulo."
        );

        validar(
                usuarioLogado.getEmail().equals(usuarioTeste.getEmail()),
                "O usuário retornado no login não corresponde ao usuário cadastrado."
        );

        usuarioTeste = usuarioLogado;
    }

    private static void testarSessaoUsuario() throws Exception {

        SessaoUsuario.setUsuarioLogado(usuarioTeste);

        Usuario usuarioSessao = SessaoUsuario.getUsuarioLogado();

        validar(
                usuarioSessao != null,
                "A sessão não armazenou o usuário logado."
        );

        validar(
                usuarioSessao.getIdUsuario() == usuarioTeste.getIdUsuario(),
                "A sessão armazenou um usuário diferente do esperado."
        );
    }

    private static void testarCatalogoVideos() throws Exception {

        VideoController videoController = new VideoController();

        ArrayList<Video> videos = videoController.listarTodosVideos();

        validar(
                videos != null,
                "A listagem de vídeos retornou null."
        );

        validar(
                !videos.isEmpty(),
                "O catálogo não possui vídeos cadastrados."
        );

        videoTeste = videos.get(0);

        validar(
                videoTeste.getIdVideo() > 0,
                "O primeiro vídeo retornado não possui ID válido."
        );

        validar(
                videoTeste.getTitulo() != null && !videoTeste.getTitulo().isBlank(),
                "O primeiro vídeo retornado não possui título válido."
        );

        System.out.println("        Vídeo usado nos testes: "
                + videoTeste.getIdVideo()
                + " - "
                + videoTeste.getTitulo());
    }

    private static void testarBuscaVideos() throws Exception {

        VideoController videoController = new VideoController();

        String termoBusca = videoTeste.getTitulo().substring(
                0,
                Math.min(3, videoTeste.getTitulo().length())
        );

        ArrayList<Video> videosEncontrados = videoController.buscarVideos(termoBusca);

        validar(
                videosEncontrados != null,
                "A busca retornou null."
        );

        validar(
                !videosEncontrados.isEmpty(),
                "A busca não retornou vídeos para um termo existente."
        );

        boolean encontrouVideoTeste = false;

        for (Video video : videosEncontrados) {
            if (video.getIdVideo() == videoTeste.getIdVideo()) {
                encontrouVideoTeste = true;
                break;
            }
        }

        validar(
                encontrouVideoTeste,
                "A busca não retornou o vídeo esperado."
        );
    }

    private static void testarHistoricoBuscas() throws Exception {

        HistoricoController historicoController = new HistoricoController();

        String termo = "teste_historico_integracao";

        historicoController.registrarBusca(termo);

        ArrayList<String> historico = historicoController.listarHistorico();

        validar(
                historico != null,
                "O histórico retornou null."
        );

        validar(
                !historico.isEmpty(),
                "O histórico está vazio após registrar uma busca."
        );

        boolean encontrouTermo = false;

        for (String item : historico) {
            if (item.toLowerCase().contains(termo.toLowerCase())) {
                encontrouTermo = true;
                break;
            }
        }

        validar(
                encontrouTermo,
                "O termo registrado não foi encontrado no histórico."
        );
    }

    private static void testarCurtirVideo() throws Exception {

        CurtidaController curtidaController = new CurtidaController();

        int antes = curtidaController.contarCurtidas(videoTeste.getIdVideo());

        curtidaController.curtirVideo(videoTeste.getIdVideo());

        int depois = curtidaController.contarCurtidas(videoTeste.getIdVideo());

        validar(
                depois == antes + 1,
                "A quantidade de curtidas não aumentou corretamente. Antes: "
                        + antes + ", depois: " + depois
        );
    }

    private static void testarCurtidaDuplicada() throws Exception {

        CurtidaController curtidaController = new CurtidaController();

        int antes = curtidaController.contarCurtidas(videoTeste.getIdVideo());

        curtidaController.curtirVideo(videoTeste.getIdVideo());

        int depois = curtidaController.contarCurtidas(videoTeste.getIdVideo());

        validar(
                depois == antes,
                "A curtida duplicada foi registrada indevidamente. Antes: "
                        + antes + ", depois: " + depois
        );
    }

    private static void testarDescurtirVideo() throws Exception {

        CurtidaController curtidaController = new CurtidaController();

        int antes = curtidaController.contarCurtidas(videoTeste.getIdVideo());

        curtidaController.descurtirVideo(videoTeste.getIdVideo());

        int depois = curtidaController.contarCurtidas(videoTeste.getIdVideo());

        validar(
                depois == antes - 1,
                "A curtida não foi removida corretamente. Antes: "
                        + antes + ", depois: " + depois
        );
    }

    private static void testarCriarPlaylist() throws Exception {

        ListaReproducaoController listaController = new ListaReproducaoController();

        String nomePlaylist = "Playlist Teste Integracao";
        String descricao = "Playlist criada por teste automatizado.";

        listaController.criarLista(nomePlaylist, descricao);

        idPlaylistTeste = buscarIdPlaylistPorNome(
                usuarioTeste.getIdUsuario(),
                nomePlaylist
        );

        validar(
                idPlaylistTeste > 0,
                "A playlist não foi localizada no banco após a criação."
        );

        System.out.println("        Playlist usada nos testes: ID " + idPlaylistTeste);
    }

    private static void testarEditarPlaylist() throws Exception {

        ListaReproducaoController listaController = new ListaReproducaoController();

        String novoNome = "Playlist Teste Editada";
        String novaDescricao = "Descrição editada por teste automatizado.";

        listaController.editarLista(
                idPlaylistTeste,
                novoNome,
                novaDescricao
        );

        String nomeBanco = buscarNomePlaylistPorId(idPlaylistTeste);

        validar(
                novoNome.equals(nomeBanco),
                "O nome da playlist não foi atualizado corretamente."
        );
    }

    private static void testarAdicionarVideoPlaylist() throws Exception {

        ListaReproducaoController listaController = new ListaReproducaoController();

        listaController.adicionarVideoNaLista(
                idPlaylistTeste,
                videoTeste.getIdVideo()
        );

        boolean existeVinculo = verificarVinculoPlaylistVideo(
                idPlaylistTeste,
                videoTeste.getIdVideo()
        );

        validar(
                existeVinculo,
                "O vínculo entre playlist e vídeo não foi criado no banco."
        );
    }

    private static void testarListarVideosPlaylist() throws Exception {

        ListaReproducaoController listaController = new ListaReproducaoController();

        ArrayList<Video> videos = listaController.listarVideosDaLista(idPlaylistTeste);

        validar(
                videos != null,
                "A listagem de vídeos da playlist retornou null."
        );

        validar(
                !videos.isEmpty(),
                "A playlist não retornou vídeos após adicionar um vídeo."
        );

        boolean encontrouVideo = false;

        for (Video video : videos) {
            if (video.getIdVideo() == videoTeste.getIdVideo()) {
                encontrouVideo = true;
                break;
            }
        }

        validar(
                encontrouVideo,
                "O vídeo adicionado não apareceu na listagem da playlist."
        );
    }

    private static void testarRemoverVideoPlaylist() throws Exception {

        ListaReproducaoController listaController = new ListaReproducaoController();

        listaController.removerVideoDaLista(
                idPlaylistTeste,
                videoTeste.getIdVideo()
        );

        boolean existeVinculo = verificarVinculoPlaylistVideo(
                idPlaylistTeste,
                videoTeste.getIdVideo()
        );

        validar(
                !existeVinculo,
                "O vínculo entre playlist e vídeo não foi removido."
        );
    }

    private static void testarExcluirPlaylist() throws Exception {

        ListaReproducaoController listaController = new ListaReproducaoController();

        listaController.excluirLista(idPlaylistTeste);

        boolean playlistExiste = verificarPlaylistExiste(idPlaylistTeste);

        validar(
                !playlistExiste,
                "A playlist ainda existe no banco após exclusão."
        );

        idPlaylistTeste = -1;
    }

    private static Usuario buscarUsuarioPorEmail(String email) throws SQLException {

        String sql = """
                SELECT id_usuario, nome, email, senha
                FROM tb_usuario
                WHERE email = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));

                return usuario;
            }
        }

        return null;
    }

    private static int buscarIdPlaylistPorNome(
            int idUsuario,
            String nomePlaylist
    ) throws SQLException {

        String sql = """
                SELECT id_lista
                FROM tb_lista
                WHERE id_usuario = ?
                AND nome = ?
                ORDER BY id_lista DESC
                LIMIT 1
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, nomePlaylist);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_lista");
            }
        }

        return -1;
    }

    private static String buscarNomePlaylistPorId(int idPlaylist) throws SQLException {

        String sql = """
                SELECT nome
                FROM tb_lista
                WHERE id_lista = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idPlaylist);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            }
        }

        return null;
    }

    private static boolean verificarVinculoPlaylistVideo(
            int idPlaylist,
            int idVideo
    ) throws SQLException {

        String sql = """
                SELECT 1
                FROM tb_lista_video
                WHERE id_lista = ?
                AND id_video = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idPlaylist);
            stmt.setInt(2, idVideo);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
    }

    private static boolean verificarPlaylistExiste(int idPlaylist) throws SQLException {

        String sql = """
                SELECT 1
                FROM tb_lista
                WHERE id_lista = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idPlaylist);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
    }

    private static void executarLimpezaFinal() {

        imprimirSecao("LIMPEZA FINAL");

        if (usuarioTeste == null || usuarioTeste.getIdUsuario() <= 0) {
            System.out.println("Nenhum usuário temporário para remover.");
            return;
        }

        String sql = """
                DELETE FROM tb_usuario
                WHERE id_usuario = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuarioTeste.getIdUsuario());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Usuário temporário removido com sucesso.");
            } else {
                System.out.println("Usuário temporário não foi encontrado para remoção.");
            }

        } catch (SQLException e) {
            System.out.println("Falha ao remover usuário temporário.");
            e.printStackTrace();
        }
    }

    private static void executarTeste(
            String nomeTeste,
            TesteExecutavel teste
    ) {

        totalTestes++;

        imprimirSecao("TESTE " + totalTestes + ": " + nomeTeste);

        try {
            teste.executar();

            totalAprovados++;

            System.out.println("RESULTADO: APROVADO");

        } catch (Exception e) {
            totalFalhas++;

            System.out.println("RESULTADO: FALHOU");
            System.out.println("MOTIVO: " + e.getMessage());

            System.out.println("DETALHES TÉCNICOS:");
            e.printStackTrace();
        }
    }

    private static void validar(
            boolean condicao,
            String mensagemErro
    ) {

        if (!condicao) {
            throw new IllegalStateException(mensagemErro);
        }
    }

    private static void imprimirCabecalhoGeral() {
        System.out.println("=================================================");
        System.out.println(" TESTE DE INTEGRACAO DO SISTEMA FEItv");
        System.out.println("=================================================");
        System.out.println("Este teste valida os principais fluxos do backend.");
        System.out.println("A interface Swing não é testada automaticamente aqui.");
    }

    private static void imprimirSecao(String titulo) {
        System.out.println();
        System.out.println("-------------------------------------------------");
        System.out.println(titulo);
        System.out.println("-------------------------------------------------");
    }

    private static void imprimirResumoFinal() {
        System.out.println();
        System.out.println("=================================================");
        System.out.println(" RESUMO FINAL DOS TESTES");
        System.out.println("=================================================");
        System.out.println("Total de testes: " + totalTestes);
        System.out.println("Aprovados: " + totalAprovados);
        System.out.println("Falhas: " + totalFalhas);

        if (totalFalhas == 0) {
            System.out.println("STATUS FINAL: SISTEMA APROVADO NOS TESTES DE INTEGRACAO.");
        } else {
            System.out.println("STATUS FINAL: SISTEMA POSSUI FALHAS A CORRIGIR.");
        }

        System.out.println("=================================================");
    }

    @FunctionalInterface
    private interface TesteExecutavel {
        void executar() throws Exception;
    }
}