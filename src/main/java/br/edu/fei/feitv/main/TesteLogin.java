package br.edu.fei.feitv.main;

import br.edu.fei.feitv.dao.UsuarioDAO;
import br.edu.fei.feitv.model.Usuario;

public class TesteLogin {

    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario = usuarioDAO.validarLogin(
                "guilherme@teste.com",
                "123456"
        );

        if (usuario != null) {
            System.out.println("Login realizado com sucesso!");
            System.out.println("Usuario: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
        } else {
            System.out.println("Email ou senha inválidos.");
        }
    }
}