package server;

import java.sql.*;

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
            connection = DriverManager.getConnection("jdbc:sqlite:gb_chat_db.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBAuthService dbAuthService = new DBAuthService();
        try {
            dbAuthService.connect();
            // Заполнил таблицу users login и pass, по аналогии с UserData из InMemoryAuthService
           // dbAuthService.batchInsert();
        } finally {
            dbAuthService.disconnect();
        }

    }

    private void batchInsert() {
        try (PreparedStatement ps = connection.prepareStatement("insert into users (login, pass) values (?, ?)")) {
            for (int i = 0; i < 10; i++) {
                ps.setString(1, "login" + i);
                ps.setString(2,"pass" + i);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(String login, String pass) {
        try(PreparedStatement ps = connection.prepareStatement("insert into users (login, pass) values (?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.executeUpdate();
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