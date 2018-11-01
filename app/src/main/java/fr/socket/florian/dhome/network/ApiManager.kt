package fr.socket.florian.dhome.network

import android.content.Context
import android.util.Log

import java.io.Closeable

import fr.socket.florian.dhome.database.Connection
import fr.socket.florian.dhome.database.Database
import fr.socket.florian.dhome.network.model.CheckServer
import fr.socket.florian.dhome.network.model.Device
import fr.socket.florian.dhome.network.model.Login
import fr.socket.florian.dhome.network.model.Tokens
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager : Closeable {

    private lateinit var connection: Connection
    private lateinit var network: Network
    private lateinit var db: Database

    fun initialize(context: Context, connectionId: Int, callback: (ApiManager) -> Unit): ApiManager {
        db = Database(context)
        db.getConnectionById(connectionId) { connection ->
            this@ApiManager.connection = connection
            network = ApiModule("https://" + connection.serverUrl, context).provideApi()
            callback(this@ApiManager)
        }
        return this
    }

    fun getAllDevices(callback: (List<Device>?) -> Unit, shouldRetry: Boolean = true) {
        checkInitialization()
        network.allDevices(connection.sessionToken).enqueue(object : Callback<List<Device>> {
            override fun onResponse(call: Call<List<Device>>, response: Response<List<Device>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if(body != null) {
                        callback(body)
                        return
                    }
                }
                // TODO: 14/10/18 parse the errorBody
                if(shouldRetry) refreshSessionToken{ getAllDevices(callback, false) }
                else callback(null)
            }

            override fun onFailure(call: Call<List<Device>>, t: Throwable) {
                Log.e("ApiManager getAllDevices", "Got Failure : " + t.message, t)
                callback(null)
            }
        })
    }

    private fun refreshSessionToken(callback: () -> Unit) {
        Log.i("ApiManager", "refreshing session token")
        network.refreshToken(connection.refreshToken).enqueue(object : Callback<Tokens> {
            override fun onResponse(call: Call<Tokens>, response: Response<Tokens>) {
                if (response.isSuccessful && response.body() != null) {
                    connection.sessionToken = response.body()!!.sessionToken!!
                    val refreshToken = response.body()!!.refreshToken
                    if (refreshToken != null) {
                        connection.refreshToken = refreshToken
                    }
                    db.updateConnection(connection)
                    callback()
                } else {
                    Log.e("ApiManager refreshToken", "Got an error : " + response.code())
                }
            }

            override fun onFailure(call: Call<Tokens>, t: Throwable) {
                Log.e("ApiManager refreshToken", "Got Failure : " + t.message, t)
            }
        })
    }

    override fun close() {
        if (this::db.isInitialized) db.close()
    }

    private fun checkInitialization() {
        if(!this::connection.isInitialized || !this::network.isInitialized || !this::db.isInitialized) {
            throw ApiManagerException("The instance was not initialized")
        }
    }

    companion object {
        fun checkServer(context: Context, url: String, callback: ApiCallback<CheckServer>) {
            ApiModule("https://$url", context).provideApi().checkServer().enqueue(callback)
        }

        fun login(context: Context, url:String, username: String, password: String, callback: ApiCallback<Login>) {
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)
            val body = RequestBody.create(MediaType.parse("application/json"), json.toString())
            ApiModule("https://$url", context).provideApi().login(body).enqueue(callback)
        }
    }
}
