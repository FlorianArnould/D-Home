package fr.socket.florian.dhome.database;

public class Connection {
    private final String serverUrl;
    private final String username;
    private final String refreshToken;
    private final String sessionToken;

    public Connection(String serverUrl, String username, String refreshToken, String sessionToken) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.refreshToken = refreshToken;
        this.sessionToken = sessionToken;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
