package br.edu.fei.feitv.controller;

import br.edu.fei.feitv.dao.ListaReproducaoDAO;
import br.edu.fei.feitv.model.ListaReproducao;
import br.edu.fei.feitv.model.Usuario;
import br.edu.fei.feitv.model.Video;
import br.edu.fei.feitv.session.SessaoUsuario;

import java.util.ArrayList;

/**
 * Controller responsável pelas regras e fluxos das listas de reprodução.
 */
public class ListaReproducaoController {

    private ListaReproducaoDAO listaDAO;

    public ListaReproducaoController() {
        this.listaDAO = new ListaReproducaoDAO();
    }

    public void criarLista(String nome, String descricao) {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        ListaReproducao lista = new ListaReproducao();

        lista.setNome(nome);
        lista.setDescricao(descricao);
        lista.setUsuario(usuario);

        listaDAO.criarLista(lista);
    }

    public ArrayList<ListaReproducao> listarListasDoUsuario() {

        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            throw new IllegalStateException("Nenhum usuário logado.");
        }

        return listaDAO.listarPorUsuario(usuario);
    }

    public void editarLista(int idLista, String nome, String descricao) {
        listaDAO.editarLista(idLista, nome, descricao);
    }

    public void excluirLista(int idLista) {
        listaDAO.excluirLista(idLista);
    }

    public void adicionarVideoNaLista(int idLista, int idVideo) {
        listaDAO.adicionarVideoNaLista(idLista, idVideo);
    }

    public void removerVideoDaLista(int idLista, int idVideo) {
        listaDAO.removerVideoDaLista(idLista, idVideo);
    }

    public ArrayList<Video> listarVideosDaLista(int idLista) {
        return listaDAO.listarVideosDaLista(idLista);
    }
}