package br.edu.fei.feitv.dao;

import br.edu.fei.feitv.database.ConnectionFactory;
import br.edu.fei.feitv.model.Filme;
import br.edu.fei.feitv.model.Serie;
import br.edu.fei.feitv.model.Video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsável por operações de banco relacionadas aos vídeos.
 */
public class VideoDAO {

    /**
     * Busca vídeos por nome usando busca parcial e ignorando maiúsculas/minúsculas.
     */
    public ArrayList<Video> buscarVideosPorNome(String nome) {

        ArrayList<Video> videos = new ArrayList<>();

        String sql = """
                SELECT * FROM tb_video
                WHERE LOWER(titulo) LIKE LOWER(?)
                ORDER BY titulo
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                videos.add(criarVideoAPartirDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar vídeos!");
            e.printStackTrace();
        }

        return videos;
    }

    /**
     * Lista todos os vídeos disponíveis no banco.
     * Usado para exibir os IDs disponíveis na tela de favoritos.
     */
    public ArrayList<Video> listarTodosVideos() {

        ArrayList<Video> videos = new ArrayList<>();

        String sql = """
                SELECT * FROM tb_video
                ORDER BY titulo
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                videos.add(criarVideoAPartirDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar vídeos!");
            e.printStackTrace();
        }

        return videos;
    }

    /**
     * Converte um registro SQL em objeto Filme ou Série.
     * Isso mantém o uso correto de herança e polimorfismo.
     */
    private Video criarVideoAPartirDoResultSet(ResultSet rs) throws SQLException {

        String tipo = rs.getString("tipo_video");

        Video video;

        if ("FILME".equals(tipo)) {
            video = new Filme();
        } else {
            video = new Serie();
        }

        video.setIdVideo(rs.getInt("id_video"));
        video.setTitulo(rs.getString("titulo"));
        video.setDescricao(rs.getString("descricao"));
        video.setGenero(rs.getString("genero"));
        video.setAnoLancamento(rs.getInt("ano_lancamento"));

        return video;
    }
}