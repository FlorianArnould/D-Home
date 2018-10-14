package fr.socket.florian.dhome.view.connections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.socket.florian.dhome.R;
import fr.socket.florian.dhome.database.Connection;
import fr.socket.florian.dhome.network.ApiModule;
import fr.socket.florian.dhome.network.model.CheckServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ConnectionViewHolder extends RecyclerView.ViewHolder {

    private final TextView serverUrl;
    private final TextView username;
    private final ImageView resultIcon;

    ConnectionViewHolder(@NonNull View view) {
        super(view);
        serverUrl = view.findViewById(R.id.server_url);
        username = view.findViewById(R.id.username);
        resultIcon = view.findViewById(R.id.result_icon);
    }

    void update(Context context, Connection connection) {
        serverUrl.setText(connection.getServerUrl());
        username.setText(connection.getUsername());
        new ApiModule("https://" + connection.getServerUrl(), context).provideApi().checkServer().enqueue(new Callback<CheckServer>() {
            @Override
            public void onResponse(@NonNull Call<CheckServer> call, @NonNull Response<CheckServer> response) {
                if (response.isSuccessful()) {
                    resultIcon.setImageResource(R.drawable.ic_check_green);
                } else {
                    resultIcon.setImageResource(R.drawable.ic_close_red);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckServer> call, @NonNull Throwable t) {
                resultIcon.setImageResource(R.drawable.ic_close_red);
            }
        });
    }
}
