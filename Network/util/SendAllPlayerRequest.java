package Network.util;

import java.io.Serializable;
import java.util.List;

import database.Player;

public class SendAllPlayerRequest implements Serializable{
    List<Player> players;
    private String requestId;

    public SendAllPlayerRequest(String requestId){
        this.requestId = requestId;
    }
    public SendAllPlayerRequest(List<Player> players){
        this.players = players;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public String getRequestId(){
        return requestId;
    }
    
}
