package br.edu.fei.feitv.model;

public abstract class Video {

    private int idVideo;
    private String titulo;
    private String descricao;
    private String genero;
    private int anoLancamento;

    public Video() {
    }

    public Video(int idVideo, String titulo, String descricao,
                 String genero, int anoLancamento) {

        this.idVideo = idVideo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
    }

    public int getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(int idVideo) {
        this.idVideo = idVideo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }
}