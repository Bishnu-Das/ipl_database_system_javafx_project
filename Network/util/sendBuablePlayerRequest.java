package Network.util;

import java.io.Serializable;
import java.util.List;

import database.Player;

public class sendBuablePlayerRequest implements Serializable {
    List<Player> players;
    String requestId;

    public sendBuablePlayerRequest(String requestId) {
        this.requestId = requestId;
    }

    public sendBuablePlayerRequest(String requestId, List<Player> players) {
        this.players = players;
        this.requestId = requestId;
    }

    public sendBuablePlayerRequest(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getRequestId() {
        return requestId;
    }
}
