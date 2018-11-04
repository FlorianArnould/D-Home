package fr.socket.florian.dhome.view.connections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.database.Connection
import java.util.*

internal class ConnectionAdapter : RecyclerView.Adapter<ConnectionViewHolder>() {

    private var connections: List<Connection> = ArrayList()

    fun update(connections: List<Connection>) {
        this.connections = connections
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ConnectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.connection_row, parent, false)
        return ConnectionViewHolder(view)
    }

    override fun onBindViewHolder(connectionViewHolder: ConnectionViewHolder, i: Int) {
        connectionViewHolder.update(connections[i])
    }

    override fun getItemCount(): Int {
        return connections.size
    }
}
