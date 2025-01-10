package Network.searchRequests;

import java.io.Serializable;
import java.util.List;

import Network.RequestIdProvider;
import database.Player;

public class SearchBySalaryRangeRequest implements Serializable, RequestIdProvider {
    int minSalary;
    int maxSalary;
    String requestId;
    List<Player> players;

    public SearchBySalaryRangeRequest(String requestId, int minSalary, int maxSalary, List<Player> players) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.requestId = requestId;
        this.players = players;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
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
