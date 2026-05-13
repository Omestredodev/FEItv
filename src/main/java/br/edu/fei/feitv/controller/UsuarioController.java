package br.edu.fei.feitv.controller;

import br.edu.fei.feitv.dao.UsuarioDAO;
import br.edu.fei.feitv.model.Usuario;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void cadastrarUsuario(
            String nome,
            String email,
            String senha
    ) {

        Usuario usuario = new Usuario();

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        usuarioDAO.cadastrarUsuario(usuario);
    }

    /*
    public boolean validarLogin(
            String email,
            String senha
    ) {

        return usuarioDAO.validarLogin(email, senha);
    }*/
    //refatoração
    public Usuario validarLogin(
            String email,
            String senha
    ) {

        return usuarioDAO.validarLogin(email, senha);
    }
}