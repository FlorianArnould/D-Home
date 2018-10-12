package fr.socket.florian.dhome.network;

import com.google.gson.annotations.SerializedName;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Network {

    @GET("api/")
    Call<CheckServer> checkServer();

    @POST("api/auth/login")
    Call<Login> login(@Body RequestBody body);

    class CheckServer {
        @SerializedName("running")
        private boolean running;

        public boolean isRunning() {
            return running;
        }
    }

    class Login {
        @SerializedName("auth")
        private boolean auth;

        @SerializedName("refreshToken")
        private String refreshToken;

        @SerializedName("sessionToken")
        private String sessionToken;

        @SerializedName("message")
        private String message;

        public boolean isAuth() {
            return auth;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public String getMessage() {
            return message;
        }
    }
}
