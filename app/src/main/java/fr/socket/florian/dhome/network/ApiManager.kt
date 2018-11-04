package fr.socket.florian.dhome.network

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import fr.socket.florian.dhome.database.Connection
import fr.socket.florian.dhome.database.Database
import fr.socket.florian.dhome.network.model.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Closeable
import java.util.concurrent.CountDownLatch

class ApiManager : Closeable {

    private lateinit var connection: Connection
    private lateinit var network: Network
    private lateinit var db: Database
    private var counter: Int = 0
    private lateinit var countDown: CountDownLatch

    fun initialize(context: Context, connectionId: Int, callback: (ApiManager) -> Unit): ApiManager {
        db = Database(context)
        db.getConnectionById(connectionId) { connection ->
            this@ApiManager.connection = connection
            network = ApiModule("https://" + connection.serverUrl, context).provideApi()
            callback(this@ApiManager)
        }
        return this
    }

    private fun decrementCounter() {
        if (this::countDown.isInitialized) countDown.countDown()
        counter--
    }

    private fun<T> request(call: Call<T>, callback: (T?) -> Unit, shouldRetry: Boolean = true) {
        counter++
        val newCallback = { it: T? ->
            callback(it)
            decrementCounter()
        }
        checkInitialization()
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        newCallback(body)
                        return
                    }
                }
                // TODO: 14/10/18 parse the errorBody
                if (shouldRetry && response.code() == 401) refreshSessionToken { succeed ->
                    if (succeed) request(call.clone(), newCallback, false)
                    else newCallback(null)
                }
                else newCallback(null)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e("ApiManager getAllDevices", "Got Failure : " + t.message, t)
                newCallback(null)
            }
        })
    }

    fun getAllDevices(callback: (List<Device>?) -> Unit) {
        request(network.allDevices(connection.sessionToken), callback)
    }

    fun createScanner(callback: (Stream?) -> Unit) {
        request(network.createScanner(connection.sessionToken), callback)
    }

    fun readScanner(id: String, callback: (Data?) -> Unit) {
        request(network.readScanner(connection.sessionToken, id), callback)
    }

    fun deleteScanner(id: String, callback: (ErrorResponse?) -> Unit) {
        request(network.deleteScanner(connection.sessionToken, id), callback)
    }

    private fun refreshSessionToken(callback: (succeed: Boolean) -> Unit) {
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
                    callback(true)
                } else {
                    Log.e("ApiManager refreshToken", "Got an error : " + response.code())
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Tokens>, t: Throwable) {
                Log.e("ApiManager refreshToken", "Got Failure : " + t.message, t)
                callback(false)
            }
        })
    }

    override fun close() {
        Log.d("ApiManager close", "counter = $counter")
        if (this::db.isInitialized) {
            if (counter == 0) db.close()
            else {
                countDown = CountDownLatch(counter)
                Closer(countDown, db).execute()
            }
        }
    }

    private fun checkInitialization() {
        if (!this::connection.isInitialized || !this::network.isInitialized || !this::db.isInitialized) {
            throw ApiManagerException("The instance was not initialized")
        }
    }

    companion object {
        fun checkServer(context: Context, url: String, callback: ApiCallback<CheckServer>) {
            ApiModule("https://$url", context).provideApi().checkServer().enqueue(callback)
        }

        fun login(context: Context, url: String, username: String, password: String, callback: ApiCallback<Login>) {
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)
            val body = RequestBody.create(MediaType.parse("application/json"), json.toString())
            ApiModule("https://$url", context).provideApi().login(body).enqueue(callback)
        }

        private class Closer(private val countDown: CountDownLatch, private val db: Database) : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                countDown.await()
                db.close()
                Log.d("ApiManager close", "finally closed")
                return null
            }
        }
    }
}
