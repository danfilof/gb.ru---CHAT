package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChatServer {


    private final Map<String, ClientHandler> clients;
    private final AuthService authService;
    private ExecutorService executorService;

    public ChatServer() {
        this.clients = new HashMap<>();
        this.authService = new DBAuthService();
    }

    public void start() {
        executorService = Executors.newCachedThreadPool();
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true)  {
                System.out.println("Waiting for connection...");
                final Socket socket = serverSocket.accept();
//                new ClientHandler(socket, this);
                System.out.printf("The client %s has connected.", socket.getInetAddress().getHostName());
                executorService.execute((Runnable) new ClientHandler(socket, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
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
