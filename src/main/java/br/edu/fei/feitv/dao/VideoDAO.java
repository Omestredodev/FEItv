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

public class VideoDAO {

    public ArrayList<Video> buscarVideosPorNome(String nome) {

        ArrayList<Video> videos = new ArrayList<>();

        String sql = """
                SELECT * FROM tb_video
                WHERE LOWER(titulo) LIKE LOWER(?)
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String tipo = rs.getString("tipo_video");

                Video video;

                if (tipo.equals("FILME")) {

                    video = new Filme();

                } else {

                    video = new Serie();
                }

                video.setIdVideo(rs.getInt("id_video"));
                video.setTitulo(rs.getString("titulo"));
                video.setDescricao(rs.getString("descricao"));
                video.setGenero(rs.getString("genero"));
                video.setAnoLancamento(rs.getInt("ano_lancamento"));

                videos.add(video);
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar vídeos!");
            e.printStackTrace();
        }

        return videos;
    }
}