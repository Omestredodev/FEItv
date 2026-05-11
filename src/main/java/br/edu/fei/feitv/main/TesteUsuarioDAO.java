package br.edu.fei.feitv.main;

import br.edu.fei.feitv.dao.UsuarioDAO;
import br.edu.fei.feitv.model.Usuario;

public class TesteUsuarioDAO {

    public static void main(String[] args) {

        Usuario usuario = new Usuario();
        usuario.setNome("Guilherme");
        usuario.setEmail("guilherme@teste.com");
        usuario.setSenha("123456");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.cadastrarUsuario(usuario);
    }
}