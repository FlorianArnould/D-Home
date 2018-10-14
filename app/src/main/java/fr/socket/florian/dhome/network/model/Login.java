package fr.socket.florian.dhome.network.model;

import com.google.gson.annotations.SerializedName;

public class Login extends ResponseBase {

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("sessionToken")
    private String sessionToken;

    public String getSessionToken() {
        return sessionToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
