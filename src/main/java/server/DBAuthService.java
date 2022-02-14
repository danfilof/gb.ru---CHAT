package server;

import java.sql.*;

public class DBAuthService implements AuthService {
    private Connection connection;
    private Statement statement;

    public DBAuthService() {
    }

    public static void main(String[] args) {
        DBAuthService dbAuthService = new DBAuthService();
        try {

        } finally {
            dbAuthService.disconnect();
        }

    }

    // 1. Добавить в сетевой чат авторизацию через базу данных SQLite.
    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        connect();
        String nick = checkAuth(login, password);
        System.out.println(nick);
        return nick;

    }

    public String checkAuth(String log, String pass)  {
        // NullPointerException!!! as connection = 0;
        try (PreparedStatement ps = connection.prepareStatement("select nick from users where login = ? and pass = ?")){
            ps.setString(1, log);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            String user_nick = rs.getString("nick");
            return user_nick;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2.*Добавить в сетевой чат возможность смены ника.
    public void updateNickByID(String newNick,int id)  {
        try (PreparedStatement un = connection.prepareStatement("update users set nick = ? where id = ?")) {
            un.setString(1,newNick);
            un.setInt(2, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Возможнось изменить ник по login
    public void updateNickByLogin(String newNick, String login)  {
        try (PreparedStatement un = connection.prepareStatement("update users set nick = ? where login = ?")) {
            un.setString(1,newNick);
            un.setString(2, login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод, позволяющий прочитать таблицу users
    public void readTable() {
        try (PreparedStatement ps = connection.prepareStatement("select * from users")) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("Login: " + rs.getString("login")+" || Password: " + rs.getString("pass") + " || Nick: " + rs.getString("nick"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:gb_chat_db.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void batchInsert() {
        try (PreparedStatement ps = connection.prepareStatement("insert into users (login, pass, nick) values (?, ?, ?)")) {
            for (int i = 0; i < 10; i++) {
                ps.setString(1, "login" + i);
                ps.setString(2,"pass" + i);
                ps.setString(3, "nick" + i);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(String login, String pass, String nick) {
        try(PreparedStatement ps = connection.prepareStatement("insert into users (login, pass, nick) values (?, ?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, nick);
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