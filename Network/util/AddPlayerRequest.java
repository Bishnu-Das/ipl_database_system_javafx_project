package Network.util;

import java.io.Serializable;

import Network.RequestIdProvider;
import database.Player;

public class AddPlayerRequest implements Serializable, RequestIdProvider {
    Player player;
    String requstId;
    boolean status = false;

    public AddPlayerRequest(String requestId, Player player, boolean status) {
        this.player = player;
        this.status = status;
        this.requstId = requestId;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public String getRequestId() {
        // TODO Auto-generated method stub
        return requstId;
    }
}
