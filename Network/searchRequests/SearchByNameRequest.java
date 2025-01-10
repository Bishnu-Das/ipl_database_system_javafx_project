package Network.searchRequests;

import java.io.Serializable;

import Network.RequestIdProvider;
import database.Player;

public class SearchByNameRequest implements Serializable, RequestIdProvider {

    private String name;
    private String requestId;
    private Player player;

    public SearchByNameRequest(String requestId, String name) {
        this.name = name;
        this.requestId = requestId;
    }

    public SearchByNameRequest(String requestId, String name, Player player) {
        this.name = name;
        this.requestId = requestId;
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestId() {
        return requestId;
    }

    public Player getPlayer() {
        return player;
    }
}