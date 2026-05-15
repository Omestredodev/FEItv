package br.edu.fei.feitv.dao;

import br.edu.fei.feitv.database.ConnectionFactory;
import br.edu.fei.feitv.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO responsável por registrar e remover curtidas dos vídeos.
 */
public class CurtidaDAO {

    public void curtirVideo(Usuario usuario, int idVideo) {

        String sql = """
                INSERT INTO tb_curtida
                (id_usuario, id_video)
                VALUES (?, ?)
                ON CONFLICT (id_usuario, id_video) DO NOTHING
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setInt(2, idVideo);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao curtir vídeo!");
            e.printStackTrace();
        }
    }

    public void descurtirVideo(Usuario usuario, int idVideo) {

        String sql = """
                DELETE FROM tb_curtida
                WHERE id_usuario = ?
                AND id_video = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setInt(2, idVideo);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao descurtir vídeo!");
            e.printStackTrace();
        }
    }

    public int contarCurtidas(int idVideo) {

        String sql = """
                SELECT COUNT(*) AS total
                FROM tb_curtida
                WHERE id_video = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idVideo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar curtidas!");
            e.printStackTrace();
        }

        return 0;
    }
}