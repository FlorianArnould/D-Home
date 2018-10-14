package fr.socket.florian.dhome.view.connections;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.socket.florian.dhome.R;
import fr.socket.florian.dhome.database.Connection;

class ConnectionAdapter extends RecyclerView.Adapter<ConnectionViewHolder> {

    private List<Connection> connections;

    ConnectionAdapter() {
        connections = new ArrayList<>();
    }

    void update(List<Connection> connections) {
        this.connections = connections;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_row, parent, false);
        return new ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder connectionViewHolder, int i) {
        connectionViewHolder.update(connections.get(i));
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }
}
