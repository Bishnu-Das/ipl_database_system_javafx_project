package Network.searchRequests;

import java.io.Serializable;
import java.util.List;

import Network.RequestIdProvider;
import database.Player;

public class SearchByPositionRequest implements Serializable, RequestIdProvider {
    String position;
    String requestId;
    List<Player> players;

    public SearchByPositionRequest(String requestId, String position, List<Player> players) {
        this.position = position;
        this.requestId = requestId;
        this.players = players;
    }

    public String getPosition() {
        return position;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
