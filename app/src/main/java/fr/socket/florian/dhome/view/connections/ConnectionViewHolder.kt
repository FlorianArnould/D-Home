package fr.socket.florian.dhome.view.connections

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.socket.florian.dhome.R

import fr.socket.florian.dhome.database.Connection
import fr.socket.florian.dhome.network.ApiModule
import fr.socket.florian.dhome.network.model.CheckServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ConnectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val serverUrl: TextView = view.findViewById(R.id.serverUrl)
    private val username: TextView = view.findViewById(R.id.username)
    private val resultIcon: ImageView = view.findViewById(R.id.resultIcon)

    fun update(connection: Connection) {
        serverUrl.text = connection.serverUrl
        username.text = connection.username
//        ApiModule("https://" + connection.serverUrl, serverUrl.context).provideApi().checkServer().enqueue(object : ApiCallback<CheckServer> {
//            override fun onResponse(call: Call<CheckServer>, response: Response<CheckServer>) {
//                if (response.isSuccessful) {
//                    onSuccess()
//                } else {
//                    onFailed()
//                }
//            }
//
//            override fun onFailure(call: Call<CheckServer>, t: Throwable) {
//                onFailed()
//            }
//        })
    }

    private fun onSuccess() {
        resultIcon.setColorFilter(resultIcon.context.getColor(R.color.green))
        resultIcon.setImageResource(R.drawable.ic_check)
    }

    private fun onFailed() {
        resultIcon.setColorFilter(resultIcon.context.getColor(R.color.red))
        resultIcon.setImageResource(R.drawable.ic_close)
    }
}
