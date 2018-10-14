package fr.socket.florian.dhome.network.model;

import com.google.gson.annotations.SerializedName;

public class ResponseBase {

    @SerializedName("auth")
    private boolean auth;

    @SerializedName("message")
    private String message;

    public boolean isAuth() {
        return auth;
    }

    public String getMessage() {
        return message;
    }

}
