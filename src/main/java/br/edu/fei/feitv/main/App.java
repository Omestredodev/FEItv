package br.edu.fei.feitv.main;

import br.edu.fei.feitv.view.TelaLogin;

/**
 * Classe principal da aplicação FEItv.
 * Responsável por iniciar o sistema abrindo a tela de login.
 */
public class App {

    public static void main(String[] args) {

        TelaLogin telaLogin = new TelaLogin();
        telaLogin.setVisible(true);
    }
}