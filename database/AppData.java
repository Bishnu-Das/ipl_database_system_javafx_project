package database;

import java.io.Serializable;
import java.util.List;

public class AppData implements Serializable{
    private PlayerManagement playerManagement;
    
    public AppData() {
        try {
            this.playerManagement = new PlayerManagement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PlayerManagement getPlayerMangement(){
        return playerManagement;
    }
    public List<Player> getAllPlayers(){
        return playerManagement.getAllPlayers();
    }
    public List<Club> getAllClubs(){
        return playerManagement.getAllClubs();
    }
}
