package br.edu.fei.feitv.controller;

import br.edu.fei.feitv.dao.CurtidaDAO;
import br.edu.fei.feitv.model.Usuario;
import br.edu.fei.feitv.session.SessaoUsuario;

/**
 * Controller responsável pelas ações de curtir e descurtir vídeos.
 */
public class CurtidaController {

    private CurtidaDAO curtidaDAO;

    public CurtidaController() {
        this.curtidaDAO = new CurtidaDAO();
    }

    public void curtirVideo(int idVideo) {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        curtidaDAO.curtirVideo(usuario, idVideo);
    }

    public void descurtirVideo(int idVideo) {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        curtidaDAO.descurtirVideo(usuario, idVideo);
    }

    public int contarCurtidas(int idVideo) {
        return curtidaDAO.contarCurtidas(idVideo);
    }
}