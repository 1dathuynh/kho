package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/quanlykhohang";
        String username = "root";
        String password = "0935542587";

        try {
            System.out.println("Connecting to MySQL database...");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected successfully!");

            // Close the connection
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.err.println("Error connecting to MySQL database:");
            e.printStackTrace();
        }
    }
}
