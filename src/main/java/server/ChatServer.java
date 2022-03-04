package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.stream.Collectors;



public class ChatServer {
    private final Map<String, ClientHandler> clients;
    private final AuthService authService;
    private ExecutorService executorService;
    // Добавил логгеры (info, warning) на основные события в file, прописанный в log4j2.xml
    private final Logger logger = Logger.getLogger("file");




    public ChatServer() {
        this.clients = new HashMap<>();
        this.authService = new DBAuthService();
    }

    // Предполагаю, что обработка потоков должна проходить здесь
    public void start() {
        //executorService = Executors.newCachedThreadPool();
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true)  {
                System.out.println("Waiting for connection...");
                logger.info("Server is on. Waiting for connection...");
                final Socket socket = serverSocket.accept();
               new ClientHandler(socket, this);
                System.out.printf("The client %s has connected.", socket.getInetAddress().getHostName());
                logger.info("The client has connected");
                //executorService.execute(new ClientHandler(socket, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           // executorService.shutdownNow();
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
        logger.info("The user " + client + " has joined");
    }

    public void unsubscribe (ClientHandler client) {
        clients.remove(client.getNick(), client);
        broadcastClientList();
        logger.info("The user" + client + " has left");
    }

    public void broadcast (String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
            logger.info("The client has broadcasted the message");
        }
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message ) {
        final ClientHandler clientTo = clients.get(nickTo);
        if (clientTo != null) {
            clientTo.sendMessage("From " + from.getNick() + ": " + message);
            from.sendMessage("To " + nickTo + ": " + message);
            logger.info("Client" + from + " sent private message to " + nickTo);
            return;
        }
        from.sendMessage("There is no user in the chat with nick " + nickTo);
        logger.warning("The user does not exist");
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
