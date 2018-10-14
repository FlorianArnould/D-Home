package fr.socket.florian.dhome.view.connections;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.socket.florian.dhome.R;
import fr.socket.florian.dhome.database.Connection;
import fr.socket.florian.dhome.database.Database;
import fr.socket.florian.dhome.utils.Listener;
import fr.socket.florian.dhome.view.MainFragment;

public class ConnectionsFragment extends MainFragment {
    private ConnectionAdapter adapter;
    @Nullable
    private Database db;

    public ConnectionsFragment() {
        // This shouldn't be called from other classes if there are some arguments
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        adapter = new ConnectionAdapter(context);
        if (db == null) {
            db = new Database(context);
        }
        db.getConnections(new Listener<List<Connection>>() {
            @Override
            public void callback(List<Connection> connections) {
                adapter.update(connections);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(R.string.nav_connections);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
