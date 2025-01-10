package Network.util;

import java.io.Serializable;

import database.Player;

public class BuyablePlayerObjec implements Serializable {
    Player player;

    public BuyablePlayerObjec(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
