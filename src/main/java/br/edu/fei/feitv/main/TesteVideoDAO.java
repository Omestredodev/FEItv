package br.edu.fei.feitv.main;

import br.edu.fei.feitv.dao.VideoDAO;
import br.edu.fei.feitv.model.Video;

import java.util.ArrayList;

public class TesteVideoDAO {

    public static void main(String[] args) {

        VideoDAO videoDAO = new VideoDAO();

        ArrayList<Video> videos =
                videoDAO.buscarVideosPorNome("inter");

        for (Video video : videos) {

            System.out.println("Título: " + video.getTitulo());
            System.out.println("Gênero: " + video.getGenero());
            System.out.println("Ano: " + video.getAnoLancamento());

            System.out.println("-----------------------");
        }
    }
}