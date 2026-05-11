package br.edu.fei.feitv.model;

public class Serie extends Video {

    public Serie() {
        super();
    }

    public Serie(int idVideo, String titulo, String descricao,
                 String genero, int anoLancamento) {

        super(idVideo, titulo, descricao, genero, anoLancamento);
    }
}