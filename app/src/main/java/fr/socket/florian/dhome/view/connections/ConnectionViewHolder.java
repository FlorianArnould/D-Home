package fr.socket.florian.dhome.view.connections;

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

    void update(Connection connection) {
        serverUrl.setText(connection.getServerUrl());
        username.setText(connection.getUsername());
        new ApiModule("https://" + connection.getServerUrl(), serverUrl.getContext()).provideApi().checkServer().enqueue(new Callback<CheckServer>() {
            @Override
            public void onResponse(@NonNull Call<CheckServer> call, @NonNull Response<CheckServer> response) {
                if (response.isSuccessful()) {
                    onSuccess();
                } else {
                    onFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckServer> call, @NonNull Throwable t) {
                onFailed();
            }
        });
    }

    private void onSuccess() {
        resultIcon.setColorFilter(resultIcon.getContext().getColor(R.color.green));
        resultIcon.setImageResource(R.drawable.ic_check);
    }

    private void onFailed() {
        resultIcon.setColorFilter(resultIcon.getContext().getColor(R.color.red));
        resultIcon.setImageResource(R.drawable.ic_close);
    }
}
