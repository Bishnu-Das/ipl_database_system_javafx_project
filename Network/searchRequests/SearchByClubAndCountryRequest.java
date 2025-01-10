package Network.searchRequests;

import java.io.Serializable;
import java.util.List;

import Network.RequestIdProvider;
import database.Player;

public class SearchByClubAndCountryRequest implements Serializable, RequestIdProvider {
    String clubName;
    String countryName;
    String requestId;
    List<Player> players;

    public SearchByClubAndCountryRequest(String requestId, String clubName, String countryName, List<Player> players) {
        this.clubName = clubName;
        this.countryName = countryName;
        this.requestId = requestId;
        this.players = players;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String getRequestId() {
        // TODO Auto-generated method stub
        return requestId;
    }
}
