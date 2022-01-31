package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler {
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
            if ("/end".equals(message)) {
                break;
            }
            chatServer.broadcast(message);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {
        while (true) {
            try {
                final String message = in.readUTF(); // auth login pass
                if (message.startsWith("/auth")) {
                    // MAKE CHECK METHOD TO CONFIRM BOTH LOGIN AND PASSWORD HAVE BEEN RECEIVED, OTHERWISE ARRAYINDEXOUTOFBOUNDARYEXCEPTION
                    final String[] split = message.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    String nick = chatServer.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("The user is already logged in");
                            continue;
                        }
                        sendMessage("/authok " + nick);
                        this.nick = nick;
                        chatServer.broadcast("The user " + nick + "has entered thr chat");
                        chatServer.subscribe(this);
                        break;
                    } else {
                        sendMessage("Wrong login and password");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            if (message.startsWith("/")) {
                out.writeUTF(message);
            } else {
                out.writeUTF(nick + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
