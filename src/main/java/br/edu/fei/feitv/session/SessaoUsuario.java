package br.edu.fei.feitv.session;

import br.edu.fei.feitv.model.Usuario;

public class SessaoUsuario {

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }
}