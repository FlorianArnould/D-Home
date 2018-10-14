package fr.socket.florian.dhome.network.model;

import com.google.gson.annotations.SerializedName;

public class CheckServer {
    @SerializedName("running")
    private boolean running;

    public boolean isRunning() {
        return running;
    }
}
