package br.edu.fei.feitv.dao;

import br.edu.fei.feitv.database.ConnectionFactory;
import br.edu.fei.feitv.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {

    public void cadastrarUsuario(Usuario usuario) {

        String sql = """
                INSERT INTO tb_usuario
                (nome, email, senha)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            stmt.executeUpdate();

            System.out.println("Usuário cadastrado com sucesso!");

        } catch (SQLException e) {

            System.out.println("Erro ao cadastrar usuário!");
            e.printStackTrace();
        }
    }

    public boolean validarLogin(String email, String senha) {

        String sql = """
                SELECT * FROM tb_usuario
                WHERE email = ?
                AND senha = ?
                """;

        try (
                Connection conn = ConnectionFactory.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            var rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            System.out.println("Erro ao validar login!");
            e.printStackTrace();

            return false;
        }
    }

}