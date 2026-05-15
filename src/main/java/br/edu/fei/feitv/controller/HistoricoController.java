package br.edu.fei.feitv.controller;

import br.edu.fei.feitv.dao.HistoricoDAO;
import br.edu.fei.feitv.model.Usuario;
import br.edu.fei.feitv.session.SessaoUsuario;

import java.util.ArrayList;

/**
 * Controller responsável por intermediar a tela e o DAO do histórico.
 */
public class HistoricoController {

    private HistoricoDAO historicoDAO;

    public HistoricoController() {
        this.historicoDAO = new HistoricoDAO();
    }

    public void registrarBusca(String termoBusca) {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        if (termoBusca == null || termoBusca.isBlank()) {
            return;
        }

        historicoDAO.registrarBusca(usuario, termoBusca);
    }

    public ArrayList<String> listarHistorico() {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        return historicoDAO.listarHistoricoPorUsuario(usuario);
    }

    public void limparHistorico() {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        historicoDAO.limparHistorico(usuario);
    }
}