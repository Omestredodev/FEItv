package br.edu.fei.feitv.model;

import java.util.ArrayList;

/**
 * Representa uma lista de reprodução criada por um usuário.
 * Uma lista pertence a um usuário e pode conter vários vídeos.
 */
public class ListaReproducao {

    private int idLista;
    private String nome;
    private String descricao;
    private Usuario usuario;
    private ArrayList<Video> videos;

    public ListaReproducao() {
        this.videos = new ArrayList<>();
    }

    public ListaReproducao(int idLista, String nome, String descricao, Usuario usuario) {
        this.idLista = idLista;
        this.nome = nome;
        this.descricao = descricao;
        this.usuario = usuario;
        this.videos = new ArrayList<>();
    }

    public void adicionarVideo(Video video) {
        this.videos.add(video);
    }

    public void removerVideo(Video video) {
        this.videos.remove(video);
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    /**
     * Usado para exibir a lista de forma legível em componentes visuais.
     */
    @Override
    public String toString() {
        return idLista + " - " + nome;
    }
}