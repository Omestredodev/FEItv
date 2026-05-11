package br.edu.fei.feitv.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/feitv";

    private static final String USER = "postgres";

    private static final String PASSWORD = "admin";

    public static Connection conectar() {

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {

            System.out.println("Erro ao conectar com o banco!");
            e.printStackTrace();

            return null;
        }
    }
}