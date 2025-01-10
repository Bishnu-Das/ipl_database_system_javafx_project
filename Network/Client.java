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
                (incoming instanceof SearchBySalaryRangeRequest)||(incoming instanceof AddPlayerRequest)||(incoming instanceof RequestChangePassword);
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
                Platform.runLater(()->main.updateAllItem());

            }
            System.out.println("Updated buyable player list received from the server.");
        } else if (update instanceof SendAllPlayerRequest request) {
            synchronized (this) {
                main.allPlayers = request.getPlayers();
                Platform.runLater(()->main.updateAllItem());
            }
            main.updateAllItem();
        }else if (update instanceof sendAllClubRequest request) {
            synchronized (this) {
                main.allClubs = request.getAllClubs();
            }
            Platform.runLater(()->main.updateAllItem());
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
                responseMap.remove(requestId); // Remove response after retrieving
                System.out.println("okay");
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
            Object response = waitForResponse(requestId, 2000);
            if (response instanceof LoginRequest) {
                // LoginRequest loginRequest = (LoginRequest) response;
                Club myClub = ((LoginRequest) response).getMyClub();
                
                if(myClub!=null){
                    main.myClub = myClub;
                    main.allClubs = ((LoginRequest)response).getAllClubs();
                    main.allPlayers = ((LoginRequest)response).getAllPlayers();
                    main.buyablePlayer =((LoginRequest)response).getBuyablePlayers();
                }
                
                // System.out.println("my club received in client...");
                //System.out.println(myClub.getName());
                return myClub;
            } else {
                System.out.println("something wrong");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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
            if(response instanceof AddPlayerRequest request){
                return request.getStatus();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    public synchronized boolean sendResetClubPassRequest(String name, String oldPassword, String newPassword) {
        try{
            String requestId = generateUniqueRequestId();
            socketWrapper.write(new RequestChangePassword(requestId, name, oldPassword, newPassword,false));
            Object response = waitForResponse(requestId, 2000);
            if(response instanceof RequestChangePassword request){
                System.out.println(request.getStatus());
                return request.getStatus();
            }
        }catch(Exception e){

        }
        return false;
        
    }
    public synchronized void sendBuyablePlayer(Player player){
        try {
            socketWrapper.write(new BuyablePlayerObjec(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void RemoveFromBuyableList(Player player, String name) {
        System.out.println("in client working fine..");
        try {
            socketWrapper.write(new RemoveFromBuyablePlayers(name,player));
            System.out.println("object written..");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public synchronized List<Player> sendAllPlayerRequest() {
    // try {
    // String requestId = generateUniqueRequestId();
    // socketWrapper.write(new SendAllPlayerRequest(requestId));
    // Object response = waitForResponse(requestId, 5000);
    // if (response instanceof SendAllPlayerRequest) {
    // List<Player> players = ((SendAllPlayerRequest) response).getPlayers();
    // if (main != null) {
    // main.setAllPlayerList(players);
    // System.out.println("Player list set in main...");
    // }
    // return players;
    // }
    // } catch (IOException | InterruptedException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    // public synchronized List<Club> getClubFromServer() {
    // try {
    // String requestId = generateUniqueRequestId();
    // socketWrapper.write(new sendAllClubRequest(requestId));
    // Object response = waitForResponse(requestId, 5000);
    // if (response instanceof sendAllClubRequest) {
    // List<Club> allClubs = ((sendAllClubRequest) response).getAllClubs();
    // main.allClubs = allClubs;
    // return allClubs;
    // }
    // } catch (IOException | InterruptedException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    // public synchronized List<Player> getBuyablePlayerList() {
    // try {
    // String requestId = generateUniqueRequestId();
    // socketWrapper.write(new sendBuablePlayerRequest(requestId));
    // Object response = waitForResponse(requestId, 5000);
    // if (response instanceof sendBuablePlayerRequest) {
    // List<Player> players = ((sendBuablePlayerRequest) response).getPlayers();
    // main.buyablePlayer = players;
    // return players;
    // }
    // } catch (IOException | InterruptedException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    // Additional methods can be refactored similarly

    private String generateUniqueRequestId() {
        return Long.toHexString(System.nanoTime());
    }

   

    

}

// package Network;

// import database.Club;
// import database.Player;

// import java.io.*;
// import java.util.HashMap;
// import java.util.List;

// import Main.Main;
// import Network.searchRequests.SearchByClubAndCountryRequest;
// import Network.searchRequests.SearchByPositionRequest;
// import Network.searchRequests.SearchBySalaryRangeRequest;
// import Network.searchRequests.SearchBynameRequest;
// import Network.util.RemoveFromBuyablePlayers;
// import Network.util.RequestChangePassword;
// import Network.util.SendAllPlayerRequest;
// import Network.util.sendAllClubRequest;
// import Network.util.sendBuablePlayerRequest;
// import Network.searchRequests.CountryWiseCountRequest;

// public class Client {

// public SocketWrapper socketWrapper;
// public Main main;
// private volatile boolean keepListening;

// public Client(String ip, int port, Main main) throws IOException {
// synchronized (this) {
// socketWrapper = new SocketWrapper(ip, port);
// this.main = main;
// }
// startListenerThread();
// }

// private void startListenerThread(){
// keepListening = true;
// Thread listnerThread = new Thread(()->{
// try{
// while (keepListening) {
// Object incoming = socketWrapper.read();
// if(incoming != null){
// handleIncomingUpdate(incoming);
// }
// }
// } catch(IOException | ClassNotFoundException e){

// }
// });
// listnerThread.setDaemon(true);
// listnerThread.start();
// }
// private void handleIncomingUpdate(Object update) {
// if (update instanceof sendBuablePlayerRequest request) {
// List<Player> updatedBuyablePlayers = request.getPlayers();
// synchronized (this) {
// main.buyablePlayer = updatedBuyablePlayers;
// }
// System.out.println("Updated buyable player list received from the server.");
// } else {
// System.out.println("Unknown update received: " + update);
// }
// }
// //Stop the listener thread gracefully....
// public synchronized void stopListenerThread(){
// keepListening = false;
// }

// public synchronized Club sentLoginRequest(String username, String password) {
// try {
// socketWrapper.write(new loginRequest(username, password));
// Club myClub = (Club) socketWrapper.read();
// main.myClub = myClub;
// return myClub;
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized List<Player> sentAllPlayerRequest() {
// try {
// socketWrapper.write(new SendAllPlayerRequest());
// Object ob = socketWrapper.read();

// if (ob instanceof SendAllPlayerRequest) {
// List<Player> players = ((SendAllPlayerRequest) ob).getPlayers();
// if(main!=null){
// main.setAllPlayerList(players);
// System.out.println("player list set in main...");
// }
// return players;
// } else {
// System.err.println("Unexpected object type received: " +
// ob.getClass().getName());
// }
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized List<Club> getClubFromSever() {
// try {
// socketWrapper.write(new sendAllClubRequest());
// Object ob = socketWrapper.read();

// List<Club> allClubs = ((sendAllClubRequest) ob).getAllClubs() ;
// main.allClubs = allClubs;
// return allClubs;
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized List<Player> getBuyablePlayerList() {
// try {
// socketWrapper.write(new sendBuablePlayerRequest());
// Object ob = socketWrapper.read();
// List<Player> players2 = ((sendBuablePlayerRequest) ob).getPlayers();
// main.buyablePlayer = players2;
// return players2;
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized void sentBuyablePlayer(Player player) {
// try {
// if (player == null) {
// throw new IllegalArgumentException("Player cannot be null");
// }
// socketWrapper.write(player);
// } catch (IOException e) {
// System.err.println("Error while sending the player: " + e.getMessage());
// e.printStackTrace();
// }
// }

// public synchronized void writeDummy(String string) throws IOException {
// socketWrapper.write(string);
// }

// public synchronized void sentRemovePlayerRequest(Player player, String
// clubName) {
// try {
// //socketWrapper.write(string);
// //socketWrapper.write(player);
// //socketWrapper.write(clubName);
// socketWrapper.write(new RemoveFromBuyablePlayers(clubName, player));
// } catch (IOException e) {
// System.err.println("Error while sending the player: " + e.getMessage());
// e.printStackTrace();
// }
// }

// public synchronized void sentAllPlayer(List<Player> allPlayers) {
// try {
// socketWrapper.write("writeplayeronfile");
// socketWrapper.write(allPlayers);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// public synchronized void sentAddPlayerRequestToClub(Player player, String
// name) {
// try {
// socketWrapper.write("addplayertoclub");
// socketWrapper.write(player);
// socketWrapper.write(name);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// public synchronized void addNewPlayerToServer(Player player) {
// try {
// socketWrapper.write("addnewplayertomainlist");
// socketWrapper.write(player);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// public synchronized Player sentSearchByNameRequest(String name) {
// try {
// socketWrapper.write(new SearchBynameRequest(name));
// return (Player) socketWrapper.read();
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized List<Player> sentSearchByClubAndCountryRequest(String
// club, String country) {
// try {
// socketWrapper.write(new SearchByClubAndCountryRequest(club, country));
// return (List<Player>) socketWrapper.read();
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized List<Player> sentSearchByPositionRequest(String position)
// {
// try {
// socketWrapper.write(new SearchByPositionRequest(position));
// return (List<Player>) socketWrapper.read();
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized List<Player> sentSearchBySalaryRequest(int low, int high)
// {
// try {
// socketWrapper.write(new SearchBySalaryRangeRequest(low, high));
// return (List<Player>) socketWrapper.read();
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized HashMap<String, Integer> sentCountryWiseCountRequest() {
// try {
// socketWrapper.write(new CountryWiseCountRequest("countryWiseCount"));
// return (HashMap<String, Integer>) socketWrapper.read();
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return null;
// }

// public synchronized boolean sentResetClubPassReq(RequestChangePassword
// requestChangePassword) {
// try {
// socketWrapper.write(requestChangePassword);
// Object ob2 = socketWrapper.read();
// return (boolean)ob2;
// } catch (IOException | ClassNotFoundException e) {
// e.printStackTrace();
// }
// return false;
// }
// }
