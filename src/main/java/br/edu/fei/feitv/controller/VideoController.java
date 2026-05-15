package br.edu.fei.feitv.controller;

import br.edu.fei.feitv.dao.VideoDAO;
import br.edu.fei.feitv.model.Video;

import java.util.ArrayList;

/**
 * Controller responsável por intermediar a View e o DAO de vídeos.
 */
public class VideoController {

    private VideoDAO videoDAO;

    public VideoController() {
        this.videoDAO = new VideoDAO();
    }

    public ArrayList<Video> buscarVideos(String nome) {
        return videoDAO.buscarVideosPorNome(nome);
    }

    public ArrayList<Video> listarTodosVideos() {
        return videoDAO.listarTodosVideos();
    }
}