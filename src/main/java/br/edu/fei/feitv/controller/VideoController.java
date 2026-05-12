package br.edu.fei.feitv.controller;

import br.edu.fei.feitv.dao.VideoDAO;
import br.edu.fei.feitv.model.Video;

import java.util.ArrayList;

public class VideoController {

    private VideoDAO videoDAO;

    public VideoController() {

        this.videoDAO = new VideoDAO();
    }

    public ArrayList<Video> buscarVideos(String nome) {

        return videoDAO.buscarVideosPorNome(nome);
    }
}