package fr.socket.florian.dhome.network

import fr.socket.florian.dhome.network.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

internal interface Network {

    @GET("api/")
    fun checkServer(): Call<CheckServer>

    @POST("api/auth/login")
    fun login(@Body body: RequestBody): Call<Login>

    @GET("api/device/all")
    fun allDevices(@Header("x-access-token") sessionToken: String): Call<List<Device>>

    @GET("api/auth/refreshToken")
    fun refreshToken(@Header("x-access-token") refreshToken: String): Call<Tokens>

    @GET("api/stream/create")
    fun createScanner(@Header("x-access-token") refreshToken: String): Call<Stream>

    @GET("api/stream/{id}")
    fun readScanner(@Header("x-access-token") refreshToken: String, @Path("id") id: String): Call<Data>

    @DELETE("api/stream/{id}")
    fun deleteScanner(@Header("x-access-token") refreshToken: String, @Path("id") id: String): Call<ErrorResponse>
}
