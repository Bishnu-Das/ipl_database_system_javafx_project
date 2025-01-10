package Network.util;

import java.io.Serializable;

import Network.RequestIdProvider;

public class RequestChangePassword implements Serializable, RequestIdProvider {
    String clubName;
    String oldPassword;
    String newPassword;
    String requstId;
    boolean stat;

    public RequestChangePassword(String requestId, String clubString, String password, String newpassword,
            boolean stat) {
        this.clubName = clubString;
        this.oldPassword = password;
        this.newPassword = newpassword;
        this.requstId = requestId;
        this.stat = stat;
    }

    public String getClubName() {
        return clubName;
    }

    public String getPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public boolean getStatus() {
        return stat;
    }

    @Override
    public String getRequestId() {
        return requstId;
    }
}
