package Network;

import database.Club;
import database.Player;
import javafx.application.Platform;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import Main.Main;
import Network.searchRequests.*;
import Network.util.*;

public class Client {

    private final SocketWrapper socketWrapper;
    private final Main main;
    private volatile boolean keepListening;
    private final BlockingQueue<Object> updateQueue;
    private final ConcurrentHashMap<String, Object> responseMap;

    public Client(String ip, int port, Main main) throws IOException {
        synchronized (this) {
            this.socketWrapper = new SocketWrapper(ip, port);
            this.main = main;
            this.updateQueue = new LinkedBlockingQueue<>();
            this.responseMap = new ConcurrentHashMap<>();
        }
        startListenerThread();
    }

    // Start listener thread
    private void startListenerThread() {
        keepListening = true;
        Thread listenerThread = new Thread(() -> {
            try {
                while (keepListening) {
                    Object incoming = socketWrapper.read();
                    if (incoming != null) {
                        processIncomingObject(incoming);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in listener thread: " + e.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void processIncomingObject(Object incoming) {
        if (isResponse(incoming)) {
            // System.out.println(incoming);
            String requestId = extractRequestId(incoming);
            if (requestId != null) {
                responseMap.put(requestId, incoming);
            }
        } else {
            updateQueue.offer(incoming);
            handleIncomingUpdate(incoming);
        }
    }

    private boolean isResponse(Object incoming) {
        // Logic to determine if the object is a response
        return (incoming instanceof LoginRequest) || (incoming instanceof SearchByNameRequest)
                || (incoming instanceof SearchByClubAndCountryRequest) || (incoming instanceof CountryWiseCountRequest)
                || (incoming instanceof SearchByPositionRequest) ||
                (incoming instanceof SearchBySalaryRangeRequest) || (incoming instanceof AddPlayerRequest)
                || (incoming instanceof RequestChangePassword)||(incoming instanceof LoginRequestGuest);
    }

    private String extractRequestId(Object response) {
        if (response instanceof RequestIdProvider) {
            return ((RequestIdProvider) response).getRequestId();
        }
        throw new IllegalArgumentException("Response does not provide a request ID");
    }

    private void handleIncomingUpdate(Object update) {
        if (update instanceof sendBuablePlayerRequest request) {
            List<Player> updatedBuyablePlayers = request.getPlayers();
            synchronized (this) {
                main.buyablePlayer = updatedBuyablePlayers;
                Platform.runLater(() -> main.updateAllItem());

            }
            System.out.println("Updated buyable player list received from the server.");
        } else if (update instanceof SendAllPlayerRequest request) {
            synchronized (this) {
                main.allPlayers = request.getPlayers();
                Platform.runLater(() -> main.updateAllItem());
            }
            main.updateAllItem();
        } else if (update instanceof sendAllClubRequest request) {
            synchronized (this) {
                main.allClubs = request.getAllClubs();
            }
            Platform.runLater(() -> main.updateAllItem());
        }
    }

    // Stop the listener thread gracefully
    public synchronized void stopListenerThread() {
        keepListening = false;
    }

    // Wait for a response with a timeout
    private Object waitForResponse(String requestId, long timeout) throws InterruptedException {
        if (requestId == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout) {
            Object response = responseMap.get(requestId);
            if (response != null) {
                responseMap.remove(requestId);
                return response;
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
        throw new InterruptedException("Timeout waiting for response");
    }

    public synchronized Club sendLoginRequest(String username, String password) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new LoginRequest(username, password, requestId));
            Object response = waitForResponse(requestId, 5000);
            if (response instanceof LoginRequest) {
                // LoginRequest loginRequest = (LoginRequest) response;
                Club myClub = ((LoginRequest) response).getMyClub();

                if (myClub != null) {
                    main.myClub = myClub;
                    main.allClubs = ((LoginRequest) response).getAllClubs();
                    main.allPlayers = ((LoginRequest) response).getAllPlayers();
                    main.buyablePlayer = ((LoginRequest) response).getBuyablePlayers();
                }

                return myClub;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public synchronized boolean sendLoginRequestAsGuest() {
        boolean status = false;
        String requestId = generateUniqueRequestId();
        try {
            socketWrapper.write(new LoginRequestGuest(requestId));
            Object response = waitForResponse(requestId, 5000);
        if(response instanceof LoginRequestGuest ob2){
            main.allClubs = ob2.getAllClubs();
            main.allPlayers = ob2.getAllPlayers();
            if(ob2!=null){
                status = true;
            }
        }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return status;
    }

    public synchronized Player sendSearchByNameRequest(String name) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new SearchByNameRequest(requestId, name));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof SearchByNameRequest request) {
                Player player = request.getPlayer();
                return player;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public synchronized List<Player> sendSearchByClubAndCountryRequest(String club, String country) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new SearchByClubAndCountryRequest(requestId, club, country, null));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof SearchByClubAndCountryRequest request) {
                List<Player> player = request.getPlayers();
                return player;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public synchronized List<Player> sendSearchByPositionRequest(String position) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new SearchByPositionRequest(requestId, position, null));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof SearchByPositionRequest request) {
                List<Player> players = request.getPlayers();
                return players;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public synchronized List<Player> sendSearchBySalaryRequest(int maxSalary, int minSalary) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new SearchBySalaryRangeRequest(requestId, maxSalary, minSalary, null));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof SearchBySalaryRangeRequest request) {
                return request.getPlayers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized HashMap<String, Integer> sendCountryWiseCountRequest() {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new CountryWiseCountRequest(requestId, null));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof CountryWiseCountRequest request) {
                HashMap<String, Integer> countryWiseCount = request.getElements();
                return countryWiseCount;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public synchronized boolean addNewPlayerToServer(Player player) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new AddPlayerRequest(requestId, player, false));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof AddPlayerRequest request) {
                return request.getStatus();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean sendResetClubPassRequest(String name, String oldPassword, String newPassword) {
        try {
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new RequestChangePassword(requestId, name, oldPassword, newPassword, false));
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof RequestChangePassword request) {
                System.out.println(request.getStatus());
                return request.getStatus();
            }
        } catch (Exception e) {

        }
        return false;

    }

    public synchronized void sendBuyablePlayer(Player player) {
        try {
            socketWrapper.write(new BuyablePlayerObjec(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void RemoveFromBuyableList(Player player, String name) {
        System.out.println("in client working fine..");
        try {
            socketWrapper.write(new RemoveFromBuyablePlayers(name, player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateUniqueRequestId() {
        return Long.toHexString(System.nanoTime());
    }

    
}