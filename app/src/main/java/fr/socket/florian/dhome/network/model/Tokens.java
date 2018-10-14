package fr.socket.florian.dhome.network.model;

import com.google.gson.annotations.SerializedName;

public class Tokens extends ResponseBase {

    @SerializedName("sessionToken")
    private String sessionToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    public String getSessionToken() {
        return sessionToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
