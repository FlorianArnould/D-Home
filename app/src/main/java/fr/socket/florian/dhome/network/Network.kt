package fr.socket.florian.dhome.network

import fr.socket.florian.dhome.network.model.CheckServer
import fr.socket.florian.dhome.network.model.Device
import fr.socket.florian.dhome.network.model.Login
import fr.socket.florian.dhome.network.model.Tokens
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

internal interface Network {

    @GET("api/")
    fun checkServer(): Call<CheckServer>

    @POST("api/auth/login")
    fun login(@Body body: RequestBody): Call<Login>

    @GET("api/device/all")
    fun allDevices(@Header("x-access-token") sessionToken: String): Call<List<Device>>

    @GET("api/auth/refreshToken")
    fun refreshToken(@Header("x-access-token") refreshToken: String): Call<Tokens>

}
