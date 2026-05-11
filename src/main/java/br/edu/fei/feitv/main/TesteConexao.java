package br.edu.fei.feitv.main;

import br.edu.fei.feitv.database.ConnectionFactory;
import java.sql.Connection;

public class TesteConexao {

    public static void main(String[] args) {

        Connection conn = ConnectionFactory.conectar();

        if (conn != null) {
            System.out.println("Conexão realizada com sucesso!");

        } else {
            System.out.println("Erro ao conectar!");
        }
    }
}