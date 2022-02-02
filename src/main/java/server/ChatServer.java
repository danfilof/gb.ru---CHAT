package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {


    private final Map<String, ClientHandler> clients;
    private final AuthService authService;

    public ChatServer() {
        clients = new HashMap<>();
        authService = new InMemoryAuthService();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true)  {
                System.out.println("Waiting for connection...");
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                System.out.printf("The client %s has connected.", socket.getInetAddress().getHostName());
                System.out.println("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);

    }

    public void subscribe(ClientHandler client) {
        clients.put(client.getNick(), client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.getNick(), client);
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }
    // Отправка приватных сообщений пользователю
    public void privateSend(ClientHandler FROM, String message) {
        final String TO = message.split(" ")[1];
        for (ClientHandler client : clients.values()) {
            if (client.getNick().equals(TO)) {
                // Выводим приватное сообщение и стираем у него первые 7 символов, дабы избежать повторения
                client.sendMessage("/w from " + FROM.getNick() + ": " + message.substring(7));
                break;
            }
        }
        // Написать пользователю, что он отправил приватное сообщение
        FROM.sendMessage("/w to " + TO + ": " + message.substring(7));
    }
}
