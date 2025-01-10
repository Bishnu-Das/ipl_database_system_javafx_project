package Network;

import database.AppData;
import database.Player;
import database.Club;
import Network.ClientHandler;

import java.io.*;
import java.util.*;

import Network.searchRequests.*;
import Network.util.*;

public class ClientHandler implements Runnable {
    private Thread thread;
    private AppData appData;
    private List<Player> buyablePlayers;
    private SocketWrapper socketWrapper;
    private Server server;

    public ClientHandler(SocketWrapper socketWrapper, AppData appData, List<Player> buyablePlayers, Server server) {
        this.appData = appData;
        this.buyablePlayers = buyablePlayers;
        this.socketWrapper = socketWrapper;
        this.server = server;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object ob = socketWrapper.read();
                handleRequest(ob);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleRequest(Object ob) throws IOException {
        synchronized (appData) {
            if (ob instanceof LoginRequest login) {// done
                handleLoginRequest(login);
            } else if (ob instanceof SearchByNameRequest search) {
                socketWrapper.write(new SearchByNameRequest(search.getRequestId(), null,
                        appData.getPlayerMangement().searchByName(search.getName())));
            } else if (ob instanceof SearchByClubAndCountryRequest search) {
                List<Player> players = new ArrayList<>(appData.getPlayerMangement()
                        .searchByClubAndCountry(search.getClubName(), search.getCountryName()));
                socketWrapper.write(new SearchByClubAndCountryRequest(search.getRequestId(), null, null, players));
            } else if (ob instanceof SearchByPositionRequest search) {
                List<Player> players = new ArrayList<>(
                        appData.getPlayerMangement().searchByPosition(search.getPosition()));
                socketWrapper.write(new SearchByPositionRequest(search.getRequestId(), null, players));

            } else if (ob instanceof SearchBySalaryRangeRequest search) {
                List<Player> players = new ArrayList<>(
                        appData.getPlayerMangement().searchInRangeSalary(search.getMinSalary(), search.getMaxSalary()));
                // System.out.println(players);
                socketWrapper.write(new SearchBySalaryRangeRequest(search.getRequestId(), 0, 0, players));
            } else if (ob instanceof CountryWiseCountRequest search) {
                HashMap<String, Integer> countryWiseCount = new HashMap<>(
                        appData.getPlayerMangement().countryWiseCount());
                socketWrapper.write(new CountryWiseCountRequest(search.getRequestId(), countryWiseCount));
            } else if (ob instanceof RequestChangePassword ob2) {
                handleChangePasswordRequest(ob2);
            } else if (ob instanceof AddPlayerRequest request) {
                boolean stat = appData.getPlayerMangement().addPlayer(request.getPlayer());
                appData.getPlayerMangement().writePlayerInFile(appData.getAllPlayers());
                socketWrapper.write(new AddPlayerRequest(request.getRequestId(), null, stat));
                socketWrapper.write(new sendAllClubRequest(null, appData.getAllClubs()));
                socketWrapper.write(new SendAllPlayerRequest(appData.getAllPlayers()));
            } else if (ob instanceof BuyablePlayerObjec ob2) {
                Player newPlayer = ob2.getPlayer();
                appData.getPlayerMangement().changeClub(newPlayer, "none");
                newPlayer.setClub("none");
                buyablePlayers.add(ob2.getPlayer());
                appData.getPlayerMangement().writePlayerInFile(appData.getAllPlayers());
                server.broadcast(new sendBuablePlayerRequest(buyablePlayers), null);
                server.broadcast(new sendAllClubRequest(appData.getAllClubs()), null);
                server.broadcast(new SendAllPlayerRequest(appData.getAllPlayers()), null);
            } else if (ob instanceof RemoveFromBuyablePlayers ob2) {
                Player player = ob2.getPlayer();
                String clubName = ob2.getCurClubName();
                appData.getPlayerMangement().changeClub(player, clubName);
                buyablePlayers.remove(player);
                appData.getPlayerMangement().writePlayerInFile(appData.getAllPlayers());
                server.broadcast(new sendBuablePlayerRequest(buyablePlayers), null);
                server.broadcast(new sendAllClubRequest(appData.getAllClubs()), null);
                server.broadcast(new SendAllPlayerRequest(appData.getAllPlayers()), null);
            } else {
                System.out.println("unknown object received...");
            }
        }
    }

    private void handleLoginRequest(LoginRequest login) throws IOException {
        String username = login.getUsername();
        String password = login.getPassword();
        String requestId = login.getRequestId();
        System.out.println(username + " " + password + " " + requestId);
        if (appData.getPlayerMangement().getClubPasswords().containsKey(username)
                && appData.getPlayerMangement().getClubPasswords().get(username).equals(password)) {
            Club myClub = appData.getPlayerMangement().getClub(username);
            List<Player> allPlayers = appData.getAllPlayers();
            List<Club> allClubs = appData.getAllClubs();
            allClubs.removeIf(club -> club.getName().equals("none"));

            socketWrapper.write(
                    new LoginRequest(username, password, requestId, myClub, allClubs, allPlayers, buyablePlayers));
        } else {
            socketWrapper.write(new LoginRequest(username, password, requestId, null, null, null, null));
        }
    }

    private void handleChangePasswordRequest(RequestChangePassword ob2) throws IOException {
        String clubName = ob2.getClubName();
        String oldPass = ob2.getPassword();
        String newPass = ob2.getNewPassword();
        if (appData.getPlayerMangement().getClubPasswords().containsKey(clubName)
                && appData.getPlayerMangement().getClubPasswords().get(clubName).equals(oldPass)) {
            appData.getPlayerMangement().getClubPasswords().put(clubName, newPass);
            appData.getPlayerMangement().writePasswordOnFile();
            socketWrapper.write(new RequestChangePassword(ob2.getRequestId(), null, null, null, true));
        } else {
            socketWrapper.write(new RequestChangePassword(ob2.getRequestId(), null, null, null, false));
        }
    }

    private void closeConnection() {
        try {
            socketWrapper.closeConnection();
            synchronized (server.connections) {
                server.connections.remove(this);
            }
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }

    public void sendUpdate(Object update) throws IOException {
        socketWrapper.write(update);
    }
}
