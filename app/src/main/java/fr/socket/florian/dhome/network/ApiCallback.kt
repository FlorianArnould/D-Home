package fr.socket.florian.dhome.network

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T> : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                onSuccess(body)
            } else {
                onFailure(null, response.code())
            }
        } else {
            val body = response.errorBody()
            if (body != null) {
                try {
                    onFailure(JSONObject(body.string()), response.code())
                } catch (e: JSONException) {
                    Log.e("Parsing errorBody", "Cannot parse : " + body.string(), e)
                    onFailure(null, response.code())
                }
            } else {
                onFailure(null, response.code())
            }
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.e("Api call", "Got Failure", t)
        onFailure(null, -1)
    }

    abstract fun onSuccess(response: T)

    abstract fun onFailure(response: JSONObject?, code: Int)
}