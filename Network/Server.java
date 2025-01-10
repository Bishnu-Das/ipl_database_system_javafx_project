package Network;

import database.AppData;
import database.Player;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private SocketWrapper serverSocketWrapper;
    private AppData appData;
    private List<Player> buyablePlayers;
    ArrayList<ClientHandler> connections;

    public Server() {
        this.appData = new AppData();
        this.buyablePlayers = new ArrayList<>();
        this.connections = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(44444);
            System.out.println("Server started...");
            while (true) {
                Socket socket = serverSocket.accept();
                serverSocketWrapper = new SocketWrapper(socket);
                System.out.println("Client accepted...");
                serve(serverSocketWrapper);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serve(SocketWrapper clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(clientSocket, appData, buyablePlayers, this);
        synchronized (connections) {
            connections.add(clientHandler);
        }
    }

    public void broadcast(Object update, ClientHandler clientHandler) {
        synchronized (connections) {
            Iterator<ClientHandler> iterator = connections.iterator();
            while (iterator.hasNext()) {
                ClientHandler handler = iterator.next();
                if (handler != clientHandler) {
                    try {
                        handler.sendUpdate(update);
                    } catch (IOException e) {
                        System.err.println("Failed to send update to a client: " + e.getMessage());
                        iterator.remove();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}