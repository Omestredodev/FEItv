package br.edu.fei.feitv.dao;

import br.edu.fei.feitv.database.ConnectionFactory;
import br.edu.fei.feitv.model.Filme;
import br.edu.fei.feitv.model.ListaReproducao;
import br.edu.fei.feitv.model.Serie;
import br.edu.fei.feitv.model.Usuario;
import br.edu.fei.feitv.model.Video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsável pela persistência das listas de reprodução e seus vídeos.
 */
public class ListaReproducaoDAO {

    public void criarLista(ListaReproducao lista) {

        String sql = """
                INSERT INTO tb_lista
                (id_usuario, nome, descricao)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, lista.getUsuario().getIdUsuario());
            stmt.setString(2, lista.getNome());
            stmt.setString(3, lista.getDescricao());

            stmt.executeUpdate();

            System.out.println("Lista criada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar lista!");
            e.printStackTrace();
        }
    }

    public ArrayList<ListaReproducao> listarPorUsuario(Usuario usuario) {

        ArrayList<ListaReproducao> listas = new ArrayList<>();

        String sql = """
                SELECT * FROM tb_lista
                WHERE id_usuario = ?
                ORDER BY id_lista
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getIdUsuario());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                ListaReproducao lista = new ListaReproducao();

                lista.setIdLista(rs.getInt("id_lista"));
                lista.setNome(rs.getString("nome"));
                lista.setDescricao(rs.getString("descricao"));
                lista.setUsuario(usuario);

                listas.add(lista);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar listas!");
            e.printStackTrace();
        }

        return listas;
    }

    public void editarLista(int idLista, String nome, String descricao) {

        String sql = """
                UPDATE tb_lista
                SET nome = ?,
                    descricao = ?
                WHERE id_lista = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setInt(3, idLista);

            stmt.executeUpdate();

            System.out.println("Lista editada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao editar lista!");
            e.printStackTrace();
        }
    }

    public void excluirLista(int idLista) {

        String sql = """
                DELETE FROM tb_lista
                WHERE id_lista = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idLista);

            stmt.executeUpdate();

            System.out.println("Lista excluída com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir lista!");
            e.printStackTrace();
        }
    }

    public void adicionarVideoNaLista(int idLista, int idVideo) {

        String sql = """
                INSERT INTO tb_lista_video
                (id_lista, id_video)
                VALUES (?, ?)
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idLista);
            stmt.setInt(2, idVideo);

            stmt.executeUpdate();

            System.out.println("Vídeo adicionado à lista!");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar vídeo à lista!");
            e.printStackTrace();
        }
    }

    public void removerVideoDaLista(int idLista, int idVideo) {

        String sql = """
                DELETE FROM tb_lista_video
                WHERE id_lista = ?
                AND id_video = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idLista);
            stmt.setInt(2, idVideo);

            stmt.executeUpdate();

            System.out.println("Vídeo removido da lista!");

        } catch (SQLException e) {
            System.out.println("Erro ao remover vídeo da lista!");
            e.printStackTrace();
        }
    }

    public ArrayList<Video> listarVideosDaLista(int idLista) {

        ArrayList<Video> videos = new ArrayList<>();

        String sql = """
                SELECT v.*
                FROM tb_video v
                INNER JOIN tb_lista_video lv
                ON v.id_video = lv.id_video
                WHERE lv.id_lista = ?
                ORDER BY v.titulo
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idLista);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String tipo = rs.getString("tipo_video");

                Video video;

                if ("FILME".equals(tipo)) {
                    video = new Filme();
                } else {
                    video = new Serie();
                }

                video.setIdVideo(rs.getInt("id_video"));
                video.setTitulo(rs.getString("titulo"));
                video.setGenero(rs.getString("genero"));
                video.setAnoLancamento(rs.getInt("ano_lancamento"));
                video.setDescricao(rs.getString("descricao"));

                videos.add(video);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar vídeos da lista!");
            e.printStackTrace();
        }

        return videos;
    }
}