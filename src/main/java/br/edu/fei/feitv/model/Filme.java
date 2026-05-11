package br.edu.fei.feitv.model;

public class Filme extends Video {

    public Filme() {
        super();
    }

    public Filme(int idVideo, String titulo, String descricao,
                 String genero, int anoLancamento) {

        super(idVideo, titulo, descricao, genero, anoLancamento);
    }
}