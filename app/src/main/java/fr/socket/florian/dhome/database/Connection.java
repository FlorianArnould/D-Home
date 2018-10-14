package fr.socket.florian.dhome.database;

public class Connection {
    private final String serverUrl;
    private final String username;
    private int id;
    private String refreshToken;
    private String sessionToken;

    public Connection(String serverUrl, String username, String refreshToken, String sessionToken) {
        this(0, serverUrl, username, refreshToken, sessionToken);
    }

    public Connection(int id, String serverUrl, String username, String refreshToken, String sessionToken) {
        this.id = id;
        this.serverUrl = serverUrl;
        this.username = username;
        this.refreshToken = refreshToken;
        this.sessionToken = sessionToken;
    }

    public int getId() {
        return id;
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

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
