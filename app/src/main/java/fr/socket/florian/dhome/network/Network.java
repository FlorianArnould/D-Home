package fr.socket.florian.dhome.network;

import java.util.List;

import fr.socket.florian.dhome.network.model.CheckServer;
import fr.socket.florian.dhome.network.model.Device;
import fr.socket.florian.dhome.network.model.Login;
import fr.socket.florian.dhome.network.model.Tokens;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Network {

    @GET("api/")
    Call<CheckServer> checkServer();

    @POST("api/auth/login")
    Call<Login> login(@Body RequestBody body);

    @GET("api/device/all")
    Call<List<Device>> allDevices(@Header("x-access-token") String sessionToken);

    @GET("api/auth/refreshToken")
    Call<Tokens> refreshToken(@Header("x-access-token") String refreshToken);

}
