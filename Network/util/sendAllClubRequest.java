package Network.util;

import java.io.Serializable;
import java.util.List;

import database.Club;

public class sendAllClubRequest implements Serializable {
    List<Club> allClubs;
    String requestId;

    public sendAllClubRequest(String requestId) {
        this.requestId = requestId;
    }

    public sendAllClubRequest(String requestId, List<Club> allClubs) {
        this.allClubs = allClubs;
        this.requestId = requestId;
    }

    public sendAllClubRequest(List<Club> allClubs) {
        this.allClubs = allClubs;
    }

    public List<Club> getAllClubs() {
        return allClubs;
    }

    public void setAllClubs(List<Club> allClubs) {
        this.allClubs = allClubs;
    }

    public String getRequestId() {
        return requestId;
    }
}
