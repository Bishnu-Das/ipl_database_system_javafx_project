package Network.util;

import java.io.Serializable;
import java.util.List;

import Network.RequestIdProvider;
import database.Club;
import database.Player;

public class LoginRequest implements Serializable, RequestIdProvider {
    private String username;
    private String password;
    private Club myClub;
    private String requestId;
    private List<Club> allClubs;

    private List<Player> allPlayers;
    private List<Player> buyablePlayers;

    public LoginRequest(String username, String password, String requestId) {
        this.username = username;
        this.password = password;
        this.myClub = null;
        this.requestId = requestId;
    }

    public LoginRequest(String username, String password, String requestId, Club myClub, List<Club> allClubs,
            List<Player> allPlayers, List<Player> buyablePlayer) {
        this.username = username;
        this.password = password;
        this.myClub = myClub;
        this.requestId = requestId;
        this.allClubs = allClubs;
        this.allPlayers = allPlayers;
        this.buyablePlayers = buyablePlayer;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Club getMyClub() {
        return myClub;
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

    @Override
    public String toString() {
        return username + " " + password + " " + requestId;
    }
}
