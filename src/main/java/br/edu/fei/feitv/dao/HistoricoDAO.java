package br.edu.fei.feitv.dao;

import br.edu.fei.feitv.database.ConnectionFactory;
import br.edu.fei.feitv.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsável por registrar e consultar o histórico de buscas dos usuários.
 */
public class HistoricoDAO {

    public void registrarBusca(Usuario usuario, String termoBusca) {

        String sql = """
                INSERT INTO tb_historico_busca
                (id_usuario, termo_busca)
                VALUES (?, ?)
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, termoBusca);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao registrar histórico de busca!");
            e.printStackTrace();
        }
    }

    public ArrayList<String> listarHistoricoPorUsuario(Usuario usuario) {

        ArrayList<String> historico = new ArrayList<>();

        String sql = """
                SELECT termo_busca, data_busca
                FROM tb_historico_busca
                WHERE id_usuario = ?
                ORDER BY data_busca DESC
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getIdUsuario());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String linha =
                        "Busca: "
                        + rs.getString("termo_busca")
                        + " | Data: "
                        + rs.getTimestamp("data_busca");

                historico.add(linha);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar histórico!");
            e.printStackTrace();
        }

        return historico;
    }

    public void limparHistorico(Usuario usuario) {

        String sql = """
                DELETE FROM tb_historico_busca
                WHERE id_usuario = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getIdUsuario());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao limpar histórico!");
            e.printStackTrace();
        }
    }
}