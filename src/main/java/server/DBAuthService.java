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
            dbAuthService.connect();
            dbAuthService.readTable();
            // Заполнил таблицу users login, pass и nick по аналогии с UserData из InMemoryAuthService
            // dbAuthService.batchInsert();
        } finally {
            dbAuthService.disconnect();
        }

    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        String nick = "";

        try (PreparedStatement lg = connection.prepareStatement("select id from users where login = ?")) {
            lg.setString(1,login);
            ResultSet rs0 = lg.executeQuery();

            try (PreparedStatement pass = connection.prepareStatement("select id from users where pass = ?")) {
                pass.setString(1, password);
                ResultSet rs1 = pass.executeQuery();

                if (rs1.getString("id").equals(rs0.getString("id"))) {
                    System.out.println("Correct login and password");

                    try (PreparedStatement user_nick = connection.prepareStatement("select nick where id = rs1")) {
                        ResultSet rn = user_nick.executeQuery();
                        nick = rn.getString("nick");
                    }
                    return nick;
                } else {
                    System.out.println("Wrong login or password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Возможнось изменить ник
    public void updateNick(String newNick,int id)  {
        try (PreparedStatement un = connection.prepareStatement("update users set nick = ? where id = ?")) {
            un.setString(1,newNick);
            un.setInt(2, id);
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
    private static class UserData {
        private final String login;
        private final String password;
        private final String nick;

        public UserData(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getNick() {
            return nick;
        }
    }
}