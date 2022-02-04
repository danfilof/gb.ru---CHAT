package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static server.Command.*;

public class ClientHandler {

    private final static String COMMAND_PREFIX = "/";


    private final Socket socket;
    private final ChatServer chatServer;
    private final DataInputStream in;
    private final DataOutputStream out;


    private String nick;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.nick = "";
            this.socket = socket;
            this.chatServer = chatServer;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authenticate();
                    readMessage();
                } finally {
                    closeConnection();
                }

            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
                chatServer.unsubscribe(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessage() {
        try {
            while (true) {
                final String message = in.readUTF();
                if (Command.isCommand(message)) {
                    if (getCommandByText(message) == END) {
                        break;
                    }
                    if (getCommandByText(message) == PRIVATE_MESSAGE) {
                        String[] split = message.split(" ");
                        String nickTo = split[1];
                        chatServer.sendMessageToClient(this, nickTo, message.substring(PRIVATE_MESSAGE.getCommand().length() + 2 + nickTo.length()));
                    }
                    continue;
                }
                    chatServer.broadcast(nick + ": " + message);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {

        while (true) {
            // Вводим простую переменную для проверки идентификации
            int clientIsAuth = 0;
            try {
                final String message = in.readUTF(); // auth login pass
                if (getCommandByText(message) == AUTH) {
                    final String[] split = message.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    String nick = chatServer.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("The user is already logged in");
                            continue;
                        }
                        sendMessage(Command.AUTHOK, nick);
                        this.nick = nick;
                        chatServer.broadcast("The user " + nick + " has entered the chat");
                        chatServer.subscribe(this);
                        // Клиент подключился и авторизовался. Значение переменной меняется
                        clientIsAuth = 1;
                        break;
                    } else {
                        sendMessage("Wrong login and password");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Проверка на авторизованность
            int finalClientIsAuth = clientIsAuth;
            // Запускаю цикл для проверки авторизации. И отпраляю его спать на 2 минуты
            new Thread(() -> {
                while (true) {
                    try {
                        wait(120000);
                        // Если знамение переменной не изменилось, значит, авторизация не произошла. Следовательно спустя 120 секунд нужно закрыть сокет.
                        if (finalClientIsAuth == 0) {
                            socket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public void sendMessage(Command command ,String message) {
        try {
            out.writeUTF(command.getCommand() + " " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
                out.writeUTF (LocalTime.now().withSecond(0).withNano(0) + " || " + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
