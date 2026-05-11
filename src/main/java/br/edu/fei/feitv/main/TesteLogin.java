package br.edu.fei.feitv.main;

import br.edu.fei.feitv.dao.UsuarioDAO;

public class TesteLogin {

    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        boolean loginValido = usuarioDAO.validarLogin(
                "guilherme@teste.com",
                "123456"
        );

        if (loginValido) {
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("Email ou senha inválidos.");
        }
    }
}