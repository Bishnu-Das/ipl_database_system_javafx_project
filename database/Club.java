package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Club implements Serializable{
    private String name;
    private List<Player> players = new ArrayList<>();
    private double totalSalary;
    
    public Club(String name, List<Player> players2) {
        this.name = name;
        double curSalary = 0;
        for(Player player: players2){
            if(player.getClub().equals(name)){
                this.players.add(player);
                curSalary += player.getWeeklySalary();
            }
        }
        this.totalSalary = curSalary;
    }

    public void addPlayer(Player player){
        if(player.getClub().equals(name)){
            this.players.add(player);
            this.totalSalary += player.getWeeklySalary();
        }
    }
    
    public void removePlayer(Player player){
        if(player.getClub().equals(name)){
            this.players.remove(player);
            this.totalSalary -= player.getWeeklySalary();
        }
    }

    //getters
    public String getName() {
        return name;
    }
    
    public List<Player> getMaxSalaryPlayer(String player) {
        List<Player> newPlayers = new ArrayList<Player>();
        Player player2 = this.players.get(0);
        for(Player curPlayer: players){
            if(curPlayer.getWeeklySalary() > player2.getWeeklySalary()){
                player2 = curPlayer;
            }
        }
        for(Player curPlayer: players){
            if(curPlayer.getWeeklySalary() == player2.getWeeklySalary()){
                newPlayers.add(curPlayer);
            }
        }
        return newPlayers;
    }
    public List<Player> getMaxAgePlayer(String player){
        List<Player> newPlayers = new ArrayList<Player>();
        Player player2 = this.players.get(0);
        for(Player curPlayer: players){
            if(curPlayer.getAge() > player2.getAge()){
                player2 = curPlayer;
            }
        }
        for(Player curPlayer: players){
            if(curPlayer.getAge() == player2.getAge()){
                newPlayers.add(curPlayer);
            }
        }
        return newPlayers;
    }
    public List<Player> getMaxHeightPlayer(String player){
        List<Player> newPlayers = new ArrayList<Player>();
        Player player2 = this.players.get(0);
        for(Player curPlayer: players){
            if(curPlayer.getHeight() > player2.getHeight()){
                player2 = curPlayer;
            }
        }
        for(Player curPlayer: players){
            if(curPlayer.getHeight() == player2.getHeight()){
                newPlayers.add(curPlayer);
            }
        }
        return newPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public double getTotalSalary() {
        return totalSalary;
    }

    @Override
    public String toString() {
        return "Club [name=" + name + ", players=" + players + ", totalSalary=" + totalSalary + "]";
    }
}
