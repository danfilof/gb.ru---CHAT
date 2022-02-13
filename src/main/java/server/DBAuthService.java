package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAuthService implements AuthService {
    private Connection connection;
    private Statement statement;

    public DBAuthService() {
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        return null;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:java_users.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBAuthService dbAuthService = new DBAuthService();
        try {
            dbAuthService.connect();
            dbAuthService.insert("login0", "pass0");
        } finally {
            dbAuthService.disconnect();
        }

    }

    private void insert(String login, String pass) {
        try {
            statement.executeUpdate("insert into Users (login, pass) values ('" + login + "', '" + pass + "' + ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}