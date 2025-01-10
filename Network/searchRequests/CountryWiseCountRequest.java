package Network.searchRequests;

import java.io.Serializable;
import java.util.HashMap;

import Network.RequestIdProvider;

public class CountryWiseCountRequest implements Serializable, RequestIdProvider {
    String requestId;
    HashMap<String, Integer> countryWiseCount;

    public CountryWiseCountRequest(String requestId, HashMap<String, Integer> countryWiseCount) {
        this.requestId = requestId;
        this.countryWiseCount = countryWiseCount;
    }

    public HashMap<String, Integer> getElements() {
        return countryWiseCount;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }
}
