package Network.util;

import java.io.Serializable;

import Network.RequestIdProvider;
import database.Player;

public class RemoveFromBuyablePlayers implements Serializable, RequestIdProvider {
    Player player;
    String curClubName;
    String prevClubName;

    public RemoveFromBuyablePlayers() {
    }

    public RemoveFromBuyablePlayers(String curClubName, Player player) {
        this.curClubName = curClubName;
        this.player = player;
        this.prevClubName = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCurClubName() {
        return curClubName;
    }

    public void setCurClubName(String curClubName) {
        this.curClubName = curClubName;
    }

    public String getPrevClubName() {
        return prevClubName;
    }

    public void setPrevClubName(String prevClubName) {
        this.prevClubName = prevClubName;
    }

    @Override
    public String getRequestId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestId'");
    }
}
