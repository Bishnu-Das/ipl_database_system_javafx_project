package Network.util;

import java.io.Serializable;
import java.util.List;

import Network.RequestIdProvider;
import database.Club;
import database.Player;

public class LoginRequestGuest implements Serializable, RequestIdProvider {
    private String requestId;
    private List<Club> allClubs;

    private List<Player> allPlayers;
    private List<Player> buyablePlayers;

    public LoginRequestGuest(String requestId) {
        this.requestId = requestId;
    }

    public LoginRequestGuest(String requestId,List<Club> allClubs,List<Player> allPlayers) {
        this.requestId = requestId;
        this.allClubs = allClubs;
        this.allPlayers = allPlayers;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<Club> getAllClubs() {
        return allClubs;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public List<Player> getBuyablePlayers() {
        return buyablePlayers;
    }
}

