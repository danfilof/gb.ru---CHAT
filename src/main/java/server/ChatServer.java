package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

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

    public void subscribe (ClientHandler client) {
        clients.put(client.getNick(), client);
        broadcastClientList();
    }

    public void unsubscribe (ClientHandler client) {
        clients.remove(client.getNick(), client);
        broadcastClientList();
    }

    public void broadcast (String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message ) {
        final ClientHandler clientTo = clients.get(nickTo);
        if (clientTo != null) {
            clientTo.sendMessage("From " + from.getNick() + ": " + message);
            from.sendMessage("To " + nickTo + ": " + message);
            return;
        }
        from.sendMessage("There is no user in the chat with nick " + nickTo);
    }

public void broadcastClientList() {
        final String message = clients.values().stream()
                .map(ClientHandler::getNick)
                .collect(Collectors.joining(" "));
        broadcast(Command.CLIENTS, message);
}

    private void broadcast(Command command, String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(command, message);
        }
    }
}
